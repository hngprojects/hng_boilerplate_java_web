-- V13__Create_notification_table.sql
CREATE TABLE notifications (
    user_id UUID PRIMARY KEY,
    message VARCHAR(255) NOT NULL,
    is_read BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL -- Using TIMESTAMP for LocalDateTime
);

