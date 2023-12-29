//package LonaShop.controller.user;
//
//import LonaShop.model.User;
//import LonaShop.model.UserDto;
//import LonaShop.service.UserService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class AuthController {
//
//    @Autowired
//    private UserService userService;
//
//    // handler method to handle login request
//    @GetMapping("/login")
//    public String login() {
//        return "user/auth/login";
//    }
//
//    @GetMapping("/accessDenied")
//    public String accessDeniedHandler(Model model) {
//        return "user/auth/forbidden";
//    }
//
//    // handler method to handle user registration form request
//    @GetMapping("/register")
//    public String showRegistrationForm(Model model) {
//        // create model object to store form data
//        UserDto user = new UserDto();
//        model.addAttribute("user", user);
//        return "user/auth/register";
//    }
//
//    // handler method to handle user registration form submit request
//    @PostMapping("/register/save")
//    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
//        User existingUser = userService.findUserByEmail(userDto.getEmail());
//
//        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
//            result.rejectValue("email", null, "There is already an account registered with the same email");
//        }
//
//        if (result.hasErrors()) {
//            model.addAttribute("user", userDto);
//            return "user/auth/register";
//        }
//
//        userService.saveUser(userDto);
//        return "redirect:/register?success";
//    }
//
//}
