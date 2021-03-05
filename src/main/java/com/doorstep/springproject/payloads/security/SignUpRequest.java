package com.doorstep.springproject.payloads.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @Size(min = 3, max = 20)
    private String fullName;

    @Size(min =3 ,max = 15)
    @NotNull
    @NotEmpty
    @NotBlank
    private String userName;

    @Email
    @Size(max = 40)
    private String emailAddress;

    @Size(max = 20)
    private String country;

    @Size(max = 15)
    private String mobileNumber;

    @Size(min = 6, max = 20)
    @NotNull
    private String password;
}