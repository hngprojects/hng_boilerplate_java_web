package hng_java_boilerplate.notificationSettings.service;

import hng_java_boilerplate.notificationSettings.entity.NotificationSettings;
import hng_java_boilerplate.notificationSettings.repository.NotificationSettingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;


import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationSettingsServiceTest {
    @Mock
    private NotificationSettingsRepository repository;
    @InjectMocks
    private NotificationSettingsService service;

    private NotificationSettings settings;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        UUID id = UUID.randomUUID();
        settings = new NotificationSettings();
        settings.setId(String.valueOf(id));
        settings.setUserId("myname@gmail.com");
        settings.setEmailNotification(true);
        settings.setPushNotification(false);
        settings.setSmsNotification(true);


    }
    @Test
    void testSaveNotificationSettings(){
        when(repository.save(any(NotificationSettings.class))).thenReturn(settings);

        NotificationSettings savedSettings = service.save(settings);

        assertEquals(settings.getId(), savedSettings.getId());
        assertEquals(settings.getUserId(), savedSettings.getUserId());
        Mockito.verify(repository, times(1)).save(settings);
    }
    @Test
    void testGetNotificationSettings() {
        when(repository.findByUserId("myname@gmail.com")).thenReturn(Optional.of(settings));

        Optional<NotificationSettings> foundSettings = service.findByUserId("myname@gmail.com");

        assertEquals(settings.getId(), foundSettings.get().getId());
        assertEquals(settings.getUserId(), foundSettings.get().getUserId());
        Mockito.verify(repository, times(1)).findByUserId("myname@gmail.com");
    }
}
