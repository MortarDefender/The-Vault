package vault.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
@RestController
public class TheVaultWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheVaultWebApplication.class, args);
    }

    @GetMapping("/hello")
    public String start(@RequestParam(value = "name", defaultValue = "world") String name) {
        return String.format("Hello %s", name);
    }
}