package com.restapi.vueconnect.security.core;

import java.util.Arrays;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.restapi.vueconnect.model.entity.Account;

import lombok.Getter;

@Getter
public class SecurityRecordExt extends User{
    //ライブラリで設定されている認証用のクラスを自作のモデルクラスのレコードに変更するための拡張
    private final Account account;

    public SecurityRecordExt(Account account){
        //ライブラリクラスに自作モデルのパラメータを上書き
        super(account.getUsername(),
            account.getPassword(),
            Arrays.asList(account.getRoles().split(",")).stream().map(role -> new SimpleGrantedAuthority((role))).toList());
        
        this.account = account;
    }
}
