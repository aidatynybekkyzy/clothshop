package com.aidatynybekkyzy.clothshop.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @NotNull
    private Long id;
    @NotNull
    private String username;
    @NotNull
    @Size(min = 4, max = 50)
    private String firstName;
    @NotNull
    @Size(min = 4, max = 50)
    private String lastName;
    @NotNull
    @Email
    @NotEmpty
    private String email;
    @Size(min = 7, max = 60, message = "{validation.user.passwordSize}")
    private String password;
    @Size(min = 7, max = 60, message = "{validation.user.passwordSize}")
    private String confirmPassword;
    @NotNull
    @Size(max = 20)
    private String phone;
    private Set<OrderDto> orders;

}
