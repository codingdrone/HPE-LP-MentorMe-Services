package com.livingprogress.mentorme.controllers;


import com.livingprogress.mentorme.BaseTest;
import com.livingprogress.mentorme.entities.IdentifiableEntity;
import com.livingprogress.mentorme.entities.Mentee;
import com.livingprogress.mentorme.entities.ParentConsent;
import com.livingprogress.mentorme.entities.PersonalInterest;
import com.livingprogress.mentorme.entities.ProfessionalInterest;
import com.livingprogress.mentorme.entities.SearchResult;
import com.livingprogress.mentorme.entities.UserStatus;
import com.livingprogress.mentorme.entities.WeightedPersonalInterest;
import com.livingprogress.mentorme.entities.WeightedProfessionalInterest;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The test cases for <code>MenteeController</code>
 */
public class MenteeControllerTest extends BaseTest {
    /**
     * The sample entity json.
     */
    private static String sample;

    /**
     * The demo entity json.
     */
    private static String demo;

    /**
     * All entities json.
     */
    private static String entities;

    /**
     * Read related json.
     *
     * @throws Exception throws if any error happens.
     */
    @BeforeClass
    public static void setupClass() throws Exception {
        sample = readFile("mentee4.json");
        demo = readFile("demo-mentee.json");
        entities = readFile("mentees.json");
    }

    /**
     * Test get method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees/4")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(sample));
    }

    /**
     * Test create method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void create() throws Exception {
        Mentee demoEntity = objectMapper.readValue(demo, Mentee.class);
        assertNotNull(demoEntity.getPassword());
        assertNull(demoEntity.getCreatedOn());
        demoEntity.setPassword(null);
        // will create mentee as inactive
        demoEntity.setStatus(UserStatus.ACTIVE);
        checkEntities(demoEntity.getPersonalInterests());
        checkEntities(demoEntity.getProfessionalInterests());
        checkEntity(demoEntity.getParentConsent());
        checkEntity(demoEntity.getInstitutionAffiliationCode());
        String res = mockMvc.perform(MockMvcRequestBuilders.post("/mentees")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(demo))
                            .andExpect(status().isCreated())
                            .andExpect(jsonPath("$.password").doesNotExist())
                            .andExpect(jsonPath("$.id").isNumber())
                            .andExpect(jsonPath("$.createdOn").exists())
                            .andExpect(jsonPath("$.status").value("INACTIVE"))
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        Mentee result = objectMapper.readValue(res, Mentee.class);
        demoEntity.setId(result.getId());
        demoEntity.setCreatedOn(result.getCreatedOn());
        demoEntity.setStatus(UserStatus.INACTIVE);
        // will use random value
        assertNotEquals(demoEntity.getParentConsent()
                                  .getToken(), result.getParentConsent()
                                                     .getToken());
        demoEntity.getParentConsent()
                  .setToken(result.getParentConsent()
                                  .getToken());
        verifyEntities(demoEntity.getPersonalInterests(), result.getPersonalInterests());
        verifyEntities(demoEntity.getProfessionalInterests(), result.getProfessionalInterests());
        verifyEntity(demoEntity.getParentConsent(), result.getParentConsent());
        verifyEntity(demoEntity.getInstitutionAffiliationCode(), result.getInstitutionAffiliationCode());
        assertEquals(objectMapper.writeValueAsString(demoEntity), objectMapper.writeValueAsString(result));
        ParentConsent parentConsent = result.getParentConsent();
        verifyEmail("New Mentee", parentConsent.getToken(), parentConsent.getParentEmail());
    }

    /**
     * Test update method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void update() throws Exception {
        // no updates
        mockMvc.perform(MockMvcRequestBuilders.put("/mentees/4")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(sample))
               .andExpect(status().isOk())
               .andExpect(content().json(sample));
        Mentee demoEntity = objectMapper.readValue(demo, Mentee.class);
        checkEntities(demoEntity.getPersonalInterests());
        checkEntities(demoEntity.getProfessionalInterests());
        checkEntity(demoEntity.getParentConsent());
        demoEntity.getPersonalInterests()
                  .get(0)
                  .setId(1L);
        demoEntity.getProfessionalInterests()
                  .get(0)
                  .setId(1L);
        demoEntity.getParentConsent()
                  .setId(1L);
        // try to update created on
        demoEntity.setCreatedOn(sampleFutureDate);
        demoEntity.setId(4);
        String json = objectMapper.writeValueAsString(demoEntity);
        String res = mockMvc.perform(MockMvcRequestBuilders.put("/mentees/4")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(json))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.password").doesNotExist())
                            .andExpect(jsonPath("$.id").isNumber())
                            .andExpect(jsonPath("$.createdOn").exists())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        Mentee result = objectMapper.readValue(res, Mentee.class);

        // same id entity just updates
        assertEquals(1, result.getPersonalInterests()
                              .get(0)
                              .getId());
        assertEquals(1, result.getProfessionalInterests()
                              .get(0)
                              .getId());
        assertEquals(1, result.getParentConsent()
                              .getId());

        // will not update created on during updating
        assertNotEquals(sampleFutureDate, result.getCreatedOn());
        demoEntity.setCreatedOn(result.getCreatedOn());
        verifyEntities(demoEntity.getPersonalInterests(), result.getPersonalInterests());
        verifyEntities(demoEntity.getProfessionalInterests(), result.getProfessionalInterests());
        verifyEntity(demoEntity.getParentConsent(), result.getParentConsent());
        verifyEntity(demoEntity.getInstitutionAffiliationCode(), result.getInstitutionAffiliationCode());
        assertEquals(objectMapper.writeValueAsString(demoEntity), objectMapper.writeValueAsString(result));
        // test nested properties
        demoEntity.setParentConsent(null);
        demoEntity.setPersonalInterests(null);
        demoEntity.setProfessionalInterests(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/mentees/4")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.password").doesNotExist())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.parentConsent").doesNotExist())
               .andExpect(jsonPath("$.personalInterests", Matchers.hasSize(0)))
               .andExpect(jsonPath("$.professionalInterests", Matchers.hasSize(0)));
        // second null updates
        mockMvc.perform(MockMvcRequestBuilders.put("/mentees/4")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.password").doesNotExist())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.parentConsent").doesNotExist())
               .andExpect(jsonPath("$.personalInterests", Matchers.hasSize(0)))
               .andExpect(jsonPath("$.professionalInterests", Matchers.hasSize(0)));
        // update with new nested values
        List<PersonalInterest> personalInterests = lookupService.getPersonalInterests();
        assertTrue(personalInterests.size() > 0);
        demoEntity.setPersonalInterests(personalInterests.stream()
                                                         .map(p -> {
                                                             WeightedPersonalInterest weightedPersonalInterest = new
                                                                     WeightedPersonalInterest();
                                                             weightedPersonalInterest.setPersonalInterest(p);
                                                             weightedPersonalInterest.setWeight(1);
                                                             return weightedPersonalInterest;
                                                         })
                                                         .collect(Collectors.toList()));
        List<ProfessionalInterest> professionalInterests = lookupService.getProfessionalInterests();
        assertTrue(professionalInterests.size() > 0);
        demoEntity.setProfessionalInterests(professionalInterests.stream()
                                                                 .map(p -> {
                                                                     WeightedProfessionalInterest
                                                                            weightedProfessionalInterest = new
                                                                             WeightedProfessionalInterest();
                                                                     weightedProfessionalInterest.setProfessionalInterest(p);
                                                                     weightedProfessionalInterest.setWeight(1);
                                                                     return weightedProfessionalInterest;
                                                                 })
                                                                 .collect(Collectors.toList()));
        mockMvc.perform(MockMvcRequestBuilders.put("/mentees/4")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.password").doesNotExist())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.parentConsent").doesNotExist())
               .andExpect(jsonPath("$.personalInterests", Matchers.hasSize(personalInterests.size())))
               .andExpect(jsonPath("$.professionalInterests", Matchers.hasSize(professionalInterests.size())));
    }

    /**
     * Test delete method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/mentees/4"))
               .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees/4"))
               .andExpect(status().isNotFound());
        mockMvc.perform(MockMvcRequestBuilders.delete("/mentees/4"))
               .andExpect(status().isNotFound());
    }

    /**
     * Test search method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void search() throws Exception {
        SearchResult<Mentee> result = readSearchResult(entities, Mentee.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees?sortColumn=id&sortOrder=ASC")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(entities, true));
        SearchResult<Mentee> result1 = getSearchResult
                ("/mentees?pageNumber=1&pageSize=2&sortColumn=id&sortOrder=ASC", Mentee.class);
        assertEquals(result.getTotal(), result1.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result1.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .skip(2)
                                .limit(2)
                                .map(Mentee::getId)
                                .toArray(),
                result1.getEntities()
                       .stream()
                       .map(Mentee::getId)
                       .toArray());
        SearchResult<Mentee> result2 = getSearchResult
                ("/mentees?pageNumber=1&pageSize=2&sortColumn=id&sortOrder=DESC", Mentee.class);
        assertEquals(result.getTotal(), result2.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result2.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .sorted(Comparator.comparing(IdentifiableEntity::getId)
                                                  .reversed())
                                .skip(2)
                                .limit(2)
                                .map(Mentee::getId)
                                .toArray(),
                result2.getEntities()
                       .stream()
                       .map(Mentee::getId)
                       .toArray());

        SearchResult<Mentee> result3 = getSearchResult
                ("/mentees?pageNumber=1&pageSize=2&sortColumn=username&sortOrder=DESC", Mentee.class);
        assertEquals(result.getTotal(), result2.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result2.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .sorted(Comparator.comparing(Mentee::getUsername)
                                                  .reversed())
                                .skip(2)
                                .limit(2)
                                .map(Mentee::getId)
                                .toArray(),
                result3.getEntities()
                       .stream()
                       .map(Mentee::getId)
                       .toArray());
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees?institutionId=2")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(10));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees?status=INACTIVE")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(11));
        ;
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees?minAveragePerformanceScore=5")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(14));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees?maxAveragePerformanceScore=0")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(4));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees?name=firstname13")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(13));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees?name=lastname14")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(14));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees?schoolName=school4")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(12));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees?personalInterests[0].id=1")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(4));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees?professionalInterests[0].id=1")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(4));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees?assignedToInstitution=false")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(3))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(3)));
        mockMvc.perform(MockMvcRequestBuilders.get
                ("/mentees?pageNumber=0&pageSize=2&sortColumn=username&sortOrder=DESC&institutionId=1&status=ACTIVE" +
                        "&minAveragePerformanceScore=0&maxAveragePerformanceScore=0&name=firstname4&schoolName" +
                "=school1&personalInterests[0].id=1&professionalInterests[0].id=1&assignedToInstitution=true")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(4));
    }

    /**
     * Test getAverageScore method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getAverageScore() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees/4/averageScore"))
               .andExpect(status().isOk())
               .andExpect(content().string(readFile("mentee4AverageScore.txt")));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees/999/averageScore"))
               .andExpect(status().isNotFound());
    }

    /**
     * Test getMatchingMentors method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getMatchingMentors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees/4/matchingMentors"))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("mentee4MatchingMentors.json")));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees/999/matchingMentors"))
               .andExpect(status().isNotFound());
    }

    /**
     * Test confirmParentConsent method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void confirmParentConsent() throws Exception {
        String res = mockMvc.perform(MockMvcRequestBuilders.post("/mentees")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(demo))
                            .andExpect(status().isCreated())
                            .andExpect(jsonPath("$.password").doesNotExist())
                            .andExpect(jsonPath("$.id").isNumber())
                            .andExpect(jsonPath("$.createdOn").exists())
                            .andExpect(jsonPath("$.status").value("INACTIVE"))
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        Mentee result = objectMapper.readValue(res, Mentee.class);
        String token = result.getParentConsent()
                             .getToken();
        mockMvc.perform(MockMvcRequestBuilders.put("/mentees/confirmParentConsent?token=" + token))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees/" + result.getId())
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value("ACTIVE"));
        mockMvc.perform(MockMvcRequestBuilders.put("/mentees/confirmParentConsent?token=" + token))
               .andExpect(status().isOk())
               .andExpect(content().string("false"));
    }
}
