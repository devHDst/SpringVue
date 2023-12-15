package com.restapi.vueconnect.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import com.restapi.vueconnect.security.core.SecuritySqlExt;
import com.restapi.vueconnect.security.core.SecurityTokenExt;
import com.restapi.vueconnect.security.filter.AccountAuthenticationFilter;
import com.restapi.vueconnect.security.filter.TokenAuthenticationFilter;

//ブラウザからのペイロードを管理するファイルを設定(beanで拡張する)
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    //認証のルートがトークン発行とトークンチェックで別れたので、複数プロバイダをセットするための改修
    @Autowired
    public void configureProvider(AuthenticationManagerBuilder authSetting, SecuritySqlExt authSqlExt, SecurityTokenExt authTokenExt) throws Exception{
        //プロバイダ設定を追加(トークンチェック用)
        var tokenCheckProvider = new PreAuthenticatedAuthenticationProvider();
        //トークンチェックサービス用のプロバイダをここでセット
        tokenCheckProvider.setPreAuthenticatedUserDetailsService(authTokenExt);
        tokenCheckProvider.setUserDetailsChecker(new AccountStatusUserDetailsChecker());
        authSetting.authenticationProvider(tokenCheckProvider);

        //初回ログインのトークン発行用のプロバイダをここでセット
        var daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService(authSqlExt);
        daoAuthProvider.setPasswordEncoder(new BCryptPasswordEncoder(8));
        authSetting.authenticationProvider(daoAuthProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/").permitAll()
            .requestMatchers("/account/**").permitAll()
            .anyRequest().authenticated()
        );
        //ライブラリのデフォルト設定をしているクラスを取得
        var authManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
        //カスタムしたフィルターを読み込ませる
        //トークン発行用フィルター
        http.addFilter(new AccountAuthenticationFilter(authManager));
        //トークン認証用フィルター
        http.addFilter(new TokenAuthenticationFilter(authManager));
        //セッションを使用しないでcsrfエラーも起きないように設定
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(8);
    }

}
