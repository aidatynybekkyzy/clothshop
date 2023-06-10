package com.aidatynybekkyzy.clothshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class UserDto {
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
    @JsonIgnore
    @NotNull
    @Size(min = 7, max = 60, message = "{validation.user.passwordSize}")
    private String password;
    @NotNull
    @Size(max = 20)
    private String phone;
   private Set<OrderDto> orders = new HashSet<>();

}
