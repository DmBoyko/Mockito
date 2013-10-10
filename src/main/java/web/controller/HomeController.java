package web.controller;

import db.DBSystemException;
import db.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.UserService;
import web.validator.UserForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class HomeController {
    private static final Log logger = LogFactory.getLog(HomeController.class);

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showLoginPage() {
        return "index";
    }

    @RequestMapping(value = "index")
    public String showIndexPage() {
        return "index";
    }

    @RequestMapping(value = "admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin";
    }

    @RequestMapping(value = "hiUser")
    public String userPage() {
        return "HiUserPage";
    }

    @RequestMapping(value = "showEdit")
    public String showEditPage(@RequestParam String type, HttpSession session,
                               @RequestParam String login, Model model) {

        if (type.equals("edit")) {
            UserForm userForm = new UserForm();
            userForm.setUser(userService.findByLogin(login));
            model.addAttribute("newUser", userForm);
            session.setAttribute("newUser", userForm);
            session.setAttribute("type", type);
            return "edit";
        }

        model.addAttribute("newUser", new UserForm());
        session.setAttribute("newUser", new UserForm());
        session.setAttribute("type", type);
        return "edit";
    }

    @RequestMapping(value = "delete")
    public String deleteUser(@RequestParam String login) {
        User user = userService.findByLogin(login);
        try {
            userService.remove(user);
        } catch (DBSystemException e) {
            logger.error("Can't remove user!!", e);
        }
        return "redirect: admin";
    }

    @RequestMapping(value = "logout")
    public String logout(HttpSession session, Model model,
                         HttpServletRequest request) throws IOException {
        boolean error = false;
        try {
            logger.debug("getAttribute(\"invalidCaptcha\") from session ");
            error = (Boolean) session.getAttribute("invalidCaptcha");
            logger.debug("invalidate session!");
            request.getSession().invalidate();
            logger.debug("invalidCaptcha =  " + error);
            model.addAttribute("error", error);
            logger.debug("add errorMsg on model!");
            model.addAttribute("errorMsg", "Captcha invalid!");
            session.removeAttribute("invalidCaptcha");
            return "index";
        } catch (Exception e) {
            logger.error("error!!", e);
            return "index";
        }
    }


    @RequestMapping(value = "loginfailed")
    public String loginError(HttpSession session) {
        logger.trace("login failed!");
        session.setAttribute("error", "true");
        return "index";
    }

    @RequestMapping(value = "login")
    public String login(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        logger.trace("check  authentication!");
        String login = authentication.getName();
        User user = userService.findByLogin(login);
        logger.trace("remove Attributes!!");
        session.removeAttribute("errorMsg");
        session.setAttribute("user", user);
        if (user.getRole().getName().equals("ROLE_ADMIN")) {
            logger.trace("redirect to admin!!");
            model.addAttribute("users", userService.findAll());
            return "admin";
        }
        logger.trace("redirect to hiUser!!");
        session.removeAttribute("invalidCaptcha");
        return "HiUserPage";
    }

}
