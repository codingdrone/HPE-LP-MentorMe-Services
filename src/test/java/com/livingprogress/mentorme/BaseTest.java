package com.livingprogress.mentorme;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livingprogress.mentorme.entities.IdentifiableEntity;
import com.livingprogress.mentorme.entities.SearchResult;
import com.livingprogress.mentorme.services.LookupService;
import com.livingprogress.mentorme.utils.Helper;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * The base test class for all tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes ={Application.class})
//@SpringApplicationConfiguration(classes ={Application.class, TestConfig.class})
@WebAppConfiguration
@EnableWebSecurity
@TestPropertySource(locations = "classpath:test.properties")
public abstract class BaseTest {
    /**
     * The sql folder.
     */
    private static final String SQL_FOLDER = "sqls";

    /**
     * The sql names list.
     */
    private static final List<String> SQLS = Arrays.asList("clear.sql", "testdata.sql");

    /**
     * The lookup service used to perform operations.
     */
    @Autowired
    protected LookupService lookupService;

    /**
     * The entity manager.
     */
    @Autowired
    protected EntityManager entityManager;

    /**
     * The platform transaction manager.
     */
    @Autowired
    private PlatformTransactionManager txManager;

    /**
     * The email server port.
     */
    @Value("${spring.mail.port}")
    private int port;

    /**
     * The from email address.
     */
    @Value("${mail.from}")
    private String fromAddress;

    /**
     * The web application context.
     */
    @Autowired
    protected WebApplicationContext context;

    /**
     * The object mapper.
     */
    protected ObjectMapper objectMapper = TestConfig.buildObjectMapper();

    /**
     * The mock mvc object.
     */
    protected MockMvc mockMvc;

    /**
     * The wiser email server.
     */
    protected Wiser wiser;

    /**
     * The sample future date.
     */
    protected Date sampleFutureDate;

    /**
     * Setup test.
     *
     * @throws Exception throws if any error happen
     */
    @Before
    public void setupTest() throws Exception {
        wiser = new Wiser(port);
        wiser.start();
        this.mockMvc = webAppContextSetup(context).build();
        for (String sql : SQLS) {
            runSQL(sql);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        sampleFutureDate = calendar.getTime();
    }

    /**
     * Tear down test.
     *
     * @throws Exception throws if any error happen
     */
    @After
    public void tearDown() throws Exception {
        wiser.stop();
    }

    /**
     * Read file.
     *
     * @param name the file name.
     * @return the file content.
     * @throws IOException throws if any io error happen
     */
    protected static String readFile(String name) throws IOException {
        File file = new File(BaseTest.class.getResource("/data/" + name).getFile());
        return FileUtils.readFileToString(file, Helper.UTF8);
    }

    /**
     * Run sql.
     *
     * @param name the sql file name
     * @throws IOException throws if any io error happen
     */
    @Transactional
    protected void runSQL(String name) throws IOException {
        Stream<String> lines = FileUtils.readLines(new File(SQL_FOLDER, name), Helper.UTF8).stream().filter(c -> !Helper.isNullOrEmpty(c) && !c.trim().startsWith("-"));
        new TransactionTemplate(txManager).execute(status -> {
            lines.forEach(sql -> entityManager.createNativeQuery(sql).executeUpdate());
            return null;
        });
    }

    /**
     * Verify email.
     *
     * @param subject the email subject contains string.
     * @param body the email body contains string.
     * @param toAddress the to email address
     * @throws Exception throws if any error happen
     */
    protected void verifyEmail(String subject, String body, String toAddress) throws Exception {
        assertEquals(1, wiser.getMessages().size());
        WiserMessage message = wiser.getMessages().get(0);
        assertEquals(fromAddress, message.getEnvelopeSender());
        assertEquals(toAddress, message.getEnvelopeReceiver());
        assertTrue(message.getMimeMessage().getSubject().contains(subject));
        assertTrue(((String) message.getMimeMessage().getContent()).contains(body));
    }

    /**
     * Read search result from json.
     *
     * @param json the json.
     * @param target the target class
     * @param <T> the class name
     * @return the search result.
     * @throws Exception throws if any error happen
     */
    protected <T> SearchResult<T> readSearchResult(String json, Class<T> target) throws Exception {
        JavaType type = objectMapper.getTypeFactory().constructParametrizedType(SearchResult.class, SearchResult.class, target);
        return objectMapper.readValue(json, type);
    }

    /**
     * Get search result with reading content from url.
     *
     * @param url the url.
     * @param target the target class
     * @param <T> the class name
     * @return the search result.
     * @throws Exception throws if any error happen
     */
    protected <T> SearchResult<T> getSearchResult(String url, Class<T> target) throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return readSearchResult(json, target);
    }

    /**
     * Get total pages.
     *
     * @param total the total number.
     * @param pageSize the page size.
     * @return the total pages.
     * @throws Exception throws if any error happen
     */
    protected static int getTotalPages(long total, long pageSize) throws Exception {
        return (int) ((total + pageSize - 1) / pageSize);
    }

    /**
     * Check identifiable entity.
     *
     * @param entity the entity
     * @param <T> the entity class
     */
    protected static <T extends IdentifiableEntity> void checkEntity(T entity) {
        assertEquals(0, entity.getId());
    }

    /**
     * Verify entity.
     *
     * @param oldEntity the old entity
     * @param newEntity the new entity
     * @param <T> the entity class
     */
    protected static <T extends IdentifiableEntity> void verifyEntity(T oldEntity, T newEntity) {
        long id = newEntity.getId();
        assertTrue(id > 0);
        oldEntity.setId(id);
    }

    /**
     * Check entities.
     *
     * @param entities the entities
     * @param <T> the entity class
     */
    protected static <T extends IdentifiableEntity> void checkEntities(List<T> entities) throws Exception {
        assertTrue(entities.size() > 0);
        entities.forEach(BaseTest::checkEntity);
    }

    /**
     * Verify entities.
     *
     * @param oldEntities the old entities
     * @param newEntities the new entities
     * @param <T> the entity class
     */
    protected static <T extends IdentifiableEntity> void verifyEntities(List<T> oldEntities, List<T> newEntities) throws Exception {
        assertTrue(oldEntities.size() > 0);
        assertEquals(oldEntities.size(), newEntities.size());
        IntStream.range(0, oldEntities.size()).forEach(idx -> {
            long id = newEntities.get(idx).getId();
            assertTrue(id > 0);
            oldEntities.get(idx).setId(id);
        });
    }
}
