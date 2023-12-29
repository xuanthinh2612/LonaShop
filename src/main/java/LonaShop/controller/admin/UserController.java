package LonaShop.controller.admin;

import LonaShop.model.UserDto;
import LonaShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserController {

//    @Autowired
//    UserService userService;

    @GetMapping("/users")
    public String showUser(Model model) {
//      handler method to handle list of users
//        List<UserDto> users = userService.findAllUsers();
//        model.addAttribute("users", users);

        return "admin/user/users";
    }

    @PostMapping("/disableUser/{id}")
    public String disableUser(@PathVariable String id) {
        return "redirect:/users";
    }
}
