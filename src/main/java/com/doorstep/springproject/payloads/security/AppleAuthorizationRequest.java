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
@NoArgsConstructor
@AllArgsConstructor
public class AppleAuthorizationRequest {

    private String client_id;

    private String client_secret;

    private String code;

    private String grant_type;

    private String refresh_token;

    private String  redirect_uri;
}