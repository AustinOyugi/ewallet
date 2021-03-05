package com.doorstep.springproject.payloads.userdata;

import com.doorstep.springproject.models.userdata.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSummary {

    private String userId;

    private String userName;

    private String fullName;

    private String emailAddress;

    private String pictureUrl;

    private String mobileNumber;

    private String country;

    private short accountActivated;

    @JsonIgnore
    private Set<Role> roles = new HashSet<>();
}
