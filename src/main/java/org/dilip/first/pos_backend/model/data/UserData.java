package org.dilip.first.pos_backend.model.data;


import lombok.Getter;
import lombok.Setter;
import org.dilip.first.pos_backend.constants.UserRole;

@Getter
@Setter
public class UserData {
    private Long id;
    private String email;
    private UserRole role;
}

