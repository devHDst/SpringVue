package com.restapi.vueconnect.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.restapi.vueconnect.model.entity.Task;
//springではエンティティとDBを接続するオブジェクトは異なる(jpaドライバを継承したインターフェイスを接続してcontrollerで用いるのが一般)
@EnableJpaRepositories
public interface TaskRepository extends JpaRepository<Task, Long>{
    
}
