package com.livingprogress.mentorme.controllers;

import com.livingprogress.mentorme.exceptions.EntityNotFoundException;
import com.livingprogress.mentorme.utils.Helper;
import com.livingprogress.mentorme.entities.EventSearchCriteria;
import com.livingprogress.mentorme.entities.Institution;
import com.livingprogress.mentorme.entities.InstitutionAffiliationCode;
import com.livingprogress.mentorme.entities.InstitutionAffiliationCodeSearchCriteria;
import com.livingprogress.mentorme.entities.InstitutionSearchCriteria;
import com.livingprogress.mentorme.entities.InstitutionSummary;
import com.livingprogress.mentorme.entities.InstitutionalProgramSearchCriteria;
import com.livingprogress.mentorme.entities.MenteeSearchCriteria;
import com.livingprogress.mentorme.entities.MentorSearchCriteria;
import com.livingprogress.mentorme.entities.Paging;
import com.livingprogress.mentorme.entities.SearchResult;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.EventService;
import com.livingprogress.mentorme.services.InstitutionAffiliationCodeService;
import com.livingprogress.mentorme.services.InstitutionService;
import com.livingprogress.mentorme.services.InstitutionalProgramService;
import com.livingprogress.mentorme.services.MenteeService;
import com.livingprogress.mentorme.services.MentorService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.security.SecureRandom;

/**
 * The Institution REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/institutions")
@NoArgsConstructor
public class InstitutionController {
    /**
     * The alphabet used to generate random String.
     */
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * The institution service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private InstitutionService institutionService;

    /**
     * The affiliation code service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private InstitutionAffiliationCodeService institutionAffiliationCodeService;

    /**
     * The affiliation code length.
     */
    @Value("${affiliationCodeLength}")
    private int affiliationCodeLength;

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
     * The institution program service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private InstitutionalProgramService institutionalProgramService;

    /**
     * The event service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private EventService eventService;


    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(institutionService, "institutionService");
        Helper.checkConfigNotNull(institutionAffiliationCodeService, "institutionAffiliationCodeService");
        Helper.checkConfigNotNull(mentorService, "mentorService");
        Helper.checkConfigNotNull(menteeService, "menteeService");
        Helper.checkConfigNotNull(institutionalProgramService, "institutionalProgramService");
        Helper.checkConfigNotNull(eventService, "eventService");
        Helper.checkConfigPositive(affiliationCodeLength, "affiliationCodeLength");
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
    public Institution get(@PathVariable long id) throws MentorMeException {
        return institutionService.get(id);
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
    public Institution create(@RequestBody Institution entity) throws MentorMeException {
        return institutionService.create(entity);
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
    public Institution update(@PathVariable long id, @RequestBody Institution entity) throws MentorMeException {
        return institutionService.update(id, entity);
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
        institutionService.delete(id);
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
    public SearchResult<Institution> search(@ModelAttribute InstitutionSearchCriteria criteria, @ModelAttribute Paging paging) throws MentorMeException {
        return institutionService.search(criteria, paging);
    }

    /**
     * This method is used to generate the affiliation code.
     *
     * @param id the ID of the institution
     * @return the affiliation code
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the institution does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "{id}/generateAffiliationCode", method = RequestMethod.PUT)
    public String generateAffiliationCode(@PathVariable long id) throws MentorMeException {
        institutionService.get(id);
        String code = getRandomString(affiliationCodeLength);
        InstitutionAffiliationCode affiliationCode = new InstitutionAffiliationCode();
        affiliationCode.setCode(code);
        affiliationCode.setInstitutionId(id);
        affiliationCode.setUsed(false);
        institutionAffiliationCodeService.create(affiliationCode);
        return affiliationCode.getCode();
    }

    /**
     * This method is used to get the institution for the affiliation code.
     *
     * @param affiliationCode the affiliation code
     * @return null if not found valid affiliation code otherwise the match institution
     * @throws IllegalArgumentException if affiliation code is null or empty
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(value = "/affiliationCode/{affiliationCode}", method = RequestMethod.GET)
    public Institution getInstitutionForAffiliationCode(@PathVariable String affiliationCode) throws MentorMeException {
        InstitutionAffiliationCode ac = getInstitutionAffiliationCode(affiliationCode);
        if (ac != null && !ac.isUsed()) {
            return institutionService.get(ac.getInstitutionId());
        }
        return null;
    }

    /**
     * This method is used to mark affiliation code as used.
     *
     * @param affiliationCode the affiliation code
     * @throws IllegalArgumentException if affiliation code is null or empty
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "useAffiliationCode", method = RequestMethod.PUT)
    public void useAffiliationCode(@RequestParam(name = "affiliationCode") String affiliationCode) throws MentorMeException {
        InstitutionAffiliationCode ac = getInstitutionAffiliationCode(affiliationCode);
        if (ac != null && !ac.isUsed()) {
            ac.setUsed(true);
            institutionAffiliationCodeService.update(ac.getId(), ac);
        } else {
            throw new EntityNotFoundException("AffiliationCode with code=" + affiliationCode + " can not be found.");
        }
    }


    /**
     * This method is used to get the institution summary.
     *
     * @param id the ID of the institution
     * @return the institution summary.
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the institution does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}/summary", method = RequestMethod.GET)
    public InstitutionSummary getInstitutionSummary(@PathVariable long id) throws MentorMeException {
        institutionService.get(id);
        InstitutionSummary result = new InstitutionSummary();
        InstitutionalProgramSearchCriteria institutionalProgramSearchCriteria = new InstitutionalProgramSearchCriteria();
        institutionalProgramSearchCriteria.setInstitutionId(id);
        result.setInstitutionalProgramsCount(institutionalProgramService.count(institutionalProgramSearchCriteria));

        EventSearchCriteria eventSearchCriteria = new EventSearchCriteria();
        eventSearchCriteria.setUpcoming(true);
        result.setUpcomingEventsCount(eventService.count(eventSearchCriteria));

        MentorSearchCriteria mentorSearchCriteria = new MentorSearchCriteria();
        mentorSearchCriteria.setInstitutionId(id);
        result.setAssociatedMentorsCount(mentorService.count(mentorSearchCriteria));

        MenteeSearchCriteria menteeSearchCriteria = new MenteeSearchCriteria();
        menteeSearchCriteria.setInstitutionId(id);
        result.setAssociatedMenteesCount(menteeService.count(menteeSearchCriteria));
        return result;
    }

    /**
     * This method is used to get match affiliation code by code.
     *
     * @param affiliationCode the affiliation code
     * @return match affiliation code otherwise the match institution
     * @throws IllegalArgumentException if affiliation code is null or empty
     * @throws MentorMeException if any other error occurred during operation
     */
    private InstitutionAffiliationCode getInstitutionAffiliationCode(String affiliationCode) throws MentorMeException {
        Helper.checkNullOrEmpty(affiliationCode, "affiliationCode");
        InstitutionAffiliationCodeSearchCriteria criteria = new InstitutionAffiliationCodeSearchCriteria();
        criteria.setCode(affiliationCode);
        SearchResult<InstitutionAffiliationCode> searchResult = institutionAffiliationCodeService.search(criteria, null);
        if (searchResult.getTotal() > 0) {
            return searchResult.getEntities().get(0);
        }
        return null;
    }

    /**
     * This method is used to generate the random string.
     *
     * @param len the length of the random string
     * @return the random string.
     */
    private static String getRandomString(int len) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(ALPHABET.charAt(rnd.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}

