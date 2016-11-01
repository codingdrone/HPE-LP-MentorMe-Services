package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The User repository.
 */
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    /**
     * This method is used to get the user by provider id and provider user id.
     *
     * @param providerId the provider id
     * @param providerUserId the provider user id
     * @return the match user
     * @throws IllegalArgumentException: if parameters are null or not valid
     * @throws MentorMeException : if any other error occurred during operation
     */
    User findByProviderIdAndProviderUserId(String providerId, String providerUserId);
}

