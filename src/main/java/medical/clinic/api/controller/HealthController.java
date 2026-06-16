package medical.clinic.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthController {
    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui/index.html";
    }
}
