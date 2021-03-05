package com.doorstep.springproject.payloads.userdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Austin Oyugi
 * @since  3/5/2021
 * @email austinoyugi@gmail.com
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePassword {

    private String oldPassword;
    private String newPassword;
}
