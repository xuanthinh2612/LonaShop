package LonaShop.exception;

import LonaShop.model.Category;
import LonaShop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    CategoryService categoryService;
    @ModelAttribute("categoryList" )
    private List<Category> initCategoryList() {
        return categoryService.findList();
    }

    @ExceptionHandler(NumberFormatException.class)
    public String handleNumberFormatException(NumberFormatException ex, Model model) {
        model.addAttribute("error", "Invalid ID format");
        return "shared/errorPage"; // return the name of your custom error page
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNumberFormatException(NoResourceFoundException ex, Model model) {
        model.addAttribute("error", "Không Tìm Thấy Đường Dẫn");
        return "shared/errorPage"; // return the name of your custom error page
    }
}
