package com.livingprogress.mentorme.controllers;

import com.livingprogress.mentorme.BaseTest;
import com.livingprogress.mentorme.entities.Event;
import com.livingprogress.mentorme.entities.IdentifiableEntity;
import com.livingprogress.mentorme.entities.Institution;
import com.livingprogress.mentorme.entities.InstitutionAffiliationCode;
import com.livingprogress.mentorme.entities.InstitutionContact;
import com.livingprogress.mentorme.entities.SearchResult;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The test cases for <code>InstitutionController</code>
 */
public class InstitutionControllerTest extends BaseTest {
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
        sample = readFile("institution1.json");
        demo = readFile("demo-institution.json");
        entities = readFile("institutions.json");
    }


    /**
     * Test get method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions/1")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(sample));
    }

    /**
     * Test get method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void create() throws Exception {
        Institution demoEntity = objectMapper.readValue(demo, Institution.class);
        assertNull(demoEntity.getCreatedOn());
        checkEntities(demoEntity.getContacts());
        String res = mockMvc.perform(MockMvcRequestBuilders.post("/institutions")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(demo))
                            .andExpect(status().isCreated())
                            .andExpect(jsonPath("$.id").isNumber())
                            .andExpect(jsonPath("$.createdOn").exists())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        Institution result = objectMapper.readValue(res, Institution.class);
        demoEntity.setId(result.getId());
        demoEntity.setCreatedOn(result.getCreatedOn());
        verifyEntities(demoEntity.getContacts(), result.getContacts());
        assertEquals(objectMapper.writeValueAsString(demoEntity), objectMapper.writeValueAsString(result));
    }

    /**
     * Test update method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void update() throws Exception {
        Institution sampleEntity = objectMapper.readValue(sample, Institution.class);
        Institution demoEntity = objectMapper.readValue(demo, Institution.class);
        BeanUtils.copyProperties(demoEntity, sampleEntity);
        demoEntity.setCreatedOn(sampleFutureDate);
        demoEntity.setId(1);
        checkEntities(demoEntity.getContacts());
        demoEntity.getContacts().get(0).setId(1L);
        String json = objectMapper.writeValueAsString(demoEntity);
        String res = mockMvc.perform(MockMvcRequestBuilders.put("/institutions/1")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(json))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.id").isNumber())
                            .andExpect(jsonPath("$.createdOn").exists())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        Institution result = objectMapper.readValue(res, Institution.class);
        // will not update created on during updating
        assertNotEquals(sampleFutureDate, result.getCreatedOn());
        demoEntity.setCreatedOn(result.getCreatedOn());
        // same id entity just updates
        assertEquals(1, result.getContacts().get(0).getId());
        verifyEntities(demoEntity.getContacts(), result.getContacts());
        assertEquals(objectMapper.writeValueAsString(demoEntity), objectMapper.writeValueAsString(result));
        // test nested properties
        demoEntity.setContacts(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/institutions/1")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.contacts", Matchers.hasSize(0)));
        // second null updates
        mockMvc.perform(MockMvcRequestBuilders.put("/institutions/1")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.contacts", Matchers.hasSize(0)));
        // empty list
        demoEntity.setContacts(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.put("/institutions/1")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.contacts", Matchers.hasSize(0)));
        demoEntity.setContacts(new ArrayList<>());
        int contactCount = 3;
        IntStream.range(0, contactCount).forEach(idx -> {
            InstitutionContact contact = new InstitutionContact();
            contact.setEmail("test" + idx + "@test.com");
            contact.setFirstName("first" + idx);
            contact.setLastName("last" + idx);
            contact.setPhoneNumber("phone" + idx);
            contact.setTitle("title" + idx);
            demoEntity.getContacts().add(contact);
        });
        mockMvc.perform(MockMvcRequestBuilders.put("/institutions/1")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.contacts", Matchers.hasSize(contactCount)));
    }

    /**
     * Test delete method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/institutions/1"))
               .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions/1"))
               .andExpect(status().isNotFound());
        mockMvc.perform(MockMvcRequestBuilders.delete("/institutions/1"))
               .andExpect(status().isNotFound());
    }

    /**
     * Test search method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void search() throws Exception {
        SearchResult<Institution> result = readSearchResult(entities, Institution.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions?sortColumn=id&sortOrder=ASC")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(entities, true));
        SearchResult<Institution> result1 = getSearchResult
                ("/institutions?pageNumber=1&pageSize=2&sortColumn=id&sortOrder=ASC", Institution.class);
        assertEquals(result.getTotal(), result1.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result1.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .skip(2)
                                .limit(2)
                                .map(Institution::getId)
                                .toArray(),
                result1.getEntities()
                       .stream()
                       .map(Institution::getId)
                       .toArray());
        SearchResult<Institution> result2 = getSearchResult
                ("/institutions?pageNumber=1&pageSize=2&sortColumn=id&sortOrder=DESC", Institution.class);
        assertEquals(result.getTotal(), result2.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result2.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .sorted(Comparator.comparing(IdentifiableEntity::getId)
                                                  .reversed())
                                .skip(2)
                                .limit(2)
                                .map(Institution::getId)
                                .toArray(),
                result2.getEntities()
                       .stream()
                       .map(Institution::getId)
                       .toArray());

        SearchResult<Institution> result3 = getSearchResult
                ("/institutions?pageNumber=1&pageSize=2&sortColumn=institutionName&sortOrder=DESC", Institution.class);
        assertEquals(result.getTotal(), result2.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result2.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .sorted(Comparator.comparing(Institution::getInstitutionName)
                                                  .reversed())
                                .skip(2)
                                .limit(2)
                                .map(Institution::getId)
                                .toArray(),
                result3.getEntities()
                       .stream()
                       .map(Institution::getId)
                       .toArray());
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions?institutionName=institutionName2")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(2));
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions?parentOrganization=parentOrganization3")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(3));
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions?location=streetAddress4")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(4));
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions?location=city5")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(5));
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions?email=institution6@test.com")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(6));
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions?description=description5")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(5));
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions?status=INACTIVE")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(2));
        mockMvc.perform(MockMvcRequestBuilders.get
                ("/institutions?pageNumber=0&pageSize=2&sortColumn=institutionName&sortOrder=DESC&institutionName" +
                        "=institutionName1&parentOrganization=parentOrganization1&location=city1&&email=institution1" +
                        "@test.com&&description" +
                "=description1&status=ACTIVE")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(1));
    }

    /**
     * Test generateAffiliationCode method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void generateAffiliationCode() throws Exception {
        String code = mockMvc.perform(MockMvcRequestBuilders.put("/institutions/1/generateAffiliationCode"))
                             .andExpect(status().isOk())
                             .andReturn()
                             .getResponse()
                             .getContentAsString();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<InstitutionAffiliationCode> query = cb.createQuery(InstitutionAffiliationCode.class);
        Root<InstitutionAffiliationCode> root = query.from(InstitutionAffiliationCode.class);
        query.select(root)
             .where(cb.equal(root.get("code"), objectMapper.readValue(code, String.class)));
        TypedQuery<InstitutionAffiliationCode> q = entityManager.createQuery(query);
        List<InstitutionAffiliationCode> list = q.getResultList();
        assertEquals(1, list.size());
        InstitutionAffiliationCode entity = list.get(0);
        assertEquals(1, entity.getInstitutionId());
        assertFalse(entity.isUsed());
        mockMvc.perform(MockMvcRequestBuilders.put("/institutions/999/generateAffiliationCode"))
               .andExpect(status().isNotFound());
    }

    /**
     * Test getInstitutionForAffiliationCode method.
     *
     * @throws Exception throws if any error happens.
     */
    @Transactional
    @Test
    public void getInstitutionForAffiliationCode() throws Exception {
        String code = createInstitutionAffiliationCode();
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions/affiliationCode/" + code)
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(sample));

        InstitutionAffiliationCode entity = getInstitutionAffiliationCode(code);
        assertEquals(1, entity.getInstitutionId());
        assertFalse(entity.isUsed());
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions/affiliationCode/notexist")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(""));
    }

    /**
     * Test useAffiliationCode method.
     *
     * @throws Exception throws if any error happens.
     */
    @Transactional
    @Test
    public void useAffiliationCode() throws Exception {
        String code = createInstitutionAffiliationCode();
        mockMvc.perform(MockMvcRequestBuilders.put("/institutions/useAffiliationCode?affiliationCode=" + code)
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
        InstitutionAffiliationCode entity = getInstitutionAffiliationCode(code);
        assertEquals(1, entity.getInstitutionId());
        assertTrue(entity.isUsed());
        mockMvc.perform(MockMvcRequestBuilders.put("/institutions/useAffiliationCode?affiliationCode=notexist")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
    }

    /**
     * Test getInstitutionSummary method.
     *
     * @throws Exception throws if any error happens.
     */
    @Transactional
    @Test
    public void getInstitutionSummary() throws Exception {
        Event event = entityManager.find(Event.class, 1L);
        event.setStartTime(sampleFutureDate);
        event.setEndTime(sampleFutureDate);
        entityManager.persist(event);
        String summary = readFile("institutionSummary.json");
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions/1/summary")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(summary));
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions/999/summary")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
    }

    /**
     * Create institution affiliation code.
     *
     * @throws Exception throws if any error happens.
     */
    private String createInstitutionAffiliationCode() throws Exception {
        String code = "code";
        InstitutionAffiliationCode entity = new InstitutionAffiliationCode();
        entity.setInstitutionId(1);
        entity.setCode(code);
        entityManager.persist(entity);
        return code;
    }

    /**
     * Get institution affiliation code by code.
     *
     * @param code the code
     * @throws Exception throws if any error happens.
     */
    private InstitutionAffiliationCode getInstitutionAffiliationCode(String code) throws Exception {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<InstitutionAffiliationCode> query = cb.createQuery(InstitutionAffiliationCode.class);
        Root<InstitutionAffiliationCode> root = query.from(InstitutionAffiliationCode.class);
        query.select(root)
             .where(cb.equal(root.get("code"), code));
        TypedQuery<InstitutionAffiliationCode> q = entityManager.createQuery(query);
        List<InstitutionAffiliationCode> list = q.getResultList();
        assertEquals(1, list.size());
        return list.get(0);
    }

}
