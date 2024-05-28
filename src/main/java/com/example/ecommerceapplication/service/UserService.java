package com.example.ecommerceapplication.service;

//import com.web.ecommerceweb.configuration.UserInfoDetails;

import com.example.ecommerceapplication.model.Orders;
import com.example.ecommerceapplication.model.User;
import com.example.ecommerceapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
//import static org.springframework.security.core.userdetails.User.builder;


@Service
public class UserService {
//    public class UserService implements UserDetailsService {

        @Autowired
    private UserRepository userRepo;

    private String getRoles(User user) {
        // Check if the user has the "ADMIN" role
        if ("ADMIN".equals(user.getRole())) {
            return "ADMIN";
        }

        // If not admin, return "USER" as default role
        return "USER";
    }


@Transactional
public void saveUser(User user) {
    try {
        if (user != null && user.getPassword() != null && !user.getPassword().isEmpty()) {
            System.out.println("Saving user: " + user.getEmail());
            userRepo.save(user);
            System.out.println("User saved successfully");
        } else {
            System.out.println("Invalid user data");
        }
    } catch (Exception e) {
        System.out.println("Error saving user:");
        e.printStackTrace(); // Print stack trace to console
    }
}

    public List<User>allUsers(){
        return userRepo.findAll();
    }
    public User findUser(int id){
        return userRepo.findById(id).orElse(null);
    }
    public boolean deleteUser(int id){
        User user=userRepo.findById(id).orElse(null);
        if(user!=null){
            userRepo.delete(user);
            return true;
        }
        return false;
    }
    public String updateUser(int id,String name,String password,String email){
        User users=userRepo.findById(id).orElse(null);
        if (users!=null){
            users.setUsername(name);
            users.setEmail(email);
//            String hashedPassword=encoder.encode(password);
//            users.setPassword(hashedPassword);
            return "Users updated";

        }
        return "user not updated";
    }
    public User findUserbyemail(String email){
        return userRepo.findByEmail(email).orElse(null);
    }

    public User getUserById(long id) {
        return (User) UserRepository.findById(id);
    }


}