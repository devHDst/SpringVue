package com.restapi.vueconnect.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.restapi.vueconnect.model.entity.Account;

@EnableJpaRepositories
public interface AccountRepository extends JpaRepository<Account, String>{
    Optional<Account> findByUsername(String username);
    Optional<Account> findByUsernameAndPassword(String username, String password);
}
