package org.dilip.first.pos_backend.model.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserForm {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}




