package com.doorstep.springproject.payloads.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {
  private String accessToken;
  private String tokenType = "Bearer";
}
