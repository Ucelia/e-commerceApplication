package com.example.ecommerceapplication.controller;

import com.example.ecommerceapplication.dto.GlobalData;
import com.example.ecommerceapplication.dto.UserDTO;
import com.example.ecommerceapplication.model.Orders;
import com.example.ecommerceapplication.model.Product;
import com.example.ecommerceapplication.model.User;
import com.example.ecommerceapplication.service.CategoryService;
import com.example.ecommerceapplication.service.EmailService;
import com.example.ecommerceapplication.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;


    @GetMapping("/")
    public String showLandingPage(Model model){
            return "index";
        }

    @GetMapping("/shop")
       public String shop(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("cartCount", GlobalData.cart.size());
          return "shop";
}
    @GetMapping("/shop/category/{id}")
    public String shopByCategory(Model model, @PathVariable int id){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("products", productService.getAllProductsByCategoryId(id));
        return "shop";
    }
    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(Model model, @PathVariable int id){
        model.addAttribute("product", productService.getProductById(id).get());
        model.addAttribute("cartCount", GlobalData.cart.size());
        return "productDetails";
    }


    // Endpoint for displaying the confirmation page
    @GetMapping("/shop/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        model.addAttribute("cart", GlobalData.cart);
        //model.addAttribute("username", GlobalData.users.getUsername());
        model.addAttribute("address", GlobalData.orders.getAddress());
        model.addAttribute("phoneNumber", GlobalData.orders.getPhoneNumber());
        GlobalData.cart.clear();
        return "orderConfirmation";
    }

    @GetMapping("/shop/checkout")
    public String checkout(Model model){
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        return "checkout";
    }


}
