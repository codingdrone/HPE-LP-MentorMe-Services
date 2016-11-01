package com.livingprogress.mentorme.controllers;

import com.livingprogress.mentorme.aop.LogAspect;
import com.livingprogress.mentorme.entities.Mentee;
import com.livingprogress.mentorme.entities.MenteeSearchCriteria;
import com.livingprogress.mentorme.entities.Mentor;
import com.livingprogress.mentorme.entities.MentorSearchCriteria;
import com.livingprogress.mentorme.entities.Paging;
import com.livingprogress.mentorme.entities.ProfessionalExperienceData;
import com.livingprogress.mentorme.entities.SearchResult;
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
import org.springframework.social.linkedin.api.LinkedIn;
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
import java.util.stream.Collectors;

/**
 * The Mentor REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/mentors")
@NoArgsConstructor
public class MentorController {
    /**
     * The mentor service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private MentorService mentorService;

    /**
     * The mentee service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private MenteeService menteeService;

    /**
     * The amount of points if there is a direct matching of the interests.
     */
    @Value("${matchingMentees.directMatchingPoints}")
    private int directMatchingPoints;

    /**
     * The amount of points if there is a parent category matching of the interests.
     */
    @Value("${matchingMentees.parentCategoryMatchingPoints}")
    private int parentCategoryMatchingPoints;

    /**
     * The coefficient for the professional interests score (professional interests are more important than personal ones).
     */
    @Value("${matchingMentees.professionalInterestsCoefficient}")
    private int professionalInterestsCoefficient;

    /**
     * The coefficient for the personal interests.
     */
    @Value("${matchingMentees.personalInterestsCoefficient}")
    private int personalInterestsCoefficient;

    /**
     * the top N best matching mentors that should be returned.
     */
    @Value("${matchingMentees.topMatchingAmount}")
    private int topMatchingAmount;

    /**
     * Spring social linked in api.
     */
    private LinkedIn linkedIn;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(menteeService, "menteeService");
        Helper.checkConfigNotNull(mentorService, "mentorService");
        Helper.checkPositive(directMatchingPoints, "directMatchingPoints");
        Helper.checkPositive(parentCategoryMatchingPoints, "parentCategoryMatchingPoints");
        Helper.checkPositive(professionalInterestsCoefficient, "professionalInterestsCoefficient");
        Helper.checkPositive(personalInterestsCoefficient, "personalInterestsCoefficient");
        Helper.checkPositive(topMatchingAmount, "topMatchingAmount");
        Helper.checkConfigState(professionalInterestsCoefficient > personalInterestsCoefficient, "professional interests are more important than personal ones");
        //TODO uncomment below if implemented linkedin
        // Helper.checkConfigNotNull(linkedIn, "linkedIn");
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
    public Mentor get(@PathVariable long id) throws MentorMeException {
        return mentorService.get(id);
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
    public Mentor create(@RequestBody Mentor entity) throws MentorMeException {
        return mentorService.create(entity);
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
    public Mentor update(@PathVariable long id, @RequestBody Mentor entity) throws MentorMeException {
        return mentorService.update(id, entity);
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
        mentorService.delete(id);
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
    public SearchResult<Mentor> search(@ModelAttribute MentorSearchCriteria criteria, @ModelAttribute Paging paging) throws MentorMeException {
        return mentorService.search(criteria, paging);
    }

    /**
     * This method is used to get the mentor avg score.
     *
     * @param id the id of the entity to retrieve
     * @return the avg score
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}/averageScore", method = RequestMethod.GET)
    public int getAverageScore(@PathVariable long id) throws MentorMeException {
        return mentorService.getAverageMentorScore(id);
    }

    /**
     * This method is used to get the matching mentees.
     *
     * @param id the id of the entity to retrieve
     * @return the matching mentees.
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}/matchingMentees", method = RequestMethod.GET)
    public List<Mentee> getMatchingMentees(@PathVariable long id) throws MentorMeException {
        Mentor mentor = mentorService.get(id);
        // get all institution mentors or all unassigned mentors
        MenteeSearchCriteria criteria = new MenteeSearchCriteria();
        if (mentor.isAssignedToInstitution()) {
            criteria.setInstitutionId(mentor.getInstitution().getId());
        } else {
            criteria.setAssignedToInstitution(false);
        }
        List<Mentee> mentees = menteeService.search(criteria, null).getEntities();
        Map<Mentee, Integer> menteeScores = new HashMap<>();
        for (Mentee mentee : mentees) {
            int professionalScore = Helper.getScore(directMatchingPoints, parentCategoryMatchingPoints, new ArrayList<>(mentee.getProfessionalInterests()), new ArrayList<>(mentee.getProfessionalInterests()), WeightedProfessionalInterest::getWeight, WeightedProfessionalInterest::getProfessionalInterest, Helper::getParentCategoryFromWeightedProfessionalInterest);
            int personalScore = Helper.getScore(directMatchingPoints, parentCategoryMatchingPoints, new ArrayList<>(mentee.getPersonalInterests()), new ArrayList<>(mentee.getPersonalInterests()), WeightedPersonalInterest::getWeight, WeightedPersonalInterest::getPersonalInterest, Helper::getParentCategoryFromWeightedPersonalInterest);
            menteeScores.put(mentee, professionalScore * professionalInterestsCoefficient + personalScore * personalInterestsCoefficient);
        }

        // comment below if do not want to show score in log
        menteeScores.entrySet().forEach(k -> Helper.logDebugMessage(LogAspect.LOGGER, k.getKey().getId() + "," + k.getValue()));
        // sort the mentorScores by scores and return the top <topMatchingAmount> mentees;
        return menteeScores.entrySet().stream().sorted(Comparator.comparing(Map.Entry<Mentee, Integer>::getValue).reversed()) // reverse means desc order
            .map(Map.Entry::getKey).limit(topMatchingAmount).collect(Collectors.toList());
    }

    /**
     * This method is used to retrieve the linked in experience data.
     *
     * @return the professional experience data
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(value = "linkedInExperience", method = RequestMethod.GET)
    public List<ProfessionalExperienceData> getLinkedInProfessionalExperienceData() throws MentorMeException {
        //TODO fix this if implemented linkedin
        throw new MentorMeException("Not implemented!");
    }
}

