package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The ForgotPassword repository.
 */
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Long> {
    /**
     * This method is used to get the ForgotPassword by token.
     * @param token the reset password token
     * @return the forgot password
     */
    ForgotPassword findByToken(String token);

    /**
     * Get count by user id.
     * @param userId the user id.
     * @return the count of forgot password entities by user id.
     */
    long countByUserId(long userId);


    /**
     * Delete all forgot passwords by user id.
     * @param userId the user id.
     */
    void deleteByUserId(long userId);
}

