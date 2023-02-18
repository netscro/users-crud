package com.test.userscrud.controller;

import com.test.userscrud.domain.User;
import com.test.userscrud.domain.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController {

    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("/users/add")
    public String userForm(User user) {
        return "add-user";
    }

    @PostMapping("/users/add")
    public String addUser(@Valid User users, BindingResult result) {

        if (result.hasErrors()) {
            return "add-user";
        }

        userRepository.save(users);
        return "redirect:/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUsers(@PathVariable("id") long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new IllegalArgumentException("Invalid user Id: " + id);
        }
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUsers(@PathVariable("id") long id, Model model) {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        } else {
            throw new IllegalArgumentException("Invalid user Id: " + id);
        }
        return "update-user";
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        userRepository.save(user);
        return "redirect:/users";
    }

}
