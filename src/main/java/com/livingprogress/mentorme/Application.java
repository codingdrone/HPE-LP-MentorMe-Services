package com.livingprogress.mentorme;

import com.livingprogress.mentorme.utils.Helper;
import org.apache.log4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.util.Map;
import java.util.UUID;

/**
 * The main application.
 */
@SpringBootApplication
public class Application {

    /**
     * The request id listener
     */
    public class RequestIdListener implements ServletRequestListener {
        /**
         * Handle request initialized event.
         *
         * @param event the servlet request event.
         */
        public void requestInitialized(ServletRequestEvent event) {
            MDC.put("RequestId", UUID.randomUUID());
        }

        /**
         * Handle request destroyed event.
         *
         * @param event the servlet request event.
         */
        public void requestDestroyed(ServletRequestEvent event) {
            MDC.clear();
        }
    }

    /**
     * Create request id listener bean.
     *
     * @return the request id listener bean.
     */
    @Bean
    public RequestIdListener executorListener() {
        return new RequestIdListener();
    }

    /**
     * Custom dispatcher servlet bean.
     *
     * @return the dispatcher servlet bean.
     */
    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet ds = new DispatcherServlet();
        // handle 404 error
        ds.setThrowExceptionIfNoHandlerFound(true);
        return ds;
    }

    /**
     * The error attributes
     *
     * @return the custom error attributes.
     */
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            /**
             * Format error attributes with code and message key only.
             * @param requestAttributes the request attributes.
             * @param includeStackTrace the include stack trace flag.
             * @return the error attributes with code and message key only.
             */
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean
                    includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
                if (!errorAttributes.containsKey("code") || !errorAttributes.containsKey("message") ||
                        errorAttributes.size() != 2) {
                    Throwable error = getError(requestAttributes);
                    Object status = errorAttributes.getOrDefault("status", 500);
                    Object message = errorAttributes.getOrDefault("message", error != null && !Helper.isNullOrEmpty(error.getMessage()) ? error.getMessage() :
                            "Unexpected error");
                    errorAttributes.clear();
                    errorAttributes.put("code", status);
                    errorAttributes.put("message", message);
                }
                return errorAttributes;
            }
        };
    }

    /**
     * The main entry point of the application.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
