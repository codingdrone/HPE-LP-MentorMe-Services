package com.livingprogress.mentorme.controllers;

import com.livingprogress.mentorme.BaseTest;
import com.livingprogress.mentorme.entities.IdentifiableEntity;
import com.livingprogress.mentorme.entities.Mentor;
import com.livingprogress.mentorme.entities.PersonalInterest;
import com.livingprogress.mentorme.entities.ProfessionalConsultantArea;
import com.livingprogress.mentorme.entities.ProfessionalExperienceData;
import com.livingprogress.mentorme.entities.ProfessionalInterest;
import com.livingprogress.mentorme.entities.SearchResult;
import com.livingprogress.mentorme.entities.WeightedPersonalInterest;
import com.livingprogress.mentorme.entities.WeightedProfessionalInterest;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
 * The test cases for <code>MentorController</code>
 */
public class MentorControllerTest extends BaseTest {
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
        sample = readFile("mentor3.json");
        demo = readFile("demo-mentor.json");
        entities = readFile("mentors.json");
    }

    /**
     * Test get method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors/3")
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
        Mentor demoEntity = objectMapper.readValue(demo, Mentor.class);
        assertNotNull(demoEntity.getPassword());
        assertNull(demoEntity.getCreatedOn());
        demoEntity.setPassword(null);
        checkEntities(demoEntity.getPersonalInterests());
        checkEntities(demoEntity.getProfessionalExperiences());
        checkEntities(demoEntity.getProfessionalInterests());
        String res = mockMvc.perform(MockMvcRequestBuilders.post("/mentors")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(demo))
                            .andExpect(status().isCreated())
                            .andExpect(jsonPath("$.password").doesNotExist())
                            .andExpect(jsonPath("$.id").isNumber())
                            .andExpect(jsonPath("$.createdOn").exists())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        Mentor result = objectMapper.readValue(res, Mentor.class);
        demoEntity.setId(result.getId());
        demoEntity.setCreatedOn(result.getCreatedOn());
        verifyEntities(demoEntity.getPersonalInterests(), result.getPersonalInterests());
        verifyEntities(demoEntity.getProfessionalExperiences(), result.getProfessionalExperiences());
        verifyEntities(demoEntity.getProfessionalInterests(), result.getProfessionalInterests());
        assertEquals(objectMapper.writeValueAsString(demoEntity), objectMapper.writeValueAsString(result));
    }

    /**
     * Test update method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void update() throws Exception {
        // no updates
        mockMvc.perform(MockMvcRequestBuilders.put("/mentors/3")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(sample))
               .andExpect(status().isOk())
               .andExpect(content().json(sample));
        Mentor demoEntity = objectMapper.readValue(demo, Mentor.class);
        checkEntities(demoEntity.getPersonalInterests());
        checkEntities(demoEntity.getProfessionalExperiences());
        checkEntities(demoEntity.getProfessionalInterests());
        demoEntity.getPersonalInterests()
                  .get(0)
                  .setId(1L);
        demoEntity.getProfessionalExperiences()
                  .get(0)
                  .setId(1L);
        demoEntity.getProfessionalInterests()
                  .get(0)
                  .setId(1L);
        // try to update created on
        demoEntity.setCreatedOn(sampleFutureDate);
        demoEntity.setId(3);
        String json = objectMapper.writeValueAsString(demoEntity);
        String res = mockMvc.perform(MockMvcRequestBuilders.put("/mentors/3")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(json))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.password").doesNotExist())
                            .andExpect(jsonPath("$.id").isNumber())
                            .andExpect(jsonPath("$.createdOn").exists())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        Mentor result = objectMapper.readValue(res, Mentor.class);

        // same id entity just updates
        assertEquals(1, result.getPersonalInterests()
                              .get(0)
                              .getId());
        assertEquals(1, result.getProfessionalExperiences()
                              .get(0)
                              .getId());
        assertEquals(1, result.getProfessionalInterests()
                              .get(0)
                              .getId());
        // will not update created on during updating
        assertNotEquals(sampleFutureDate, result.getCreatedOn());
        demoEntity.setCreatedOn(result.getCreatedOn());
        verifyEntities(demoEntity.getPersonalInterests(), result.getPersonalInterests());
        verifyEntities(demoEntity.getProfessionalExperiences(), result.getProfessionalExperiences());
        verifyEntities(demoEntity.getProfessionalInterests(), result.getProfessionalInterests());
        assertEquals(objectMapper.writeValueAsString(demoEntity), objectMapper.writeValueAsString(result));
        // test nested properties
        demoEntity.setPersonalInterests(null);
        demoEntity.setProfessionalInterests(null);
        demoEntity.setProfessionalAreas(null);
        demoEntity.setProfessionalExperiences(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/mentors/3")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.password").doesNotExist())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.personalInterests", Matchers.hasSize(0)))
               .andExpect(jsonPath("$.professionalInterests", Matchers.hasSize(0)))
               .andExpect(jsonPath("$.professionalExperiences", Matchers.hasSize(0)))
               .andExpect(jsonPath("$.professionalAreas").doesNotExist());
        // second null updates
        mockMvc.perform(MockMvcRequestBuilders.put("/mentors/3")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.password").doesNotExist())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.personalInterests", Matchers.hasSize(0)))
               .andExpect(jsonPath("$.professionalInterests", Matchers.hasSize(0)))
               .andExpect(jsonPath("$.professionalExperiences", Matchers.hasSize(0)))
               .andExpect(jsonPath("$.professionalAreas", Matchers.hasSize(0)));
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
        List<ProfessionalConsultantArea>  areas = lookupService.getProfessionalConsultantAreas();
        assertTrue(areas.size() > 0);
        demoEntity.setProfessionalAreas(areas);
        int count = 3;
        demoEntity.setProfessionalExperiences(new ArrayList<>());
        IntStream.range(0, count).forEach(idx -> {
            ProfessionalExperienceData data= new ProfessionalExperienceData();
            data.setPosition("position" + idx);
            data.setWorkLocation("workLocation" + idx);
            data.setDescription("description" + idx);
            data.setStartDate(new Date());
            data.setEndDate(new Date());
            demoEntity.getProfessionalExperiences().add(data);
        });
        mockMvc.perform(MockMvcRequestBuilders.put("/mentors/3")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.password").doesNotExist())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.personalInterests", Matchers.hasSize(personalInterests.size())))
               .andExpect(jsonPath("$.professionalInterests", Matchers.hasSize(professionalInterests.size())))
               .andExpect(jsonPath("$.professionalExperiences", Matchers.hasSize(count)))
               .andExpect(jsonPath("$.professionalAreas", Matchers.hasSize(areas.size())));
    }

    /**
     * Test delete method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/mentors/3"))
               .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors/3"))
               .andExpect(status().isNotFound());
        mockMvc.perform(MockMvcRequestBuilders.delete("/mentors/3"))
               .andExpect(status().isNotFound());
    }

    /**
     * Test search method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void search() throws Exception {
        SearchResult<Mentor> result = readSearchResult(entities, Mentor.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?sortColumn=id&sortOrder=ASC")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(entities, true));
        SearchResult<Mentor> result1 = getSearchResult
                ("/mentors?pageNumber=1&pageSize=2&sortColumn=id&sortOrder=ASC", Mentor.class);
        assertEquals(result.getTotal(), result1.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result1.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .skip(2)
                                .limit(2)
                                .map(Mentor::getId)
                                .toArray(),
                result1.getEntities()
                       .stream()
                       .map(Mentor::getId)
                       .toArray());
        SearchResult<Mentor> result2 = getSearchResult
                ("/mentors?pageNumber=1&pageSize=2&sortColumn=id&sortOrder=DESC", Mentor.class);
        assertEquals(result.getTotal(), result2.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result2.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .sorted(Comparator.comparing(IdentifiableEntity::getId)
                                                  .reversed())
                                .skip(2)
                                .limit(2)
                                .map(Mentor::getId)
                                .toArray(),
                result2.getEntities()
                       .stream()
                       .map(Mentor::getId)
                       .toArray());

        SearchResult<Mentor> result3 = getSearchResult
                ("/mentors?pageNumber=1&pageSize=2&sortColumn=username&sortOrder=DESC", Mentor.class);
        assertEquals(result.getTotal(), result2.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result2.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .sorted(Comparator.comparing(Mentor::getUsername)
                                                  .reversed())
                                .skip(2)
                                .limit(2)
                                .map(Mentor::getId)
                                .toArray(),
                result3.getEntities()
                       .stream()
                       .map(Mentor::getId)
                       .toArray());
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?mentorType=PROFESSIONAL_CONSULTANT")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(3))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(3)));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?institutionId=2")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(5));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?status=INACTIVE")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?professionalAreas[0].id=2")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(3));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?minAveragePerformanceScore=5")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(9));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?maxAveragePerformanceScore=0")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(3));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?name=firstname8")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(8));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?name=lastname7")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(7));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?companyName=companyName4")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(7));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?mentorRequestStatus=APPROVED")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(3));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?personalInterests[0].id=1")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(3));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?professionalInterests[0].id=1")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(3));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?assignedToInstitution=false")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(3))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(3)));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors?pageNumber=0&pageSize=2&sortColumn=username&sortOrder=DESC&mentorType=MENTEE_PAIRED&institutionId=1&status=ACTIVE&professionalAreas[0]" +
                ".id=1&minAveragePerformanceScore=0&maxAveragePerformanceScore=0&name=firstname3&companyName=companyName1& mentorRequestStatus=APPROVED&personalInterests[0].id=1&professionalInterests[0].id=1&assignedToInstitution=true")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(3));
    }

    /**
     * Test getAverageScore method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getAverageScore() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors/3/averageScore"))
               .andExpect(status().isOk())
               .andExpect(content().string(readFile("mentor3AverageScore.txt")));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors/999/averageScore"))
               .andExpect(status().isNotFound());
    }

    /**
     * Test getMatchingMentees method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getMatchingMentees() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors/3/matchingMentees"))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("mentor3MatchingMentees.json")));
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors/999/matchingMentees"))
               .andExpect(status().isNotFound());
    }

    /**
     * Test getLinkedInProfessionalExperienceData method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getLinkedInProfessionalExperienceData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors/linkedInExperience"))
               .andExpect(status().isInternalServerError());
    }
}
