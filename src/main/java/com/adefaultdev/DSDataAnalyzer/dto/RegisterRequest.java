package com.adefaultdev.DSDataAnalyzer.dto;

import lombok.Data;

/**
 * Data Transfer Object for user register requests.
 * Contains credentials for registration.
 *
 * @author ADefaultDev
 * @since 1.0.0
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
}
