package org.dilip.first.pos_backend.controller;

import jakarta.validation.Valid;
import org.dilip.first.pos_backend.dto.UserDto;
import org.dilip.first.pos_backend.model.data.UserData;
import org.dilip.first.pos_backend.model.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserDto userDto;

    @PostMapping("/signup")
    public UserData signup(@Valid @RequestBody UserForm form) {
        return userDto.signup(form);
    }

    @PostMapping("/login")
    public UserData login(@Valid @RequestBody UserForm form) {
        return userDto.login(form);
    }
}
