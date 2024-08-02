package hng_java_boilerplate.notification.services;

import hng_java_boilerplate.notification.dto.request.MarkRead;
import hng_java_boilerplate.notification.dto.response.NotificationDto;
import hng_java_boilerplate.notification.dto.response.NotificationDtoRes;
import hng_java_boilerplate.notification.dto.response.NotificationResponse;
import hng_java_boilerplate.notification.models.Notification;
import hng_java_boilerplate.notification.repositories.NotificationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private NotificationService underTest;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setNotificationId(UUID.randomUUID());
        notification.setMessage("hi");
        notification.setUserId("user-id");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void shouldGetAllNotifications() {
        when(notificationRepository.findAll()).thenReturn(List.of(notification));

        NotificationResponse res = underTest.getAllNotifications();
        verify(notificationRepository, times(1)).findAll();

        assertThat(res.status()).isEqualTo("success");
        assertThat(res.message()).isEqualTo("Notifications retrieved successfully");
        assertThat(res.status_code()).isEqualTo(200);
        assertThat(res.data().total_notification_count()).isEqualTo(1);
    }

    @Test
    void ShouldGetUnreadNotifications() {
        User user = new User();
        user.setId("user-id");

        when(notificationRepository.findByUserIdAndIsRead("user-id", true)).thenReturn(List.of(notification));
        when(userService.getLoggedInUser()).thenReturn(user);


        NotificationResponse res = underTest.getUnreadNotifications(true);
        verify(notificationRepository, times(1)).findByUserIdAndIsRead("user-id", true);

        assertThat(res.status()).isEqualTo("success");
        assertThat(res.message()).isEqualTo("Unread notifications retrieved successfully");
        assertThat(res.status_code()).isEqualTo(200);
        assertThat(res.data().total_notification_count()).isEqualTo(1);
    }

    @Test
    void ShouldMarkAsRead() {
        when(notificationRepository.findById(notification.getNotificationId())).thenReturn(Optional.of(notification));
        MarkRead read = new MarkRead();
        read.set_read(true);

        NotificationDtoRes res = underTest.markAsRead(notification.getNotificationId(), read);

        verify(notificationRepository, times(1)).saveAndFlush(any(Notification.class));
        assertThat(res.data().is_read()).isTrue();
    }

    @Test
    void shouldMarkAllAsRead() {
        when(notificationRepository.findByIsRead(false)).thenReturn(List.of(notification));

        NotificationResponse res = underTest.markAllAsRead();
        assertThat(res.status()).isEqualTo("success");

    }
}