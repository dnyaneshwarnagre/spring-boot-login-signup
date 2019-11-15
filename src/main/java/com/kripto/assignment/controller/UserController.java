package com.kripto.assignment.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.kripto.assignment.data.model.Role;
import com.kripto.assignment.data.model.User;
import com.kripto.assignment.service.interf.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
    

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
        	userService.create(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("profile");

        }
        return modelAndView;
    }
    
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
	public ModelAndView profile(User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User updateUser = userService.findUserByEmail(auth.getName());
		if (updateUser != null) {
			userService.update(user, updateUser);
			modelAndView.addObject("successMessage", "Your profile update successfully");
			modelAndView.setViewName("profile");

		} else {
			modelAndView.setViewName("registration");
		}
		return modelAndView;
	}
    
    @RequestMapping(value="/home", method = RequestMethod.GET)
	public ModelAndView home() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		ModelAndView modelAndView = new ModelAndView();
		for(Role role : user.getRoles()) {
			if(role.getRole().equals("ADMIN")) {
				
				modelAndView.addObject("lists", userService.list());
				modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
				modelAndView.setViewName("dashboard");
			}else if(role.getRole().equals("USER")) {
				
				modelAndView.addObject("user", user);
				modelAndView.setViewName("profile");
			}
		}
		
		return modelAndView;
    }
        
}
