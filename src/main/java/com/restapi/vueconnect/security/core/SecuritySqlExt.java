package com.restapi.vueconnect.security.core;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.restapi.vueconnect.model.entity.Account;
import com.restapi.vueconnect.model.repository.AccountRepository;

import lombok.extern.slf4j.Slf4j;

//認証用のテーブルからアカウント情報を取得するためのクラスをDIコンテナから拡張
@Slf4j
@Service
public class SecuritySqlExt implements UserDetailsService{
    private final AccountRepository accountRepository;
    //認証を実行する処理のDB認証の部分をカスタム
    public SecuritySqlExt(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Account account = accountRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("アカウント情報が存在しません"));
        return new SecurityRecordExt(account);
    }

}
