package com.adefaultdev.DSDataAnalyzer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer Object for user response.
 * Contains credentials of user.
 *
 * @author ADefaultDev
 * @since 1.0.0
 */
@Data
public class UserResponse {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private String username;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String role;
}