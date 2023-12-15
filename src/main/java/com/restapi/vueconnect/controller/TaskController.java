package com.restapi.vueconnect.controller;

import java.util.Optional;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.vueconnect.model.entity.Task;
import com.restapi.vueconnect.model.repository.TaskRepository;

@RestController
public class TaskController {
    
    @Autowired
    private TaskRepository taskRepository;


    @GetMapping("/task/list")
    @CrossOrigin
    public List<Task> getList() throws Exception{
        System.out.println(taskRepository.findAll());
        return taskRepository.findAll();
    }


    @GetMapping("/task/detail/{id}")
    @CrossOrigin
    public Task getdetail(@PathVariable long id) throws Exception{
        Optional<Task> detailData = taskRepository.findById(id);

        if(detailData.isPresent()) return detailData.get();

        return null;
    }
    //登録API
    @PostMapping("/task/register")
    @CrossOrigin
    public Task register(@RequestBody Task task) throws Exception{
        return taskRepository.save(task);
    }
    
    //更新API
    @PostMapping("/task/update/{id}")
    @CrossOrigin
    public Task update(@PathVariable long id, @RequestBody Task task) throws Exception{
        task.setId(id);
        return taskRepository.save(task);
    }

    //削除API
    @DeleteMapping("/task/delete/{id}")
    @CrossOrigin
    public String deleteData(@PathVariable long id) throws Exception{
        Optional <Task> target = taskRepository.findById(id);
        if(target.isPresent()) taskRepository.deleteById(id); 
        return "delete successfully";
    } 

}
