package com.example.Task_Manager.controller;


import com.example.Task_Manager.TokenUtil;
import com.example.Task_Manager.model.User;
import com.example.Task_Manager.repository.TaskRepository;
import com.example.Task_Manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private TaskRepository taskRepo;

    @PostMapping("/register")
    public String register(@RequestBody User user){
        repo.save(user);
        return "Registered!!";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        System.out.println("INPUT USERNAME: " + user.getUsername());
        System.out.println("INPUT PASSWORD: " + user.getPassword());

        User dbUser = repo.findByUsername(user.getUsername());

        System.out.println("DB USER: " + dbUser);

        if (dbUser == null) {
            return "User not found";
        }

        if (!dbUser.getPassword().equals(user.getPassword())) {
            return "Invalid password";
        }

        return tokenUtil.generateToken(user.getUsername());
    }
}