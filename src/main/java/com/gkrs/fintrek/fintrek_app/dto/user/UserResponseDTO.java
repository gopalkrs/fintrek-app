package com.gkrs.fintrek.fintrek_app.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

}
