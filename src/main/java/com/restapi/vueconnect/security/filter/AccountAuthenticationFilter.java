package com.restapi.vueconnect.security.filter;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.vueconnect.security.core.SecurityRecordExt;
import com.restapi.vueconnect.security.payload.LoginForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    //初回ログイン用にspringSecurityから認証用フィルターを拡張

    private final AuthenticationManager authenticationManager;

    public AccountAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        
        //初回の認証をするAPIのURLを設定する
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/account/signin","POST"));

        var issuedAt = new Date();
        
        //成功時の処理を記載
        this.setAuthenticationSuccessHandler((req,res,ex)->{
            //認証成功時にトークンをヘッダーに発行(TokenAuthenticationFilterで必須)
            String token = JWT.create().withIssuer("test-issure").withIssuedAt(issuedAt)
                    .withExpiresAt(new Date(issuedAt.getTime() + 1000 * 60 * 60))
                    // ユーザ名
                    .withClaim("username", ex.getName())
                    // ロール情報
                    .withClaim("role", ex.getAuthorities().iterator().next().toString())
                    .sign(Algorithm.HMAC256("secret"));
            
            //CORS対策でオリジナルパラメータは許可設定を足さないと取得できない(らしい)
            res.setHeader("Access-Control-Expose-Headers", "X-AUTH-TOKEN");
            res.setHeader("X-AUTH-TOKEN", token);
            res.setStatus(200);
            log.info(token);
            //ここでフィルターに認証するべきモデルクラスの情報を連携させている
            SecurityRecordExt securityAccount = (SecurityRecordExt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            res.getWriter().write((new ObjectMapper().writeValueAsString(securityAccount.getAccount())));
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            // リクエストのデータをjavaオブジェクト->Jsonに変換
            LoginForm principal = new ObjectMapper().readValue(request.getInputStream(), LoginForm.class);
            // 認証処理を実行するクラスに引き渡す(DaoAuthenticaionProvider)
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(principal.getUsername(), principal.getPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
