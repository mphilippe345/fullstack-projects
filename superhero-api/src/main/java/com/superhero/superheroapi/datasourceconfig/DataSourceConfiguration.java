package com.superhero.superheroapi.datasourceconfig;

import com.google.gson.Gson;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

/**
 * Configuration for connecting to local and remote databases
 */
@Configuration
public class DataSourceConfiguration {

  private final Gson gson = new Gson();

  /**
   * Builds a datasource bean for our local database
   *
   * @return a Datasource containing our local database connection info
   */
  @Bean
  @Profile("default")
  @Primary
  public DataSource localDataSource() {
    return DataSourceBuilder.create()
        .url("jdbc:postgresql://localhost:5432/SuperheroLocal")
        .username("postgres")
        .password("root")
        .build();
  }

  /**
   * Builds a datasource bean for our remote database
   *
   * @return a DataSource containing our remote database connection info
   */
  @Bean
  @Profile("remote")
  public DataSource remoteDataSource() {
    RdsDatabaseSecret secret = getSecret();
    return DataSourceBuilder.create()
        .driverClassName("org.postgresql.Driver")
        .url("jdbc:" + secret.getEngine() + "ql://" + secret.getHost() + ":" + secret.getPort()
            + "/" + secret.getDbname())
        .username(secret.getUsername())
        .password(secret.getPassword())
        .build();
  }

  /**
   * Temporarily connects to AWS to retrieve a secret value
   *
   * @return Secret value mapped to our RdsDatabaseSecret model class
   */
  private RdsDatabaseSecret getSecret() {

    String secretName = "remote/ComicsCornerApp/Postgresql";
    Region region = Region.of("us-east-2");

    GetSecretValueResponse getSecretValueResponse;
    try (SecretsManagerClient client = SecretsManagerClient.builder()
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .region(region)
        .build()) {

      GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
          .secretId(secretName)
          .build();

      getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
    }

    String secret = getSecretValueResponse.secretString();
    return gson.fromJson(secret, RdsDatabaseSecret.class);
  }
}
