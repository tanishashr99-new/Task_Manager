package com.example.Task_Manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.Task_Manager.model.Task;
import com.example.Task_Manager.model.User;
import com.example.Task_Manager.repository.TaskRepository;
import com.example.Task_Manager.repository.UserRepository;
import com.example.Task_Manager.TokenUtil;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TokenUtil tokenUtil;

    @PostMapping("/add")
    public String createTask(
            @RequestBody Task task,
            @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);

        if (username == null) {
            return "Unauthorized";
        }

        task.setUsername(username);
        taskRepo.save(task);

        return "Task Added Successfully";
    }

    @GetMapping("/all")
    public Object getTasks(
            @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);

        if (username == null) {
            return "Unauthorized";
        }

        return taskRepo.findByUsername(username);
    }

    @GetMapping("/admin")
    public Object adminTasks(
            @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);

        if (username == null) {
            return "Unauthorized";
        }

        User user = userRepo.findByUsername(username);

        if (user == null) {
            return "Unauthorized";
        }

        if (!user.getRole().equals("ADMIN")) {
            return "Access Denied";
        }

        return taskRepo.findAll();
    }
}