package com.test.pavedroad.intcollabsnotification.core.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.test.pavedroad.intcollabsnotification.core.MongoProperties;
import lombok.RequiredArgsConstructor;
import org.bson.UuidRepresentation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "service.index.iks", havingValue = "false")
public class LocalConfig {
    private final MongoProperties mongoProperties;

    /***
     * Method to create mongo client settings builder
     * @return
     */
    @Bean
    MongoClientSettings.Builder createMongoSettingsBuilder() {
        ConnectionString connectionString = new ConnectionString(mongoProperties.getUri());
        /* JobRunr stores/retrieves UUID values, therefore we need to explicitly specify which representation to use */
        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder()
                .applyToSslSettings(builder -> {
                    builder.enabled(true);
                    builder.invalidHostNameAllowed(true);
                })
                .applyConnectionString(connectionString).uuidRepresentation(UuidRepresentation.JAVA_LEGACY);
        return settingsBuilder;
    }
}
