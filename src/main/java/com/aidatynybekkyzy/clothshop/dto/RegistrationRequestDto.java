package com.aidatynybekkyzy.clothshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDto {
    private String firstname;
    private String lastname;
    private String email;

    @Size(min=4, max=12)
    private String password;
    @Size (min=4, max=12)
    private String confirmPassword;
    @NotNull
    @Size(max = 20)
    private String phone;

}
