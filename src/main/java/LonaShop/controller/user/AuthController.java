package LonaShop.controller.user;

import LonaShop.model.User;
import LonaShop.model.UserDto;
import LonaShop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class AuthController extends UserBaseController {

    @Autowired
    private UserService userService;

    // handler method to handle login request
    @GetMapping("/login")
    public String login(Model model, RedirectAttributes attributes) {
        if (isLoggedIn()) {
            attributes.addFlashAttribute("msg", "Bạn đang login");
            return "redirect:/trang-chu";
        }
        return "user/auth/login";
    }

    @GetMapping("/accessDenied")
    public String accessDeniedHandler(Model model) {
        return "user/auth/forbidden";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model, RedirectAttributes attributes) {
        if (isLoggedIn()) {
            attributes.addFlashAttribute("warningMsg", "Bạn đang login, vui lòng đăng xuất trước khi tạo tài khoản mới");
            return "redirect:/trang-chu";
        }

        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "user/auth/register";
    }

    @GetMapping("/register-email")
    public String showRegistrationForm(@RequestParam("email") String email, Model model, RedirectAttributes attributes) {
        if (isLoggedIn()) {
            attributes.addFlashAttribute("warningMsg", "Bạn đang login, vui lòng đăng xuất trước khi tạo tài khoản mới");
            return "redirect:/trang-chu";
        }

        User userExist = userService.findUserByEmail(email);

        if (!ObjectUtils.isEmpty(userExist)) {
            attributes.addFlashAttribute("warningMsg", "Email đã đăng ký! Vui lòng login!");
            return "redirect:/login";
        }

        // create model object to store form data
        UserDto user = new UserDto();
        user.setEmail(email);
        model.addAttribute("user", user);
        return "user/auth/register";
    }

    // handler method to handle user registration form submit request
    @PostMapping(value = "register", params = "save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model,
                               RedirectAttributes attributes) {
        if (isLoggedIn()) {
            attributes.addFlashAttribute("error", "Bạn đang login, vui lòng đăng xuất trước khi tạo tài khoản mới");
            return "redirect:/trang-chu";
        }

        User userByEmail = userService.findUserByEmail(userDto.getEmail());
        if (!ObjectUtils.isEmpty(userByEmail)) {
            result.rejectValue("email", null, "Email của bạn đã được đăng ký bới một tài khoản khác, Xin vui lòng kiểm tra lại.");
        }

        User userByPhoneNumber = userService.findUserByPhoneNumber(userDto.getPhoneNumber());
        if (!ObjectUtils.isEmpty(userByPhoneNumber)) {
            result.rejectValue("phoneNumber", null, "Số điện thoại của bạn đã được đăng ký bới một tài khoản khác, Xin vui lòng kiểm tra lại.");
        }

        String password = userDto.getPassword();
        String confirmPassword = userDto.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            result.rejectValue("confirmPassword", null, "Nhập lại mật khẩu chưa đúng.");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "user/auth/register";
        }

        userService.saveUser(userDto);

        attributes.addFlashAttribute("msg", "Bạn đã đăng ký tài khoản thành công, vui lòng đăng nhập hệ thống!");
        return "redirect:/login";
    }

    @PostMapping(value = "register", params = "cancel")
    public String RegisterCancel(Model model) {
        return "redirect:/trang-chu";
    }

    private boolean isLoggedIn() {
        UserDto userDto = getCurrentLoggedInUserDto();
        return !ObjectUtils.isEmpty(userDto);
    }

}
