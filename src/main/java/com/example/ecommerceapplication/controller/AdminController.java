package com.example.ecommerceapplication.controller;

import com.example.ecommerceapplication.dto.GlobalData;
import com.example.ecommerceapplication.model.Category;
import com.example.ecommerceapplication.model.Product;
import com.example.ecommerceapplication.service.CategoryService;
import com.example.ecommerceapplication.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@Controller
@RequestMapping("/admins")
public class AdminController {
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images";
    private final CategoryService categoryService;
    private  final ProductService productService;

    @Autowired
    public AdminController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/admin")
    public String adminDashboard(){
        return "adminDashboard";
    }

    @GetMapping("/admin/categories")
    public String categoriesList(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        return "listCategories";
    }

    @GetMapping("/admin/categories/add")
    public String createCategory(Model model){
        model.addAttribute("category", new Category());
        return "createcategories";
    }

    @PostMapping("/admin/categories/add")
    public String createCat(@ModelAttribute("category") Category category){
        categoryService.addCategory(category);
        return "redirect:/admins/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id){
        categoryService.removeCategoryById(id);
        return "redirect:/admins/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String editCategory(@PathVariable int id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "createcategories";
        } else {
            return "error";
        }
    }


    @GetMapping("/admin/products")
    public String productList(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "listProducts";
    }

    @GetMapping("/admin/products/add")
    public String showProductList(Model model){
        model.addAttribute("productDTO", new Product());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "addProducts";
    }

    @PostMapping("/admin/products/add")
    public String addingProduct(@ModelAttribute("productDTO") Product productDTO, @RequestParam("productImage")MultipartFile file,
                       @RequestParam("imgName")String imgName) throws IOException {

    String imageUUID;
    if(!file.isEmpty()){
        imageUUID = file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
        Files.write(fileNameAndPath, file.getBytes());
    }else{
        imageUUID = imgName;
    }

    Product product = new Product();
    product.setName(productDTO.getName());
    product.setPrice(productDTO.getPrice());
    product.setDescription(productDTO.getDescription());
    product.setImageName(imageUUID);

    Optional<Category> categoryOptional = categoryService.getCategoryById(productDTO.getCategory().getId());
    if (categoryOptional.isPresent()){
        Category category = categoryOptional.get();
        product.setCategory(category);
        productService.addProduct(product);
    } else {
        throw new IOException("Category with ID " + productDTO.getCategory().getId() + " not found");
    }
    return "redirect:/admins/admin/products";
    }

    @GetMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable int id){
        productService.removeProductById(id);
        return "redirect:/admins/admin/products";
    }

    @GetMapping("/admin/products/update/{id}")
    public String updateProd(@PathVariable int id, Model model){
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            Product product = productService.getProductById(id).get();
            Product productDTO = new Product();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setCategory(product.getCategory());

            productDTO.setPrice(product.getPrice());
            productDTO.setImageName(product.getImageName());
            model.addAttribute("categories", categoryService.getAllCategory());
            model.addAttribute("productDTO", productDTO);

            return "redirect:/admins/admin/products/add";
        } else {
            // Handle case where product with the given ID doesn't exist
            return "error"; // Or any other appropriate error page
        }
    }

    @GetMapping("/admin/adviewproduct/{id}")
    public String adViewProduct(Model model, @PathVariable int id){
        model.addAttribute("product", productService.getProductById(id).get());
        model.addAttribute("cartCount", GlobalData.cart.size());
        return "adViewProduct";
    }

}
