package thevaultweb.the_vault_web;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.InvalidParameterException;

@RestController
@RequestMapping("/")
public class LoginPage {

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("html/login.html");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password) {
        String userSessionKey = "";

        try {
            userSessionKey = ContextWebClass.safe.login(username, password);

        } catch (InvalidParameterException e) {
            // Toast.makeText(e.getMessage());
            userSessionKey = "error";
        }

        return String.format("Login: username: %s - password: %s\nuser session key: %s", username, password, userSessionKey);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public String register(@RequestParam(value = "email") String email,
                           @RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password) {

        ContextWebClass.safe.register(username, password);
        String userSessionKey = ContextWebClass.safe.login(username, password);

        return String.format("Register: username: %s - password: %s - email: %s\nuser session key: %s", username, password, email, userSessionKey);
    }

}
