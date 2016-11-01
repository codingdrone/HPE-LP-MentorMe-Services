package com.livingprogress.mentorme.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livingprogress.mentorme.entities.IdentifiableEntity;
import com.livingprogress.mentorme.entities.InstitutionAffiliationCode;
import com.livingprogress.mentorme.entities.InstitutionUser;
import com.livingprogress.mentorme.entities.InstitutionUserSearchCriteria;
import com.livingprogress.mentorme.entities.Mentee;
import com.livingprogress.mentorme.entities.Mentor;
import com.livingprogress.mentorme.entities.NewPassword;
import com.livingprogress.mentorme.entities.ParentConsent;
import com.livingprogress.mentorme.entities.PersonalInterest;
import com.livingprogress.mentorme.entities.ProfessionalExperienceData;
import com.livingprogress.mentorme.entities.ProfessionalInterest;
import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.entities.WeightedPersonalInterest;
import com.livingprogress.mentorme.entities.WeightedProfessionalInterest;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

/**
 * This class provides help methods used in this application.
 */
@NoArgsConstructor(access = PRIVATE)
public class Helper {
    /**
     * Represents the entrance message.
     */
    private static final String MESSAGE_ENTRANCE = "Entering method %1$s.";

    /**
     * Represents the exit message.
     */
    private static final String MESSAGE_EXIT = "Exiting method %1$s.";

    /**
     * Represents the error message.
     */
    private static final String MESSAGE_ERROR = "Error in method %1$s. Details:";

    /**
     * The object mapper.
     */
    public static final ObjectMapper MAPPER = new Jackson2ObjectMapperBuilder()
            .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).build();

    /**
     * Represents the utf8 encoding name.
     */
    public static final String UTF8 = "UTF-8";

    /**
     * It checks whether a given object is null.
     *
     * @param object the object to be checked
     * @param name the name of the object, used in the exception message
     * @throws IllegalArgumentException the exception thrown when the object is null
     */
    public static void checkNull(Object object, String name) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(String.format("%s should be provided", name));
        }
    }

    /**
     * It checks whether a given string is valid email address.
     *
     * @param str the string to be checked
     * @return true if a given string is valid email address
     */
    public static boolean isEmail(String str) {
        return new EmailValidator().isValid(str, null);
    }

    /**
     * It checks whether a given string is null or empty.
     *
     * @param str the string to be checked
     * @return true if a given string is null or empty
     */
    public static boolean isNullOrEmpty(String str) throws IllegalArgumentException {
        return str == null || str.trim().isEmpty();
    }

    /**
     * It checks whether a given string is null or empty.
     *
     * @param str the string to be checked
     * @param name the name of the string, used in the exception message
     * @throws IllegalArgumentException the exception thrown when the given string is null or empty
     */
    public static void checkNullOrEmpty(String str, String name) throws IllegalArgumentException {
        if (isNullOrEmpty(str)) {
            throw new IllegalArgumentException(String.format("%s should be valid string(not null and not empty)", name));
        }
    }

    /**
     * Check if the value is positive.
     *
     * @param value the value to be checked
     * @param name the name of the value, used in the exception message
     * @throws IllegalArgumentException if the value is not positive
     */
    public static void checkPositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.format("%s should be positive", name));
        }
    }

    /**
     * Check if the value is valid email.
     *
     * @param value the value to be checked
     * @param name the name of the value, used in the exception message
     * @throws IllegalArgumentException if the value is not email
     */
    public static void checkEmail(String value, String name) {
        checkNullOrEmpty(value, name);
        if (!isEmail(value)) {
            throw new IllegalArgumentException(String.format("%s should be valid email address", name));
        }
    }

    /**
     * Check if the configuration state is true.
     *
     * @param state the state
     * @param message the error message if the state is not true
     * @throws ConfigurationException if the state is not true
     */
    public static void checkConfigState(boolean state, String message) {
        if (!state) {
            throw new ConfigurationException(message);
        }
    }

    /**
     * Check if the configuration is null or not.
     *
     * @param object the object
     * @param name the name
     * @throws ConfigurationException if the configuration is null
     */
    public static void checkConfigNotNull(Object object, String name) {
        if (object == null) {
            throw new ConfigurationException(String.format("%s should be provided", name));
        }
    }

    /**
     * Check if the configuration is positive or not.
     *
     * @param value the configuration  value
     * @param name the name
     * @throws ConfigurationException if the configuration value is  not positive
     */
    public static void checkConfigPositive(long value, String name) {
        if (value <= 0) {
            throw new ConfigurationException(String.format("%s should be positive", name));
        }
    }

    /**
     * Logs message with <code>DEBUG</code> level.
     *
     * @param logger the logger.
     * @param message the log message.
     */
    public static void logDebugMessage(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /**
     * Logs for entrance into public methods at <code>DEBUG</code> level.
     *
     * @param logger the logger.
     * @param signature the signature.
     * @param paramNames the names of parameters to log (not Null).
     * @param params the values of parameters to log (not Null).
     */
    public static void logEntrance(Logger logger, String signature, String[] paramNames, Object[] params) {
        if (logger.isDebugEnabled()) {
            String msg = String.format(MESSAGE_ENTRANCE, signature) + toString(paramNames, params);
            logger.debug(msg);
        }
    }

    /**
     * Logs for exit from public methods at <code>DEBUG</code> level.
     *
     * @param logger the logger.
     * @param signature the signature of the method to be logged.
     * @param value the return value to log.
     */
    public static void logExit(Logger logger, String signature, Object value) {
        if (logger.isDebugEnabled()) {
            String msg = String.format(MESSAGE_EXIT, signature);
            if (value != null) {
                msg += " Output parameter : " + toString(value);
            }
            logger.debug(msg);
        }
    }

    /**
     * Logs the given exception and message at <code>ERROR</code> level.
     *
     * @param <T> the exception type.
     * @param logger the logger.
     * @param signature the signature of the method to log.
     * @param ex the exception to log.
     */
    public static <T extends Throwable> void logException(Logger logger, String signature, T ex) {
        StringBuilder sw = new StringBuilder();
        sw.append(String.format(MESSAGE_ERROR, signature)).append(": ").append(ex.getMessage());
        logger.error(sw.toString(), ex);
    }

    /**
     * Converts the parameters to string.
     *
     * @param paramNames the names of parameters.
     * @param params the values of parameters.
     * @return the string
     */
    private static String toString(String[] paramNames, Object[] params) {
        StringBuilder sb = new StringBuilder(" Input parameters: {");
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(paramNames[i]).append(":").append(toString(params[i]));
            }
        }
        sb.append("}.");
        return sb.toString();
    }

    /**
     * Converts the object to string.
     *
     * @param obj the object
     * @return the string representation of the object.
     */
    public static String toString(Object obj) {
        String result;
        try {
            if (obj instanceof HttpServletRequest) {
                result = "Http servlet request";
            } else if (obj instanceof HttpServletResponse) {
                result = "Http servlet response";
            } else if (obj instanceof ModelAndView) {
                result = "Spring model and view object";
            } else if (obj instanceof NewPassword) {
                result = "New Password request";
            } else {
                result = MAPPER.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            result = "The object can not be serialized by Jackson JSON mapper, error: " + e.getMessage();
        }
        return result;
    }

    /**
     * Get password encoder.
     *
     * @return the BC crypt password encoder
     */
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static User encodePassword(User user, boolean isUpdating) {
        Helper.checkNull(user, "user");
        String rawPassword = user.getPassword();
        boolean checkPassword = !isUpdating || rawPassword != null;
        if (checkPassword) {
            Helper.checkNullOrEmpty(rawPassword, "user.password");
            PasswordEncoder encoder = getPasswordEncoder();
            user.setPassword(encoder.encode(rawPassword));
        }
        return user;
    }


    /**
     * Build predicate to match ids in identifiable entity list.
     *
     * @param val the list value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @param <T> the identifiable entity
     * @return the match predicate
     */
    public static <T extends IdentifiableEntity> Predicate buildInPredicate(List<T> val, Predicate pd, Path<?> path, CriteriaBuilder cb) {
        if (val != null && !val.isEmpty()) {
            List<Long> ids = val.stream().map(IdentifiableEntity::getId).collect(Collectors.toList());
            pd = cb.and(pd, path.in(ids));
        }
        return pd;
    }

    /**
     * Build >= predicate.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @param <Y> the comparable entity
     * @return the match predicate
     */
    public static <Y extends Comparable<? super Y>> Predicate buildGreaterThanOrEqualToPredicate(Y val, Predicate pd, Path<? extends Y> path, CriteriaBuilder cb) {
        if (val != null) {
            pd = cb.and(pd, cb.greaterThanOrEqualTo(path, val));
        }
        return pd;
    }

    /**
     * Build <= predicate.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @param <Y> the comparable entity
     * @return the match predicate
     */
    public static <Y extends Comparable<? super Y>> Predicate buildLessThanOrEqualToPredicate(Y val, Predicate pd, Path<? extends Y> path, CriteriaBuilder cb) {
        if (val != null) {
            pd = cb.and(pd, cb.lessThanOrEqualTo(path, val));
        }
        return pd;
    }

    /**
     * Build equal predicate for object value.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildEqualPredicate(Object val, Predicate pd, Path<?> path, CriteriaBuilder cb) {
        if (val != null) {
            pd = cb.and(pd, cb.equal(path, val));
        }
        return pd;
    }

    /**
     * Build equal predicate for string value.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildEqualPredicate(String val, Predicate pd, Path<?> path, CriteriaBuilder cb) {
        if (!isNullOrEmpty(val)) {
            pd = cb.and(pd, cb.equal(path, val));
        }
        return pd;
    }

    /**
     * Build like predicate for string value.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildLikePredicate(String val, Predicate pd, Path<String> path, CriteriaBuilder cb) {
        if (!isNullOrEmpty(val)) {
            pd = cb.and(pd, buildLike(val, path, cb));
        }
        return pd;
    }

    /**
     * Build like predicate for string value.
     *
     * @param val the value
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildLike(String val, Path<String> path, CriteriaBuilder cb) {
        return cb.like(path, "%" + val + "%");
    }

    /**
     * Build name predicate..
     *
     * @param name the name
     * @param root the root
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildNamePredicate(String name, Predicate pd, Root<?> root, CriteriaBuilder cb) {
        if (!isNullOrEmpty(name)) {
            pd = cb.and(pd, cb.or(Helper.buildLike(name, root.get("firstName"), cb), Helper.buildLike(name, root.get("lastName"), cb)));
        }
        return pd;
    }

    /**
     * Build predicate for institution user.
     *
     * @param criteria the institution user criteria
     * @param root the root
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildPredicate(InstitutionUserSearchCriteria criteria, Predicate pd, Root<?> root, CriteriaBuilder cb) {
        pd = Helper.buildEqualPredicate(criteria.getInstitutionId(), pd, root.get("institution").get("id"), cb);
        pd = Helper.buildEqualPredicate(criteria.getStatus(), pd, root.get("status"), cb);
        pd = Helper.buildGreaterThanOrEqualToPredicate(criteria.getMinAveragePerformanceScore(), pd, root.get("averagePerformanceScore"), cb);
        pd = Helper.buildLessThanOrEqualToPredicate(criteria.getMaxAveragePerformanceScore(), pd, root.get("averagePerformanceScore"), cb);
        pd = Helper.buildNamePredicate(criteria.getName(), pd, root, cb);
        pd = Helper.buildInPredicate(criteria.getPersonalInterests(), pd, root.join("personalInterests", JoinType.LEFT).get("personalInterest").get("id"), cb);
        pd = Helper.buildInPredicate(criteria.getProfessionalInterests(), pd, root.join("professionalInterests", JoinType.LEFT).get("professionalInterest").get("id"), cb);
        pd = Helper.buildEqualPredicate(criteria.getAssignedToInstitution(), pd, root.get("assignedToInstitution"), cb);
        return pd;
    }

    /**
     * Check whether entity list is null or empty.
     *
     * @param values the entity list.
     * @return true if value has been updated.
     */
    public static <T extends IdentifiableEntity> boolean isNullOrEmptyList(List<T> values) {
        return values == null || values.isEmpty();
    }

    /**
     * Check whether value has been updated.
     *
     * @param oldValue the old value
     * @param newValue the new value.
     * @return true if value has been updated.
     */
    public static boolean isUpdated(Object oldValue, Object newValue) {
        return (oldValue != null && !oldValue.equals(newValue)) || (newValue != null && !newValue.equals(oldValue));
    }

    /**
     * Check whether both values is null.
     *
     * @param oldValue the old value
     * @param newValue the new value.
     * @return true if both values is null
     */
    public static boolean isBothNull(Object oldValue, Object newValue) {
        return oldValue == null && newValue == null;
    }

    /**
     * Check whether identifiable entity list has been updated.
     *
     * @param oldValues the old values
     * @param newValues the new values.
     * @return true if value has been updated.
     */
    public static <T extends IdentifiableEntity> boolean isUpdated(List<T> oldValues, List<T> newValues) {
        List<Long> oldIds = oldValues == null ? Collections.emptyList() : oldValues.stream().map(IdentifiableEntity::getId).collect(Collectors.toList());
        return newValues == null && !oldIds.isEmpty() || newValues != null && (oldIds.size() != newValues.size() || newValues.stream().anyMatch(a -> !oldIds.contains(a.getId())));
    }

    /**
     * Check whether weighted personal interest entity list has been updated.
     *
     * @param oldValues the old values
     * @param newValues the new values.
     * @return true if value has been updated.
     */
    public static <T extends WeightedPersonalInterest> boolean isUpdatedWeightedPersonalInterests(List<T> oldValues, List<T> newValues) {
        return isUpdated(oldValues, newValues) || oldValues.stream().anyMatch(w -> {
            T match = newValues.stream().filter(n -> n.getId() == w.getId()).findFirst().get();
            return isUpdated(w.getWeight(), match.getWeight()) || isUpdated(w.getPersonalInterest().getId(), match.getPersonalInterest().getId());
        });
    }

    /**
     * Check whether weighted professional interest entity list has been updated.
     *
     * @param oldValues the old values
     * @param newValues the new values.
     * @return true if value has been updated.
     */
    public static <T extends WeightedProfessionalInterest> boolean isUpdatedWeightedProfessionalInterests(List<T> oldValues, List<T> newValues) {
        return isUpdated(oldValues, newValues) || oldValues.stream().anyMatch(w -> {
            T match = newValues.stream().filter(n -> n.getId() == w.getId()).findFirst().get();
            return isUpdated(w.getWeight(), match.getWeight()) || isUpdated(w.getProfessionalInterest().getId(), match.getProfessionalInterest().getId());
        });
    }

    /**
     * Check whether user entity has been updated.
     *
     * @param oldEntity the old entity
     * @param newEntity the new entity.
     * @return true if value has been updated.
     */
    public static <T extends User> boolean isUpdated(T oldEntity, T newEntity) {
        if(isBothNull(oldEntity, newEntity)){
            return false;
        }
        boolean updated = false;
        if (isUpdated(oldEntity.getUsername(), newEntity.getUsername())) {
            updated = true;
            oldEntity.setUsername(newEntity.getUsername());
        }
        if (newEntity.getPassword() != null) {
            updated = true;
            User encodedUser = Helper.encodePassword(newEntity, true);
            oldEntity.setPassword(encodedUser.getPassword());
        }
        if (isUpdated(oldEntity.getFirstName(), newEntity.getFirstName())) {
            updated = true;
            oldEntity.setFirstName(newEntity.getFirstName());
        }
        if (isUpdated(oldEntity.getLastName(), newEntity.getLastName())) {
            updated = true;
            oldEntity.setLastName(newEntity.getLastName());
        }
        if (isUpdated(oldEntity.getRoles(), newEntity.getRoles())) {
            updated = true;
            oldEntity.setRoles(newEntity.getRoles());
        }

        if (isUpdated(oldEntity.getEmail(), newEntity.getEmail())) {
            updated = true;
            oldEntity.setEmail(newEntity.getEmail());
        }
        if (isUpdated(oldEntity.getProfilePicturePath(), newEntity.getProfilePicturePath())) {
            updated = true;
            oldEntity.setProfilePicturePath(newEntity.getProfilePicturePath());
        }
        if (isUpdated(oldEntity.getStatus(), newEntity.getStatus())) {
            updated = true;
            oldEntity.setStatus(newEntity.getStatus());
        }
        return updated;
    }


    /**
     * Check whether institution user entity has been updated.
     *
     * @param oldEntity the old entity
     * @param newEntity the new entity.
     * @return true if value has been updated.
     */
    public static <T extends InstitutionUser> boolean isUpdated(T oldEntity, T newEntity) {
        if(isBothNull(oldEntity, newEntity)){
            return false;
        }
        boolean updated = isUpdated((User) oldEntity, (User) newEntity);
        if (isUpdated(oldEntity.getInstitution(), newEntity.getInstitution())) {
            updated = true;
            oldEntity.setInstitution(newEntity.getInstitution());
        }
        if (isUpdated(oldEntity.isAssignedToInstitution(), newEntity.isAssignedToInstitution())) {
            updated = true;
            oldEntity.setAssignedToInstitution(newEntity.isAssignedToInstitution());
        }
        if (isUpdated(oldEntity.getBirthDate(), newEntity.getBirthDate())) {
            updated = true;
            oldEntity.setBirthDate(newEntity.getBirthDate());
        }
        if (isUpdated(oldEntity.getPhone(), newEntity.getPhone())) {
            updated = true;
            oldEntity.setPhone(newEntity.getPhone());
        }
        if (isUpdated(oldEntity.getSkypeUsername(), newEntity.getSkypeUsername())) {
            updated = true;
            oldEntity.setSkypeUsername(newEntity.getSkypeUsername());
        }

        if (isUpdated(oldEntity.getIntroVideoLink(), newEntity.getIntroVideoLink())) {
            updated = true;
            oldEntity.setIntroVideoLink(newEntity.getIntroVideoLink());
        }
        if (isUpdated(oldEntity.getDescription(), newEntity.getDescription())) {
            updated = true;
            oldEntity.setDescription(newEntity.getDescription());
        }
        if (isUpdatedWeightedPersonalInterests(oldEntity.getPersonalInterests(), newEntity.getPersonalInterests())) {
            updated = true;
            oldEntity.getPersonalInterests().clear();
            if (newEntity.getPersonalInterests() != null) {
                oldEntity.getPersonalInterests().addAll(newEntity.getPersonalInterests());
                oldEntity.getPersonalInterests().forEach(c -> c.setUser(oldEntity));
            }
        }
        if (isUpdatedWeightedProfessionalInterests(oldEntity.getProfessionalInterests(), newEntity.getProfessionalInterests())) {
            updated = true;
            oldEntity.getProfessionalInterests().clear();
            if (newEntity.getProfessionalInterests() != null) {
                oldEntity.getProfessionalInterests().addAll(newEntity.getProfessionalInterests());
                oldEntity.getProfessionalInterests().forEach(c -> c.setUser(oldEntity));
            }
        }
        if (isUpdated(oldEntity.getAveragePerformanceScore(), newEntity.getAveragePerformanceScore())) {
            updated = true;
            oldEntity.setAveragePerformanceScore(newEntity.getAveragePerformanceScore());
        }
        return updated;
    }

    /**
     * Check whether professional experience entity has been updated.
     *
     * @param oldValues the old values
     * @param newValues the new values.
     * @return true if value has been updated.
     */
    public static <T extends ProfessionalExperienceData> boolean isUpdatedProfessionalExperienceDatas(List<T> oldValues, List<T> newValues) {
        return isUpdated(oldValues, newValues) || oldValues.stream().anyMatch(w -> {
            T match = newValues.stream().filter(n -> n.getId() == w.getId()).findFirst().get();
            return isUpdated(w.getPosition(), match.getPosition()) || isUpdated(w.getWorkLocation(), match.getWorkLocation()) || isUpdated(w.getStartDate(), match.getStartDate()) || isUpdated(w.getEndDate(), match.getEndDate()) || isUpdated(w.getDescription(), match.getDescription());
        });
    }

    /**
     * Check whether mentor entity has been updated.
     *
     * @param oldEntity the old entity
     * @param newEntity the new entity.
     * @return true if value has been updated.
     */
    public static <T extends Mentor> boolean isUpdated(T oldEntity, T newEntity) {
        if(isBothNull(oldEntity, newEntity)){
            return false;
        }
        boolean updated = isUpdated((InstitutionUser) oldEntity, (InstitutionUser) newEntity);
        if (isUpdatedProfessionalExperienceDatas(oldEntity.getProfessionalExperiences(), newEntity.getProfessionalExperiences())) {
            updated = true;
            oldEntity.getProfessionalExperiences().clear();
            if (newEntity.getProfessionalExperiences() != null) {
                oldEntity.getProfessionalExperiences().addAll(newEntity.getProfessionalExperiences());
                oldEntity.getProfessionalExperiences().forEach(c -> c.setMentor(oldEntity));
            }
        }
        if (isUpdated(oldEntity.getProfessionalAreas(), newEntity.getProfessionalAreas())) {
            updated = true;
            oldEntity.setProfessionalAreas(newEntity.getProfessionalAreas());
        }
        if (isUpdated(oldEntity.getMentorType(), newEntity.getMentorType())) {
            updated = true;
            oldEntity.setMentorType(newEntity.getMentorType());
        }
        if (isUpdated(oldEntity.getCompanyName(), newEntity.getCompanyName())) {
            updated = true;
            oldEntity.setCompanyName(newEntity.getCompanyName());
        }
        if (isUpdated(oldEntity.getLinkedInUrl(), newEntity.getLinkedInUrl())) {
            updated = true;
            oldEntity.setLinkedInUrl(newEntity.getLinkedInUrl());
        }
        return updated;
    }

    /**
     * Check whether parent consent entity has been updated.
     *
     * @param oldEntity the old entity
     * @param newEntity the new entity.
     * @return true if value has been updated.
     */
    public static boolean isUpdatedParentConsent(ParentConsent oldEntity, ParentConsent newEntity) {
        if(isBothNull(oldEntity, newEntity)){
            return false;
        }
        return isUpdated(oldEntity, newEntity) ||
                isUpdated(oldEntity.getParentName(), newEntity.getParentEmail()) ||
                isUpdated(oldEntity.getSignatureFilePath(), oldEntity.getSignatureFilePath()) ||
                isUpdated(oldEntity.getParentEmail(), oldEntity.getParentEmail()) ||
                isUpdated(oldEntity.getToken(), oldEntity.getToken());
    }

    /**
     * Check whether institution affiliation code entity has been updated.
     *
     * @param oldEntity the old entity
     * @param newEntity the new entity.
     * @return true if value has been updated.
     */
    public static boolean isUpdatedInstitutionAffiliationCode(InstitutionAffiliationCode oldEntity, InstitutionAffiliationCode newEntity) {
        if(isBothNull(oldEntity, newEntity)){
            return false;
        }
        return isUpdated(oldEntity, newEntity) ||
                isUpdated(oldEntity.getCode(), newEntity.getCode()) || isUpdated(oldEntity.isUsed(), oldEntity.isUsed());
    }

    /**
     * Check whether mentee entity has been updated.
     *
     * @param oldEntity the old entity
     * @param newEntity the new entity.
     * @return true if value has been updated.
     */
    public static <T extends Mentee> boolean isUpdated(T oldEntity, T newEntity) {
        if(isBothNull(oldEntity, newEntity)){
            return false;
        }
        boolean updated = isUpdated((InstitutionUser) oldEntity, (InstitutionUser) newEntity);
        if (isUpdated(oldEntity.getFamilyIncome(), newEntity.getFamilyIncome())) {
            updated = true;
            oldEntity.setFamilyIncome(newEntity.getFamilyIncome());
        }
        if (isUpdated(oldEntity.getSchool(), newEntity.getSchool())) {
            updated = true;
            oldEntity.setSchool(newEntity.getSchool());
        }
        if (isUpdatedInstitutionAffiliationCode(oldEntity.getInstitutionAffiliationCode(), newEntity.getInstitutionAffiliationCode())) {
            updated = true;
            oldEntity.setInstitutionAffiliationCode(newEntity.getInstitutionAffiliationCode());
        }

        if (isUpdatedParentConsent(oldEntity.getParentConsent(), newEntity.getParentConsent())) {
            updated = true;
            oldEntity.setParentConsent(newEntity.getParentConsent());
        }
        if (isUpdated(oldEntity.getFacebookUrl(), newEntity.getFacebookUrl())) {
            updated = true;
            oldEntity.setFacebookUrl(newEntity.getFacebookUrl());
        }
        return updated;
    }

    /**
     * Get parent category from weighted personal interest.
     *
     * @param entity the weighted personal interest entity.
     * @return parent category
     */
    public static PersonalInterest getParentCategoryFromWeightedPersonalInterest(WeightedPersonalInterest entity) {
        if (entity != null && entity.getPersonalInterest() != null && entity.getPersonalInterest().getParentCategory() != null) {
            return entity.getPersonalInterest().getParentCategory();
        }
        return null;
    }

    /**
     * Get parent category from weighted professional interest.
     *
     * @param entity the weighted professional interest entity.
     * @return parent category
     */
    public static ProfessionalInterest getParentCategoryFromWeightedProfessionalInterest(WeightedProfessionalInterest entity) {
        if (entity != null && entity.getProfessionalInterest() != null && entity.getProfessionalInterest().getParentCategory() != null) {
            return entity.getProfessionalInterest().getParentCategory();
        }
        return null;
    }

    /**
     * Get match score.
     *
     * @param directMatchingPoints the direct matching points.
     * @param parentCategoryMatchingPoints the parent category matching points
     * @param interests1 the interest list1.
     * @param interests2 the interest list2.
     * @param weightExtractor the weight extractor
     * @param interestExtractor the interest extractor
     * @param parentCategoryExtractor the parent category extractor
     * @param <T> the identifiable entity
     * @return the matching score.
     */
    public static <T extends IdentifiableEntity> int getScore(int directMatchingPoints, int parentCategoryMatchingPoints, List<T> interests1, List<T> interests2, Function<? super T, Integer> weightExtractor, Function<? super T, IdentifiableEntity> interestExtractor, Function<? super T, IdentifiableEntity> parentCategoryExtractor) {
        int score = 0;
        while (!interests1.isEmpty()) {
            // gets the most weighted interest from the list
            T maxWeightInterest = interests1.stream().max(Comparator.comparing(weightExtractor)).get();
            Map<T, Integer> scoresForMatching = new HashMap<>();
            for (T interest : interests2) {
                int matchingScore = 0;
                IdentifiableEntity interest1 = interestExtractor.apply(maxWeightInterest);
                IdentifiableEntity interest2 = interestExtractor.apply(interest);
                IdentifiableEntity parentCategory1 = parentCategoryExtractor.apply(maxWeightInterest);
                IdentifiableEntity parentCategory2 = parentCategoryExtractor.apply(interest);
                // check if direct matching or parent category matching applies
                if (interest1 != null && interest2 != null && interest1.getId() == interest2.getId()) {
                    matchingScore = directMatchingPoints;
                } else if (parentCategory1 != null && parentCategory2 != null && parentCategory1.getId() == parentCategory2.getId()) {
                    matchingScore = parentCategoryMatchingPoints;
                }
                // only add for matching found
                if (matchingScore > 0) {
                    int weightedMatchingScore = weightExtractor.apply(maxWeightInterest) * weightExtractor.apply(interest) * matchingScore;
                    scoresForMatching.put(interest, weightedMatchingScore);
                }
            }
            //get interest with max score from scoresForMatching;
            Optional<Map.Entry<T, Integer>> maxScoreInterest = scoresForMatching.entrySet().stream().max(Comparator.comparing(Map.Entry<T, Integer>::getValue));
            if (maxScoreInterest.isPresent() && maxScoreInterest.get().getValue() > 0) {
                score += maxScoreInterest.get().getValue();
                //  remove the maxMentorInterest from interests2
                interests2.remove(maxScoreInterest.get().getKey());
            }
            //remove from interests1
            interests1.remove(maxWeightInterest);
        }
        return score;
    }
}
