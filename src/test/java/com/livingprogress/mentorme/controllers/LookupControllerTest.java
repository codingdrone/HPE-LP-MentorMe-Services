package com.livingprogress.mentorme.controllers;

import com.livingprogress.mentorme.BaseTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The test cases for <code>LookupController</code>
 */
public class LookupControllerTest extends BaseTest {

    /**
     * Test getUserRoles method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getUserRoles() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/lookups/userRoles")
                                      .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("userRoles.json")));
    }

    /**
     * Test getCountries method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getCountries() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/lookups/countries")
                                      .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("countries.json")));
    }

    /**
     * Test getStates method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getStates() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/lookups/states")
                                      .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("states.json")));
    }

    /**
     * Test getProfessionalConsultantAreas method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getProfessionalConsultantAreas() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/lookups/professionalConsultantAreas")
                                      .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("professionalConsultantAreas.json")));
    }

    /**
     * Test getGoalCategories method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getGoalCategories() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/lookups/goalCategories")
                                      .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("goalCategories.json")));
    }

    /**
     * Test getProgramCategories method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getProgramCategories() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/lookups/programCategories")
                                      .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("programCategories.json")));
    }

    /**
     * Test getPersonalInterests method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getPersonalInterests() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/lookups/personalInterests")
                                      .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("personalInterests.json")));
    }

    /**
     * Test getProfessionalInterests method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getProfessionalInterests() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/lookups/professionalInterests")
                                      .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("professionalInterests.json")));
    }

    /**
     * Test getDocumentCategories method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void getDocumentCategories() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/lookups/documentCategories")
                                      .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("documentCategories.json")));
    }
}
