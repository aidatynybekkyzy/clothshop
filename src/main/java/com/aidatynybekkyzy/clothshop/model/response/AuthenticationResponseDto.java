package com.aidatynybekkyzy.clothshop.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class    AuthenticationResponseDto {
    private String accessToken;
    private String email;
}
