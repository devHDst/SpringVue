package com.restapi.vueconnect.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.slf4j.Slf4j;

//ヘッダー認証用のライブラリクラスを継承して発行済みのトークンを調べる
@Slf4j
public class TokenAuthenticationFilter extends RequestHeaderAuthenticationFilter{    
   
    public TokenAuthenticationFilter(AuthenticationManager authenticationManager){
        setPrincipalRequestHeader("Authorization");
        setExceptionIfHeaderMissing(false);
        setAuthenticationManager(authenticationManager);
        //トークン認証が必要なAPIを指定
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/task/**"));

        this.setAuthenticationSuccessHandler((req, res, ex) -> {
            log.info("ログインしました");
        });
        this.setAuthenticationFailureHandler((req, res, ex) -> {
            log.info("認証に失敗しました");
        });
    }
}
