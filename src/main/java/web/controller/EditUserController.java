package web.controller;

import db.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.RoleService;
import service.UserService;
import web.validator.UserForm;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: dboyko
 * Date: 8/12/13
 */
@Controller
@RequestMapping("/edit")
public class EditUserController {
    private static final Log logger = LogFactory.getLog(UserForm.class);

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;
    @Qualifier("roleServiceImpl")
    @Autowired
    private RoleService roleService;

    @RequestMapping(method = RequestMethod.POST)
    public String processEditUser(@ModelAttribute("newUser") @Valid final UserForm newUser,
                                  final BindingResult bindingResult, Model model,
                                  @RequestParam String type) {

        if (bindingResult.hasErrors()) {
            logger.error("not valid user !");
            return "edit";
        }

        if (type.equals("edit")) {
            if (updateUser(newUser, model)) return "edit";
        }

        if (type.equals("add")) {
            if (createUser(newUser, model)) return "edit";
        }
        return "redirect: admin";
    }

    private boolean createUser(UserForm newUser, Model model) {
        User user = newUser.getUser();
        user.setRole(roleService.findByName(newUser.getRole()));
        try {
            userService.create(user);
        } catch (DBSystemException e) {
            logger.error("Can't update user - system error!", e);
            model.addAttribute("errorMsg", "Can't create user - system error!");
            return true;
        } catch (NotUniqueLoginException e) {
            logger.error("Can't update user - not unique user login!", e);
            model.addAttribute("errorMsg", "Can't create user - not unique user login!");
            return true;
        } catch (NotUniqueEmailException e) {
            logger.error("Can't update user - not unique user email", e);
            model.addAttribute("errorMsg", "Can't create user - not unique user emai!");
            return true;
        } catch (NotUniqueRoleNameException e) {
            logger.error("Can't update user - not unique user role name!", e);
            model.addAttribute("errorMsg", "Can't create user - not unique user role name!");
            return true;
        }
        return false;
    }

    private boolean updateUser(UserForm newUser, Model model) {
        User user = newUser.getUser();
        user.setId(userService.findByLogin(user.getLogin()).getId());
        user.setRole(roleService.findByName(newUser.getRole()));
        try {
            userService.update(user);
        } catch (NotUniqueEmailException e) {
            logger.error("Can't update user - not unique email!!", e);
            model.addAttribute("errorMsg", "Can't update user - not unique email!!!");
            return true;
        } catch (DBException e) {
            logger.error("Can't update user - system error!", e);
            model.addAttribute("errorMsg", "Can't update user - system error!");
            return true;
        }
        return false;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String signup() {
        return "redirect: admin";
    }
}
