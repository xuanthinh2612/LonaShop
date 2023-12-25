package LonaShop.controller.admin;

import LonaShop.model.Category;
import LonaShop.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin/category")
public class CategoryManageController extends AdminBaseController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/index")
    public String index(Model model) {
        List<Category> categoryList = categoryService.findList();
        model.addAttribute("categoryList", categoryList);
        return "admin/category/index";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "admin/category/new";
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/category/new";
        }

        categoryService.save(category);

        return index(model);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "admin/category/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("category") Category category, Model model){
        categoryService.save(category);
        return "redirect:/admin/category/index";
    }
}
