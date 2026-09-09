package Photo_Sharing_Platform.controller;

import Photo_Sharing_Platform.model.User;
import Photo_Sharing_Platform.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignupPage(Model model) {

        model.addAttribute("user", new User());

        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(
            @ModelAttribute("user") User user,
            Model model) {

        if (userService.usernameExists(user.getUsername())) {

            model.addAttribute(
                    "error",
                    "Username already exists"
            );

            return "signup";
        }

        if (userService.emailExists(user.getEmail())) {

            model.addAttribute(
                    "error",
                    "Email already exists"
            );

            return "signup";
        }

        userService.registerUser(user);

        return "redirect:/signup-success";
    }

    @GetMapping("/signup-success")
    public String signupSuccess() {

        return "signup-success";
    }
}