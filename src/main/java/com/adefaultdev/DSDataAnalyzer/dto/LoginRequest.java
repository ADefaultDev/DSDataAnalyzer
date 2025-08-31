package com.adefaultdev.DSDataAnalyzer.dto;

import lombok.Data;

/**
 * Data Transfer Object for user login requests.
 * Contains credentials for authentication.
 *
 * @author ADefaultDev
 * @since 1.0.0
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}