package org.dilip.first.pos_backend.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    @NotBlank
    @Pattern(
            regexp = "^[a-zA-Z][a-zA-Z0-9]*@[a-zA-Z]+$",
            message = "Email must start with a letter, may contain numbers before @, and contain only letters after @"
    )
    private String email;

    @NotBlank
    private String password;
}
