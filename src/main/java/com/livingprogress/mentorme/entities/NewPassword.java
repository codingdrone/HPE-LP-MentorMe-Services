package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * The new password request.
 */
@Getter
@Setter
public class NewPassword  {
    /**
     * The token.
     */
    private String token;

    /**
     * The new password.
     */
    private String newPassword;
}

