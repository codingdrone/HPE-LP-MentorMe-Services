package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The ForgotPassword repository.
 */
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Long> {
    /**
     * This method is used to get the ForgotPassword by token.
     * 
     * Parameters:
     * - token: String
     * 
     * Returns:
     * the ForgotPassword
     * @param token 
     * @return 
     */
    ForgotPassword findByToken(String token);

    long countByUserId(long userId);
}

