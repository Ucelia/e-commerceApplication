package com.example.ecommerceapplication.controller;

import com.example.ecommerceapplication.dto.GlobalData;
import com.example.ecommerceapplication.model.User;
import com.example.ecommerceapplication.repository.UserRepository;
import com.example.ecommerceapplication.service.EmailService;
import com.example.ecommerceapplication.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;


    @GetMapping("/login")
    public String showloginPage() {
        GlobalData.cart.clear();
        return "login";
    }


    @GetMapping("/register")
    public String showSignupPage(Model model) {
        model.addAttribute("User", new User());
        return "register";
    }

@PostMapping("/register")
public String signup(@ModelAttribute User user) {
    // Check if the user already exists
    Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
    if (existingUser.isPresent()) {
        return "redirect:/users/login?error=user-exists";
    } else {
        // Set the default role for the user
        user.setRole("USER");

        // Save the user to the database
        userService.saveUser(user);

        return "redirect:/users/login?registrationSuccess=true";


    }
}



    @PostMapping("/login")
    public String login(@ModelAttribute User loginUser, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("Attempting login for user with email: " + loginUser.getEmail());

        // Find user by email
        User user = userService.findUserbyemail(loginUser.getEmail());

        if (user != null) {
            System.out.println("User found in the database.");
            // Log the user's credentials
            System.out.println("User credentials - Email: " + loginUser.getEmail() + ", Password: " + loginUser.getPassword());

            // Check if the entered password matches the stored password
            if (loginUser.getPassword().equals(user.getPassword())) {
                System.out.println("Login successful.");

                // Store user in the session
                request.getSession().setAttribute("authenticatedUser", user);
                System.out.println(user.getEmail());


                // Redirect based on user role
                if (user.getRole().equals("ADMIN")) {
                    redirectAttributes.addFlashAttribute("message", "Welcome Admin!");
                    return "redirect:/admins/admin"; // Assuming admin dashboard URL
                } else {
                    redirectAttributes.addFlashAttribute("message", "Welcome User!");
                    return "redirect:/products/shop"; // Default redirection for regular users
                }
            } else {
                System.out.println("Invalid password");
                // Password incorrect, add error message
                model.addAttribute("error", "Invalid username or password.");
                return "login";
            }
        } else {
            System.out.println("User not found in the database.");
            model.addAttribute("error", "User not found. Please create your account.");
            return "login";
        }
    }


}

