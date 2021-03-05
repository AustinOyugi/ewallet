package com.doorstep.springproject.payloads.system;

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
public class ApiResponse {
    private Boolean success;
    private String message;

}