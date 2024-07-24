-- V4__Create_notification_settings_table.sql
CREATE TABLE notification_settings (
id VARCHAR(36) PRIMARY KEY,
user_id VARCHAR(36) NOT NULL,
email_notification BOOLEAN NOT NULL,
push_notification BOOLEAN NOT NULL,
sms_notification BOOLEAN NOT NULL
);