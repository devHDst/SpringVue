package com.restapi.vueconnect.security.core;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.restapi.vueconnect.model.entity.Account;

import lombok.extern.slf4j.Slf4j;

//ライブラリのリクエストヘッダー認証用クラスを拡張してログイン済のトークンをチェック
@Slf4j
@Service
public class SecurityTokenExt implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    
    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException{
        //トークンの解読と認証をここで実施する
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC256("secret")).build().verify(token.getPrincipal().toString());
        } catch (JWTDecodeException ex) {
            throw new BadCredentialsException("不正なアクセスです");
        }
        if(decodedJWT.getToken().isEmpty()) throw new UsernameNotFoundException("認証情報が不足しております");

        //デコードした後はトークンに持たせているユーザー情報を取得して、カスタムの認証レコードに渡す
        Account account = new Account();
        account.setUsername(decodedJWT.getClaim("username").asString());
        account.setPassword("");
        account.setRoles(decodedJWT.getClaim("role").asString());
        
        //アカウントの情報が登録されているモデルクラスに渡す
        return new SecurityRecordExt(account);
    }
}
