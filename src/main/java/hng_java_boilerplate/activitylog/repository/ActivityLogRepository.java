package hng_java_boilerplate.activitylog.repository;

import hng_java_boilerplate.activitylog.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByOrgIdAndUserId(String orgId, String userId);
}
