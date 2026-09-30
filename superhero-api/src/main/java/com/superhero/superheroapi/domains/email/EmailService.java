package com.superhero.superheroapi.domains.email;

import com.google.gson.Gson;
import com.superhero.superheroapi.domains.purchase.Purchase;
import jakarta.mail.internet.MimeMessage;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

/**
 * Service class that handles the emails that are sent out.
 */
@Configuration
public class EmailService {

  private final TemplateEngine templateEngine;
  private final Gson gson = new Gson();

  @Autowired
  public EmailService(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  /**
   * Puts together the purchase confirmation email template and sends to the email of the recipient
   * provided.
   *
   * @param email       email of the recipient.
   * @param newPurchase purchase that was just made that contains all the information that will be
   *                    displayed.
   * @param subTotal    subtotal of the items before tax and shipping. For display purposes.
   */
  public void sendPurchaseConfirmationEmail(String email, Purchase newPurchase,
      BigDecimal subTotal) {
    MimeMessage message = javaMailSender().createMimeMessage();
    String from = "no-reply@comicscorner.publicvm.com";
    try {
      Context context = new Context();
      context.setVariable("purchase", newPurchase);
      context.setVariable("subtotal", subTotal);
      String htmlContent = templateEngine.process("emailreceipt", context);

      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(from);
      helper.setTo(email);
      helper.setSubject("Purchase Confirmation");
      helper.setText(htmlContent, true);

      javaMailSender().send(message);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    SmtpSecret secret = getSecret();

    String host = secret.getHost();
    if (host != null) {
      mailSender.setHost(host);
    }

    String portStr = secret.getPort();
    if (portStr != null) {
      mailSender.setPort(Integer.parseInt(portStr));
    }

    String username = secret.getUsername();
    if (username != null) {
      mailSender.setUsername(username);
    }

    String password = secret.getPassword();
    if (password != null) {
      mailSender.setPassword(password);
    }

    return mailSender;
  }

  /**
   * Temporarily connects to AWS to retrieve a secret value
   *
   * @return Secret value mapped to our SmtpSecret model class
   */
  private SmtpSecret getSecret() {

    String secretName = "smtpEmailerCreds/ComicsCornerApp";
    Region region = Region.of("us-east-2");

    GetSecretValueResponse getSecretValueResponse;
    try (
        SecretsManagerClient client = SecretsManagerClient.builder()
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .region(region)
            .build()
    ) {
      GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
          .secretId(secretName)
          .build();

      getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
    }

    String secret = getSecretValueResponse.secretString();
    return gson.fromJson(secret, SmtpSecret.class);
  }
}
