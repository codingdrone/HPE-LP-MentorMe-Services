package com.livingprogress.mentorme.controllers;

import com.livingprogress.mentorme.BaseTest;
import com.livingprogress.mentorme.entities.ForgotPassword;
import com.livingprogress.mentorme.entities.IdentifiableEntity;
import com.livingprogress.mentorme.entities.NewPassword;
import com.livingprogress.mentorme.entities.SearchResult;
import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.entities.UserRole;
import com.livingprogress.mentorme.services.LookupService;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

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
 * The test cases for <code>UserController</code>
 */
public class UserControllerTest extends BaseTest {
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
        sample = readFile("user1.json");
        demo = readFile("demo-user.json");
        entities = readFile("users.json");
    }

    /**
     * Test get method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
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
        User demoEntity = objectMapper.readValue(demo, User.class);
        assertNotNull(demoEntity.getPassword());
        assertNull(demoEntity.getCreatedOn());
        demoEntity.setPassword(null);
        String res = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(demo))
                            .andExpect(status().isCreated())
                            .andExpect(jsonPath("$.password").doesNotExist())
                            .andExpect(jsonPath("$.id").isNumber())
                            .andExpect(jsonPath("$.createdOn").exists())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        User result = objectMapper.readValue(res, User.class);
        demoEntity.setId(result.getId());
        demoEntity.setCreatedOn(result.getCreatedOn());
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
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(sample))
               .andExpect(status().isOk())
               .andExpect(content().json(sample));
        User sampleEntity = objectMapper.readValue(sample, User.class);
        User demoEntity = objectMapper.readValue(demo, User.class);
        BeanUtils.copyProperties(demoEntity, sampleEntity);
        // try to update created on
        demoEntity.setCreatedOn(sampleFutureDate);
        demoEntity.setId(1);
        String json = objectMapper.writeValueAsString(demoEntity);
        String res = mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(json))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.password").doesNotExist())
                            .andExpect(jsonPath("$.id").isNumber())
                            .andExpect(jsonPath("$.createdOn").exists())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        User result = objectMapper.readValue(res, User.class);
        // will not update created on during updating
        assertNotEquals(sampleFutureDate, result.getCreatedOn());
        demoEntity.setCreatedOn(result.getCreatedOn());
        assertEquals(objectMapper.writeValueAsString(demoEntity), objectMapper.writeValueAsString(result));
        // test nested properties
        demoEntity.setRoles(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.password").doesNotExist())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.roles").doesNotExist());
        // second null updates
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.password").doesNotExist())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.roles", Matchers.hasSize(0)));
        // update with new nested values
        List<UserRole> roles = lookupService.getUserRoles();
        demoEntity.setRoles(roles);
        assertTrue(roles.size() > 0);
        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(demoEntity)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.password").doesNotExist())
               .andExpect(jsonPath("$.id").isNumber())
               .andExpect(jsonPath("$.createdOn").exists())
               .andExpect(jsonPath("$.roles", Matchers.hasSize(roles.size())));
    }

    /**
     * Test delete method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
               .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
               .andExpect(status().isNotFound());
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
               .andExpect(status().isNotFound());
    }

    /**
     * Test search method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void search() throws Exception {
        SearchResult<User> result = readSearchResult(entities, User.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/users?sortColumn=id&sortOrder=ASC")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(entities, true));
        SearchResult<User> result1 = getSearchResult("/users?pageNumber=1&pageSize=2&sortColumn=id&sortOrder=ASC",
                User.class);
        assertEquals(result.getTotal(), result1.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result1.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .skip(2)
                                .limit(2)
                                .map(User::getId)
                                .toArray(),
                result1.getEntities()
                       .stream()
                       .map(User::getId)
                       .toArray());
        SearchResult<User> result2 = getSearchResult("/users?pageNumber=1&pageSize=2&sortColumn=id&sortOrder=DESC",
                User.class);
        assertEquals(result.getTotal(), result2.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result2.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .sorted(Comparator.comparing(IdentifiableEntity::getId)
                                                  .reversed())
                                .skip(2)
                                .limit(2)
                                .map(User::getId)
                                .toArray(),
                result2.getEntities()
                       .stream()
                       .map(User::getId)
                       .toArray());

        SearchResult<User> result3 = getSearchResult
                ("/users?pageNumber=1&pageSize=2&sortColumn=username&sortOrder=DESC", User.class);
        assertEquals(result.getTotal(), result2.getTotal());
        assertEquals(getTotalPages(result.getTotal(), 2), result2.getTotalPages());
        assertArrayEquals(result.getEntities()
                                .stream()
                                .sorted(Comparator.comparing(User::getUsername)
                                                  .reversed())
                                .skip(2)
                                .limit(2)
                                .map(User::getId)
                                .toArray(),
                result3.getEntities()
                       .stream()
                       .map(User::getId)
                       .toArray());
        mockMvc.perform(MockMvcRequestBuilders.get("/users?name=firstname11")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(11));
        mockMvc.perform(MockMvcRequestBuilders.get("/users?name=lastname13")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(13));
        mockMvc.perform(MockMvcRequestBuilders.get("/users?email=email3@test.com")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(3));
        mockMvc.perform(MockMvcRequestBuilders.get("/users?username=test4")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(4));
        mockMvc.perform(MockMvcRequestBuilders.get("/users?role.id=2")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(2));
        mockMvc.perform(MockMvcRequestBuilders.get
                ("/users?pageNumber=0&pageSize=2&sortColumn=username&sortOrder=DESC&name=firstname1&email=email1@test" +
                        ".com&username=test1&role.id=1")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.total").value(1))
               .andExpect(jsonPath("$.totalPages").value(1))
               .andExpect(jsonPath("$.entities", Matchers.hasSize(1)))
               .andExpect(jsonPath("$.entities[0].id").value(1));
    }

    /**
     * Test forgotPassword method.
     *
     * @throws Exception throws if any error happens.
     */
    @Test
    public void forgotPassword() throws Exception {
        String email = "email1@test.com";
        mockMvc.perform(MockMvcRequestBuilders.put("/users/forgotPassword?email=" + email))
               .andExpect(status().isOk());
        List<ForgotPassword> result = getForgotPasswords();
        assertEquals(1, result.size());
        ForgotPassword forgotPassword = result.get(0);
        assertEquals(1, forgotPassword.getUserId());
        assertNotNull(forgotPassword.getExpiredOn());
        verifyEmail("Reset password", forgotPassword.getToken(), email);
        mockMvc.perform(MockMvcRequestBuilders.put("/users/forgotPassword?email=" + email))
               .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.put("/users/forgotPassword?email=" + email))
               .andExpect(status().isForbidden());
        mockMvc.perform(MockMvcRequestBuilders.put("/users/forgotPassword?email=notexist@test.com"))
               .andExpect(status().isNotFound());
    }

    /**
     * Test updatePassword method.
     *
     * @throws Exception throws if any error happens.
     */
    @Transactional
    @Test
    public void updatePassword() throws Exception {
        String token = "token";
        String newPassword = "newPassword";
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setUserId(1);
        forgotPassword.setToken(token);
        forgotPassword.setExpiredOn(sampleFutureDate);
        entityManager.persist(forgotPassword);
        assertEquals(1, getForgotPasswords().size());
        mockMvc.perform(MockMvcRequestBuilders.put("/users/forgotPassword?email=email1@test.com"))
               .andExpect(status().isOk());
        assertEquals(2, getForgotPasswords().size());
        NewPassword entity = new NewPassword();
        entity.setToken(token);
        entity.setNewPassword(newPassword);
        mockMvc.perform(MockMvcRequestBuilders.put("/users/updatePassword")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(entity)))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));
        assertEquals(0, getForgotPasswords().size());
        mockMvc.perform(MockMvcRequestBuilders.put("/users/updatePassword")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(entity)))
               .andExpect(status().isOk())
               .andExpect(content().string("false"));
    }

    /**
     * Get forgot passwords.
     *
     * @throws Exception throws if any error happens.
     */
    private List<ForgotPassword> getForgotPasswords() throws Exception {
        return entityManager.createQuery("from ForgotPassword", ForgotPassword.class)
                            .getResultList();
    }
}
