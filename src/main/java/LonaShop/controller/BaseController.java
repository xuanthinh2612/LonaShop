package LonaShop.controller;

import LonaShop.model.Category;
import LonaShop.model.User;
import LonaShop.model.UserDto;
import LonaShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public class BaseController {

    @Autowired
    private UserService userService;

    @ModelAttribute("currentUser")
    private User getCurrentUserDto() {
        return getCurrentLoggedInUser();
    }

    protected User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();
            user = userService.findUserByEmailOrPhoneNumber(currentUserEmail);
        }

        return user;
    }

    protected UserDto getCurrentLoggedInUserDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();
            userDto = userService.findUserDtoByEmail(currentUserEmail);
        }
        return userDto;
    }

}
