package com.livingprogress.mentorme.services;

import com.livingprogress.mentorme.entities.ForgotPassword;
import com.livingprogress.mentorme.entities.NewPassword;
import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.entities.UserSearchCriteria;
import com.livingprogress.mentorme.exceptions.AccessDeniedException;
import com.livingprogress.mentorme.exceptions.EntityNotFoundException;
import com.livingprogress.mentorme.exceptions.MentorMeException;

/**
 * The user service. Extends generic service interface.Implementation should be effectively thread-safe.
 */
public interface UserService extends GenericService<User, UserSearchCriteria> {
    /**
     * This method is used to get the user by provider id and provider user id.
     *
     * @param providerId the provider id
     * @param providerUserId the provider user id
     * @return the match user
     * @throws IllegalArgumentException: if parameters are null or not valid
     * @throws MentorMeException: if any other error occurred during operation
     */
    User findByProviderIdAndProviderUserId(String providerId, String providerUserId) throws MentorMeException;

    /**
     * This method is used to create the forgot password entity for the given user.
     *
     * @param userId the user id.
     * @return the created forgot password entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AccessDeniedException if does not allow to perform action
     * @throws MentorMeException if any other error occurred during operation
     */
    ForgotPassword forgotPassword(long userId) throws MentorMeException;

    /**
     * This method is used to update the forgot password entity for the given token.
     *
     * @param newPassword the newPassword request.
     * @return true if update the password successfully otherwise false
     * @throws IllegalArgumentException if newPassword is null or invalid
     * @throws MentorMeException if any other error occurred during operation
     */
    boolean updatePassword(NewPassword newPassword) throws MentorMeException;
}

