package org.dilip.first.pos_backend.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    private String password;
}
