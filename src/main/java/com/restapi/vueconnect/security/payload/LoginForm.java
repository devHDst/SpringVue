package com.restapi.vueconnect.security.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    private String Username;
    private String Password;
}
