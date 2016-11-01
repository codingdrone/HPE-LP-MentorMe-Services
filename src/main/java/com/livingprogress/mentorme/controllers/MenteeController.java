package com.livingprogress.mentorme.controllers;

import com.livingprogress.mentorme.aop.LogAspect;
import com.livingprogress.mentorme.entities.Mentee;
import com.livingprogress.mentorme.entities.MenteeSearchCriteria;
import com.livingprogress.mentorme.entities.Mentor;
import com.livingprogress.mentorme.entities.MentorSearchCriteria;
import com.livingprogress.mentorme.entities.Paging;
import com.livingprogress.mentorme.entities.ParentConsent;
import com.livingprogress.mentorme.entities.SearchResult;
import com.livingprogress.mentorme.entities.UserStatus;
import com.livingprogress.mentorme.entities.WeightedPersonalInterest;
import com.livingprogress.mentorme.entities.WeightedProfessionalInterest;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.EntityNotFoundException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.MenteeService;
import com.livingprogress.mentorme.services.MentorService;
import com.livingprogress.mentorme.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The Mentee REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/mentees")
@NoArgsConstructor
public class MenteeController extends BaseEmailController {
    /**
     * The mentee service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private MenteeService menteeService;

    /**
     * The mentor service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private MentorService mentorService;

    /**
     * The amount of points if there is a direct matching of the interests.
     */
    @Value("${matchingMentors.directMatchingPoints}")
    private int directMatchingPoints;

    /**
     * The amount of points if there is a parent category matching of the interests.
     */
    @Value("${matchingMentors.parentCategoryMatchingPoints}")
    private int parentCategoryMatchingPoints;

    /**
     * The coefficient for the professional interests score (professional interests are more important than personal ones).
     */
    @Value("${matchingMentors.professionalInterestsCoefficient}")
    private int professionalInterestsCoefficient;

    /**
     * The coefficient for the personal interests.
     */
    @Value("${matchingMentors.personalInterestsCoefficient}")
    private int personalInterestsCoefficient;

    /**
     * the top N best matching mentors that should be returned.
     */
    @Value("${matchingMentors.topMatchingAmount}")
    private int topMatchingAmount;


    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(menteeService, "menteeService");
        Helper.checkConfigNotNull(mentorService, "mentorService");
        Helper.checkPositive(directMatchingPoints, "directMatchingPoints");
        Helper.checkPositive(parentCategoryMatchingPoints, "parentCategoryMatchingPoints");
        Helper.checkPositive(professionalInterestsCoefficient, "professionalInterestsCoefficient");
        Helper.checkPositive(personalInterestsCoefficient, "personalInterestsCoefficient");
        Helper.checkPositive(topMatchingAmount, "topMatchingAmount");
        Helper.checkConfigState(professionalInterestsCoefficient > personalInterestsCoefficient, "professional interests are more important than personal ones");
    }

    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Mentee get(@PathVariable long id) throws MentorMeException {
        return menteeService.get(id);
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Mentee create(@RequestBody Mentee entity) throws MentorMeException {
        Helper.checkNull(entity, "entity");
        entity.setStatus(UserStatus.INACTIVE);
        boolean sendEmail = false;
        if (entity.getParentConsent() != null) {
            ParentConsent parentConsent = entity.getParentConsent();
            Helper.checkEmail(parentConsent.getParentEmail(), "parentConsent.parentEmail");
            parentConsent.setToken(UUID.randomUUID().toString());
            sendEmail = true;
        }
        Mentee mentee = menteeService.create(entity);
        if (sendEmail) {
            // will create parent consent and only send email if created successfully
            Map<String, Object> model = new HashMap<>();
            model.put("parentConsent", mentee.getParentConsent());
            sendEmail(mentee.getParentConsent().getParentEmail(), "createMentee", model);
        }
        return mentee;
    }

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Mentee update(@PathVariable long id, @RequestBody Mentee entity) throws MentorMeException {
        return menteeService.update(id, entity);
    }

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) throws MentorMeException {
        menteeService.delete(id);
    }

    /**
     * This method is used to search for entities by criteria and paging params.
     *
     * @param criteria the search criteria
     * @param paging the paging data
     * @return the search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.GET)
    public SearchResult<Mentee> search(@ModelAttribute MenteeSearchCriteria criteria, @ModelAttribute Paging paging) throws MentorMeException {
        return menteeService.search(criteria, paging);
    }

    /**
     * This method is used to get the mentee avg score.
     *
     * @param id the id of the entity to retrieve
     * @return the avg score
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}/averageScore", method = RequestMethod.GET)
    public int getAverageScore(@PathVariable long id) throws MentorMeException {
        return menteeService.getAverageMenteeScore(id);
    }

    /**
     * This method is used to get the matching mentors.
     *
     * @param id the id of the entity to retrieve
     * @return the matching mentors.
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}/matchingMentors", method = RequestMethod.GET)
    public List<Mentor> getMatchingMentors(@PathVariable long id) throws MentorMeException {
        Mentee mentee = menteeService.get(id);
        // get all institution mentors or all unassigned mentors
        MentorSearchCriteria criteria = new MentorSearchCriteria();
        if (mentee.isAssignedToInstitution()) {
            criteria.setInstitutionId(mentee.getInstitution().getId());
        } else {
            criteria.setAssignedToInstitution(false);
        }
        List<Mentor> mentors = mentorService.search(criteria, null).getEntities();
        Map<Mentor, Integer> mentorScores = new HashMap<>();
        for (Mentor mentor : mentors) {
            int professionalScore = Helper.getScore(directMatchingPoints, parentCategoryMatchingPoints, new ArrayList<>(mentee.getProfessionalInterests()), new ArrayList<>(mentor.getProfessionalInterests()), WeightedProfessionalInterest::getWeight, WeightedProfessionalInterest::getProfessionalInterest, Helper::getParentCategoryFromWeightedProfessionalInterest);
            int personalScore = Helper.getScore(directMatchingPoints, parentCategoryMatchingPoints, new ArrayList<>(mentee.getPersonalInterests()), new ArrayList<>(mentor.getPersonalInterests()), WeightedPersonalInterest::getWeight, WeightedPersonalInterest::getPersonalInterest, Helper::getParentCategoryFromWeightedPersonalInterest);
            mentorScores.put(mentor, professionalScore * professionalInterestsCoefficient + personalScore * personalInterestsCoefficient);
        }

        // comment below if do not want to show score in log
        mentorScores.entrySet().forEach(k -> Helper.logDebugMessage(LogAspect.LOGGER, k.getKey().getId() + "," + k.getValue()));
        // sort the mentorScores by scores and return the top <topMatchingAmount> mentors
        return mentorScores.entrySet().stream().sorted(Comparator.comparing(Map.Entry<Mentor, Integer>::getValue).reversed()) // reverse means desc order
            .map(Map.Entry::getKey).limit(topMatchingAmount).collect(Collectors.toList());
    }

    /**
     * This method is used to confirm the parent consent.
     *
     * @param token the token
     * @return true if found parent consent for inactive mentee
     * @throws IllegalArgumentException if token is null or empty
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "confirmParentConsent", method = RequestMethod.PUT)
    public boolean confirmParentConsent(String token) throws MentorMeException {
        return menteeService.confirmParentConsent(token);
    }
}

