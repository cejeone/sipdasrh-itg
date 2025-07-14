package com.sipdasrh.awas;

import com.sipdasrh.awas.config.AsyncSyncConfiguration;
import com.sipdasrh.awas.config.EmbeddedRedis;
import com.sipdasrh.awas.config.EmbeddedSQL;
import com.sipdasrh.awas.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { ServiceAwasApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
public @interface IntegrationTest {
}
