-- V13__Create_notification_table.sql
CREATE TABLE notifications (
    notification_id UUID PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    message VARCHAR(255) NOT NULL,
    is_read BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL -- Using TIMESTAMP for LocalDateTime
);
