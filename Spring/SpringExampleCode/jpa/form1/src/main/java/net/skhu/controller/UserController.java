package net.skhu.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.skhu.model.UserRegistrationModel;
import net.skhu.service.DepartmentService;
import net.skhu.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired UserService userService;
    @Autowired DepartmentService departmentService;

    @RequestMapping(value="register", method=RequestMethod.GET)
    public String register(Model model, UserRegistrationModel userModel) {
        model.addAttribute("departments", departmentService.findAll());
        return "user/register";
    }

    @RequestMapping(value="register", method=RequestMethod.POST)
    public String register(@Valid UserRegistrationModel userModel, BindingResult bindingResult, Model model) {
        if (userService.hasErrors(userModel, bindingResult)) {
            model.addAttribute("departments", departmentService.findAll());
            return "user/register";
        }
        userService.save(userModel);
        return "redirect:registerSuccess";
    }

    @RequestMapping("registerSuccess")
    public String registerSuccess() {
        return "user/registerSuccess";
    }

}
