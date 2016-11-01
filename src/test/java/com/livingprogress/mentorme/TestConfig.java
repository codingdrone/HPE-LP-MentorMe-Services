package com.livingprogress.mentorme;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The test configuration.
 * It will format date time with string without consider timezone issue.
 */
@Configuration
public class TestConfig extends WebMvcConfigurerAdapter {
    /**
     * The datetime formt for <code>javax.persistence.TemporalType.TIMESTAMP</code>
     */
    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * The date format for <code>javax.persistence.TemporalType.DATE</code>
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * The valid formats for <code>java.util.Date</code>
     */
    private static final List<String> FORAMATS = Arrays.asList(DATE_FORMAT, DATETIME_FORMAT);

    /**
     * Configure message converters.
     *
     * @param converters the message converters.
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(converter());
    }

    /**
     * Create mapping jackson2 http message converter with custom date format.
     *
     * @return the mapping jackson2 http message converter.
     */
    @Bean
    @Primary
    MappingJackson2HttpMessageConverter converter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(buildObjectMapper());
        return jsonConverter;
    }

    /**
     * Build object mapper with custom date format.
     * @return the custom object mapper.
     */
    public static ObjectMapper buildObjectMapper(){
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .simpleDateFormat(DATETIME_FORMAT)
                .indentOutput(true);
        builder.deserializerByType(Date.class, new JsonDeserializer<Date>() {
            /**
             * Custom date deserialize.
             * @param jsonParser the json parser
             * @param deserializationContext the deserialization context
             * @return the parsed date object.
             * @throws IOException throws if error to parse date string
             */
            @Override
            public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws
                    IOException {
                String date = jsonParser.getText();
                for (String format : FORAMATS) {
                    try {
                        return new SimpleDateFormat(format).parse(date);
                    } catch (ParseException e) {
                        // do nothing
                    }
                }
                throw new IllegalArgumentException("Error to parse date for " + date);
            }
        });
        return builder.build();
    }
}
