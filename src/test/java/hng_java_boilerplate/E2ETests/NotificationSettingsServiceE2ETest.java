package hng_java_boilerplate.E2ETests;


import hng_java_boilerplate.notificationSettings.entity.NotificationSettings;
import hng_java_boilerplate.notificationSettings.repository.NotificationSettingsRepository;
import hng_java_boilerplate.notificationSettings.service.NotificationSettingsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class NotificationSettingsServiceE2ETest {

    @Autowired
    private NotificationSettingsService service;

    @Autowired
    private NotificationSettingsRepository repository;

    @Test
    void testCreateAndRetrieveNotificationSettings() {
        NotificationSettings settings = new NotificationSettings();
        settings.setUserId("user@example.com");
        settings.setEmailNotification(true);
        settings.setPushNotification(false);
        settings.setSmsNotification(true);

        service.save(settings);

        Optional<NotificationSettings> retrievedSettings = repository.findByUserId("user@example.com");
        assertThat(retrievedSettings).isPresent();
        assertThat(retrievedSettings.get().getEmailNotification()).isTrue();
        assertThat(retrievedSettings.get().getPushNotification()).isFalse();
        assertThat(retrievedSettings.get().getSmsNotification()).isTrue();
    }
}
