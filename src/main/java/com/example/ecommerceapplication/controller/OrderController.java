package com.example.ecommerceapplication.controller;

import com.example.ecommerceapplication.dto.GlobalData;
import com.example.ecommerceapplication.dto.UserDTO;
import com.example.ecommerceapplication.model.Orders;
import com.example.ecommerceapplication.model.Product;
import com.example.ecommerceapplication.model.User;
import com.example.ecommerceapplication.service.OrderService;
import com.example.ecommerceapplication.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    //create order
    // New endpoints for ordering page
    @GetMapping("/order")
    public String showOrderForm(Model model) {
        model.addAttribute("order", new Orders());
        model.addAttribute("cart", GlobalData.cart);
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        return "orderForm";
    }

    @PostMapping("/order")
    public String placeOrder(Model model, @ModelAttribute Orders order, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Ensure the cart is not empty
        if (GlobalData.cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty.");
            return "redirect:/orders/order";
        }

        // Get the authenticated user's information from the session
        User authenticatedUser = (User) request.getSession().getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            // Handle the case where the user is not authenticated
            redirectAttributes.addFlashAttribute("error", "You must be logged in to place an order.");
            return "redirect:/admins/login";
        }

        // Set the customer information in the order
        order.setCustomer(authenticatedUser);
        GlobalData.orders.setAddress(order.getAddress());
        GlobalData.orders.setPhoneNumber(order.getPhoneNumber());

        // Set the products and total price
        order.setProducts(GlobalData.cart);
        order.setTotalPrice(GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());

        // Create the order
        Orders savedOrder = orderService.createOrder(order);

        if (savedOrder != null) {
            //GlobalData.cart.clear();  // Clear the cart after order is placed
            redirectAttributes.addFlashAttribute("message", "Order placed successfully!");
            //return "redirect:/orders/orderHistory/" + authenticatedUser.getId();
            return "redirect:/products/shop/confirmation";
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to place order.");
            return "redirect:/orders/order";
        }
    }




    @GetMapping("/orderHistory/{email}")
    public String viewOrderHistory(Model model, @PathVariable Long userId, @PathVariable String email) {
        //User user = userService.getUserById(userId); // Implement userService to get user by ID
        User user = userService.findUserbyemail(email);
        if (user != null) {
            List<Orders> orderHistory = orderService.getOrdersByCustomer(user);// Implement orderService to get order history for user

            model.addAttribute("user", user);
            model.addAttribute("orderHistory", orderHistory);
            return "orderHistory"; // Assuming the view name is "orderHistory.html"
        } else {
            // Handle case where user is not found
            model.addAttribute("error", "User not found.");
            return "error"; // or any other appropriate error page
        }
    }




//    @GetMapping("/ordering")
//    public String checkout(Model model){
//        model.addAttribute("cartCount", GlobalData.cart.size());
//        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
//        return "checkout";
//    }

    // Get all carts
    @GetMapping("/allOrders")
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.ok(orderService.getAllOrders());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    // Get cart by owner
    @GetMapping("/getOrdersByOwner/{userId}")
    public ResponseEntity<?> getCartByOwner(@PathVariable long userId) {
        try {
            User user = userService.getUserById(userId);
            if (user != null) {
                List<Orders> myOrders = orderService.getOrdersByCustomer(user);
                if (myOrders != null) {
                    return ResponseEntity.ok(myOrders);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No Order Found!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error !"));
        }
    }

    @PutMapping("/cancelOrder/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable long orderId) {
        try {
            Orders order = orderService.getOrderById(orderId);
            if (order != null) {
                //order.setStatus(OrderStatus.CANCELED);
                Orders updatedOrder = orderService.updateOrder(order.getId(),order);
                if (updatedOrder != null) {
                    return ResponseEntity.ok(updatedOrder);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Order Not Found!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error !"));
        }
    }

    @PutMapping("/deliverOrder/{orderId}")
    public ResponseEntity<?> deliverOrder(@PathVariable long orderId) {
        try {
            Orders order = orderService.getOrderById(orderId);
            if (order != null) {
                //order.setStatus(OrderStatus.DELIVERED);
                Orders updatedOrder = orderService.updateOrder(order.getId(),order);
                if (updatedOrder != null) {
                    return ResponseEntity.ok(updatedOrder);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Order Not Found!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error !"));
        }
    }
    // Get cart by ID
    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<?> getCartById(@PathVariable Long orderId) {
        try {
            Orders order = orderService.getOrderById(orderId);
            if (order!=null) {
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No Order Found!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error !"));
        }
    }


    @DeleteMapping("deleteOrder/{orderId}")
    public ResponseEntity<?> deleteCart(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Order with ID " + orderId + " has been deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }}
