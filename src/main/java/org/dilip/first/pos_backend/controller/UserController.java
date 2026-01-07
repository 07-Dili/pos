package org.dilip.first.pos_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.dilip.first.pos_backend.dto.UserDto;
import org.dilip.first.pos_backend.model.data.UserData;
import org.dilip.first.pos_backend.model.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

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
    public UserData login(@Valid @RequestBody UserForm form, HttpServletRequest request) {

        UserData user = userDto.login(form);
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole());
        session.setAttribute("lastAccess", Instant.now());
        return user;
    }


    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}
