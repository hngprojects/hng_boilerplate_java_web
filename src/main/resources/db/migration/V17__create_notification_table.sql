-- V17__Create_notification_and_notification_settings_tables.sql
-- Creating the notifications table with a foreign key reference to notification_settings
CREATE TABLE notifications (
    notification_id UUID PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    message VARCHAR(255) NOT NULL,
    is_read BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    notification_settings_id INT UNIQUE,
    CONSTRAINT fk_notification_settings
        FOREIGN KEY(notification_settings_id)
        REFERENCES notification_settings(id)
        ON DELETE CASCADE
);
-- Creating the notification_settings table
CREATE TABLE notification_settings (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    mobile_push_notifications BOOLEAN DEFAULT FALSE,
    email_notification_activity_in_workspace BOOLEAN DEFAULT FALSE,
    email_notification_always_send_email_notifications BOOLEAN DEFAULT FALSE,
    email_notification_email_digest BOOLEAN DEFAULT FALSE,
    email_notification_announcement_and_update_emails BOOLEAN DEFAULT FALSE,
    slack_notifications_activity_on_your_workspace BOOLEAN DEFAULT FALSE,
    slack_notifications_always_send_email_notifications BOOLEAN DEFAULT FALSE,
    slack_notifications_announcement_and_update_emails BOOLEAN DEFAULT FALSE
);



