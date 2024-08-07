package hng_java_boilerplate.activitylog.service;

import hng_java_boilerplate.activitylog.model.ActivityLog;
import hng_java_boilerplate.activitylog.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;


    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }
    @Transactional
    public void logActivity(String orgId, String userId, String activity) {
        ActivityLog log = new ActivityLog();
        log.setOrgId(orgId);
        log.setUserId(userId);
        log.setActivity(activity);
        log.setTimestamp(Instant.now());
        System.out.println("Attempting to save log: " + log);
        activityLogRepository.save(log);
    }

    public List<ActivityLog> getActivityLogs(String orgId, String userId) {
        return activityLogRepository.findByOrgIdAndUserId(orgId, userId);
    }
}
