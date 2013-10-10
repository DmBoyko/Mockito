package web.controller;

import db.*;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.RoleService;
import service.UserService;
import web.validator.UserForm;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private static final Log logger = LogFactory.getLog(SignupController.class);
    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;
    @Qualifier("roleServiceImpl")
    @Autowired
    private RoleService roleService;

    @RequestMapping(method = RequestMethod.GET)
    public String signup(ModelMap model) {
        putNewUserForm(model);
        return "signup";
    }

    private void putNewUserForm(ModelMap model) {
        UserForm newUser = new UserForm();
        model.put("newUser", newUser);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSignup(@ModelAttribute("newUser") @Valid final UserForm newUser,
                                final BindingResult result, Model model,
                                @RequestParam("recaptcha_challenge_field") String challenge,
                                @RequestParam("recaptcha_response_field") String response,
                                HttpServletRequest request) {

        checkCaptcha(result, model, challenge, response, request);

        if (result.hasErrors()) {
            logger.debug("result has errors!");
            return "signup";
        }

        if (createUser(newUser, model)) {
            return "signup";
        }

        return "signup-success";
    }

    private void checkCaptcha(BindingResult result, Model model, String challenge,
                              String response, HttpServletRequest request) {
        // Validate the reCAPTCHA
        String remoteAddr = request.getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();

        // Probably don't want to hardcode your private key here but
        // just to get it working is OK...
        String privateKey = "6LftC-YSAAAAAEBDPtTjB-G1A-3f6dFEnYmEYD5V";
        reCaptcha.setPrivateKey(privateKey);

        ReCaptchaResponse reCaptchaResponse =
                reCaptcha.checkAnswer(remoteAddr, challenge, response);

        if (!reCaptchaResponse.isValid()) {
            FieldError fieldError = new FieldError(
                    "comment",
                    "captcha",
                    response,
                    false,
                    new String[]{"errors.badCaptcha"},
                    null,
                    "Please try again.");
            result.addError(fieldError);
            model.addAttribute("errorMsgCap", "Captcha invalid!\n");
        }
    }

    private boolean createUser(UserForm newUser, Model model) {
        User user = newUser.getUser();
        user.setRole(roleService.findByName(newUser.getRole()));
        try {
            userService.create(user);
        } catch (DBSystemException e) {
            logger.error("Can't create user - system error!", e);
            model.addAttribute("errorMsg", "Can't create user - system error!");
            return true;
        } catch (NotUniqueLoginException e) {
            logger.error("Can't create user - not unique user login!", e);
            model.addAttribute("errorMsg", "Can't create user - not unique user login!");
            return true;
        } catch (NotUniqueEmailException e) {
            logger.error("Can't create user - not unique user email", e);
            model.addAttribute("errorMsg", "Can't create user - not unique user emai!");
            return true;
        } catch (NotUniqueRoleNameException e) {
            logger.error("Can't create user - not unique user role name!", e);
            model.addAttribute("errorMsg", "Can't create user - not unique user role name!");
            return true;
        }
        return false;
    }
}
