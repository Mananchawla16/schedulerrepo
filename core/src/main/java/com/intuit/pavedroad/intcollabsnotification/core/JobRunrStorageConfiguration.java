package com.intuit.pavedroad.intcollabsnotification.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import com.intuit.pavedroad.intcollabsnotification.core.config.DocDbSslContextBuilder;
import com.intuit.pavedroad.intcollabsnotification.core.config.MongoProperties;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ClusterType;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SslSettings;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.UuidRepresentation;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.spring.autoconfigure.JobRunrProperties;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.StorageProviderUtils;
import org.jobrunr.storage.nosql.documentdb.AmazonDocumentDBStorageProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ComponentScan(basePackageClasses = JobRunrStorageConfiguration.class)
@Slf4j
@RequiredArgsConstructor
public class JobRunrStorageConfiguration {

    private final Environment environment;
    private final MongoProperties mongoProperties;
    private final MongoCustomConversions mongoCustomConversions;

    @Bean
    public MongoClient mongoClient() {
        String[] activeProfiles = environment.getActiveProfiles();

        MongoCredential credential = MongoCredential.createCredential(mongoProperties.getUsername(),
                mongoProperties.getDatabase(), mongoProperties.getPassword().toCharArray());
        ClusterSettings.Builder clusterSettingsBuilder = ClusterSettings
                .builder()
                .hosts(Collections.singletonList(new ServerAddress(mongoProperties.getHostname(), mongoProperties.getPort())));

        /* Set requiredClusterType to REPLICA_SET if not in local environment */
        if (Arrays.stream(activeProfiles).noneMatch(x -> StringUtils.equalsIgnoreCase("LOCAL", x))) {
            clusterSettingsBuilder
                    .requiredClusterType(ClusterType.REPLICA_SET);
        }
        ClusterSettings clusterSettings = clusterSettingsBuilder.build();
        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder()
                .credential(credential)
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                .applyToClusterSettings(builder -> builder.applySettings(clusterSettings))
                .readPreference(ReadPreference.secondaryPreferred())
                .retryWrites(false);

        /* Apply sslSettings if not in local environment */
        if (Arrays.stream(activeProfiles).noneMatch(x -> StringUtils.equalsIgnoreCase("LOCAL", x))) {
            SslSettings sslSettings = SslSettings.builder()
                    .context(new DocDbSslContextBuilder().buildSSLContext())
                    .enabled(true)
                    .invalidHostNameAllowed(true)
                    .build();
            settingsBuilder.applyToSslSettings(builder -> builder.applySettings(sslSettings));
        }

        /* Apply connectionPool settings if present */
        Integer minConnectionPoolSize = mongoProperties.getMinPoolSize();
        Integer maxConnectionPoolSize = mongoProperties.getMaxPoolSize();
        if (ObjectUtils.allNotNull(minConnectionPoolSize, maxConnectionPoolSize)) {
            ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
                    .minSize(minConnectionPoolSize)
                    .maxSize(maxConnectionPoolSize)
                    .maxWaitTime(10, TimeUnit.MINUTES)
                    .maxConnectionLifeTime(30, TimeUnit.MINUTES)
                    .maxConnectionIdleTime( 60000, TimeUnit.MILLISECONDS)
                    .build();
            settingsBuilder.applyToConnectionPoolSettings(builder -> builder.applySettings(connectionPoolSettings));
        }

        MongoClientSettings settings = settingsBuilder.build();
        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient(), mongoProperties.getDatabase());
        MappingMongoConverter mappingMongoConverter = (MappingMongoConverter) mongoTemplate.getConverter();
        mappingMongoConverter.setCustomConversions(mongoCustomConversions);
        mappingMongoConverter.afterPropertiesSet();
        return mongoTemplate;
    }

    @Bean
    @Primary
    public StorageProvider documentDBStorageProvider(MongoClient mongoClient, JobMapper jobMapper, JobRunrProperties properties) {
        String databaseName = properties.getDatabase().getDatabaseName();
        String tablePrefix = properties.getDatabase().getTablePrefix();
        StorageProviderUtils.DatabaseOptions databaseOptions = properties.getDatabase()
                .isSkipCreate() ? StorageProviderUtils.DatabaseOptions.SKIP_CREATE : StorageProviderUtils.DatabaseOptions.CREATE;
        AmazonDocumentDBStorageProvider amazonDocumentDBStorageProvider =
                new AmazonDocumentDBStorageProvider(mongoClient, databaseName, tablePrefix, databaseOptions);
        amazonDocumentDBStorageProvider.setJobMapper(jobMapper);
        return amazonDocumentDBStorageProvider;
    }

}
