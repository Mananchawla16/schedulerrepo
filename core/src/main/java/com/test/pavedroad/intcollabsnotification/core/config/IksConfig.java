package com.test.pavedroad.intcollabsnotification.core.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ClusterType;
import com.test.pavedroad.intcollabsnotification.core.MongoProperties;
import lombok.RequiredArgsConstructor;
import org.bson.UuidRepresentation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "service.index.iks", havingValue = "true")
public class IksConfig {
    private final MongoProperties mongoProperties;

    /***
     * Method to create mongo client settings builder
     * @return
     */
    @Bean
    MongoClientSettings.Builder createMongoSettingsBuilder() {
        MongoCredential credential = MongoCredential.createCredential(mongoProperties.getUsername(),
                mongoProperties.getDatabase(), mongoProperties.getPassword().toCharArray());
        ClusterSettings.Builder clusterSettingsBuilder = ClusterSettings.builder().hosts(Collections.singletonList(
                new ServerAddress(mongoProperties.getHostname(), mongoProperties.getPort())));
        clusterSettingsBuilder.requiredClusterType(ClusterType.REPLICA_SET).mode(ClusterConnectionMode.MULTIPLE);

        ClusterSettings clusterSettings = clusterSettingsBuilder.build();
        /* JobRunr stores/retrieves UUID values, therefore we need to explicitly specify which representation to use */
        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder().credential(credential)
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                .applyToClusterSettings(builder -> builder.applySettings(clusterSettings))
                .readPreference(ReadPreference.secondaryPreferred()).retryWrites(false);

        return settingsBuilder;
    }
}
