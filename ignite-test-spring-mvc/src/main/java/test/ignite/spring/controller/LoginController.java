package test.ignite.spring.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

/**
 * <p>This application demonstrates usage of Apache Ignite for session distribution.
 * Application has been developed using <i>Spring MVC</i> and authentication has been implemented
 * using <i>spring security</i> framework.</p>
 * <p>
 * <p>This class is used to redirect login requests. Login requests are coming
 * through spring security filter chain.</p>
 *
 * @author Yasitha Thilakaratne
 * @since v-1.0.0
 */
@Controller
public class LoginController {

    @RequestMapping(value = {"login", "/"})
    public String login() {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!(auth instanceof AnonymousAuthenticationToken)) {
                return "home";
            }
            return "login";
        } catch (Exception e) {
            return "redirect:/error/500";
        }
    }

    @RequestMapping(value = "/home")
    public String home() {
        return "home";
    }
}
