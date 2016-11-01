package com.livingprogress.mentorme.security;

import com.livingprogress.mentorme.BaseTest;
import com.livingprogress.mentorme.entities.NewPassword;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * The test cases for <code>SecurityConfig</code>
 */
public class SecutiryConfigTest extends BaseTest {

    /**
     * The token auth header name.
     */
    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    /**
     * The spring security filter chain.
     */
    @Autowired
    private Filter springSecurityFilterChain;

    /**
     * Setup test.
     *
     * @throws Exception throws if any error happen
     */
    @Before
    public void setupTest() throws Exception {
        super.setupTest();
        this.mockMvc = webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    /**
     * Test anonymous requests.
     * @throws Exception throws if any error happen
     */
    @Test
    public void anonymousTest() throws Exception {
        // test /lookups/**
        mockMvc.perform(
                MockMvcRequestBuilders.get("/lookups/userRoles")
                                      .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("userRoles.json")));

        // test /users/forgotPassword
        mockMvc.perform(MockMvcRequestBuilders.put("/users/forgotPassword?email=notexist@test.com"))
               .andExpect(status().isNotFound());

        NewPassword entity = new NewPassword();
        entity.setToken("notexist");
        entity.setNewPassword("newPassword");
        // test /users/updatePassword
        mockMvc.perform(MockMvcRequestBuilders.put("/users/updatePassword")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(entity)))
               .andExpect(status().isOk())
               .andExpect(content().string("false"));
    }

    /**
     * Test Unauthorized error without any auth header.
     * @throws Exception throws if any error happen
     */
    @Test
    public void unauthorizedTest1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isUnauthorized());
    }

    /**
     * Test Unauthorized error with wrong username.
     * @throws Exception throws if any error happen
     */
    @Test
    public void unauthorizedTest2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .with(httpBasic("wrong", "password")))
                                              .andExpect(status().isUnauthorized());
    }

    /**
     * Test Unauthorized error with wrong password.
     * @throws Exception throws if any error happen
     */
    @Test
    public void unauthorizedTest3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .with(httpBasic("test1", "wrong")))
               .andExpect(status().isUnauthorized());
    }

    /**
     * Test Unauthorized error with inactive user.
     * @throws Exception throws if any error happen
     */
    @Test
    public void unauthorizedTest4() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .with(httpBasic("test5", "password")))
               .andExpect(status().isUnauthorized());
    }

    /**
     * Test Unauthorized error with wrong JWT token.
     * @throws Exception throws if any error happen
     */
    @Test
    public void unauthorizedTest5() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, "wrong"))
               .andExpect(status().isUnauthorized());
    }

    /**
     * Test Unauthorized error with expired JWT token.
     * @throws Exception throws if any error happen
     */
    @Test
    public void unauthorizedTest6() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, readFile("expiredToken.txt")))
               .andExpect(status().isUnauthorized());
    }

    /**
     * Test basic auth.
     * @throws Exception throws if any error happen
     */
    @Test
    public void basicTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                                              .accept(MediaType.APPLICATION_JSON).with(httpBasic("test1", "password")))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("user1.json")));
    }

    /**
     * Test system admin JWT token.
     * @throws Exception throws if any error happen
     */
    @Test
    public void sytemAdminTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, readFile("sytemAdminToken.txt")))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("user1.json")));
    }

    /**
     * Test institution dmin JWT token.
     * @throws Exception throws if any error happen
     */
    @Test
    public void institutionAdminTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions/1")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, readFile("institutionAdminToken.txt")))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("institution1.json")));
    }

    /**
     * Test mentor JWT token.
     * @throws Exception throws if any error happen
     */
    @Test
    public void mentorTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors/3")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, readFile("mentorToken.txt")))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("mentor3.json")));
    }

    /**
     * Test mentee JWT token.
     * @throws Exception throws if any error happen
     */
    @Test
    public void menteeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees/4")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, readFile("menteeToken.txt")))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("mentee4.json")));
    }

    /**
     * Test forbidden error with mentor try to access mentee only endpoint.
     * @throws Exception throws if any error happen
     */
    @Test
    public void forbiddenTest1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/mentees/4")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, readFile("mentorToken.txt")))
               .andExpect(status().isForbidden());
    }

    /**
     * Test forbidden error with mentor try to access system admin only endpoint.
     * @throws Exception throws if any error happen
     */
    @Test
    public void forbiddenTest2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, readFile("mentorToken.txt")))
               .andExpect(status().isForbidden());
    }

    /**
     * Test basic login as system admin.
     * @throws Exception throws if any error happen
     */
    @Test
    public void loginAsSytemAdminTest() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .with(httpBasic("test1", "password")))
               .andExpect(status().isOk())
               .andReturn()
               .getResponse()
               .getContentAsString();
        String token = objectMapper.readValue(json, String.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, token))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("user1.json")));
    }

    /**
     * Test basic login as institution admin.
     * @throws Exception throws if any error happen
     */
    @Test
    public void loginAsInstitutionAdminTest() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                                                            .accept(MediaType.APPLICATION_JSON)
                                                            .with(httpBasic("test2", "password")))
                             .andExpect(status().isOk())
                             .andReturn()
                             .getResponse()
                             .getContentAsString();
        String token = objectMapper.readValue(json, String.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions/1")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, token))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("institution1.json")));
    }

    /**
     * Test basic login as mentor.
     * @throws Exception throws if any error happen
     */
    @Test
    public void loginAsMentorTest() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                                                            .accept(MediaType.APPLICATION_JSON)
                                                            .with(httpBasic("test3", "password")))
                             .andExpect(status().isOk())
                             .andReturn()
                             .getResponse()
                             .getContentAsString();
        String token = objectMapper.readValue(json, String.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/mentors/3")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, token))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("mentor3.json")));
    }


    /**
     * Test basic login as mentee.
     * @throws Exception throws if any error happen
     */
    @Test
    public void loginAsMenteeTest() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                                                            .accept(MediaType.APPLICATION_JSON)
                                                            .with(httpBasic("test4", "password")))
                             .andExpect(status().isOk())
                             .andReturn()
                             .getResponse()
                             .getContentAsString();
        String token = objectMapper.readValue(json, String.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/mentees/4")
                                              .accept(MediaType.APPLICATION_JSON)
                                              .header(AUTH_HEADER_NAME, token))
               .andExpect(status().isOk())
               .andExpect(content().json(readFile("mentee4.json")));
    }
}
