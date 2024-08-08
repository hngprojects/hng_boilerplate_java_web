package hng_java_boilerplate.activitylog.controller;

import hng_java_boilerplate.activitylog.dto.ActivityLogResponseDto;
import hng_java_boilerplate.activitylog.dto.ApiResponseDto;
import hng_java_boilerplate.activitylog.dto.ErrorResponseDto;
import hng_java_boilerplate.activitylog.model.ActivityLog;
import hng_java_boilerplate.activitylog.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/organizations")
@PreAuthorize("hasRole('ADMIN')")
public class ActivityLogController {
    private final ActivityLogService activityLogService;


    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping("/{orgId}/users/{userId}/activity-logs")
    public ResponseEntity<?> getActivityLogs(
            @PathVariable("orgId") String orgId,
            @PathVariable("userId") String userId,
            Authentication authentication) {
        try {
            if (orgId == null || orgId.isEmpty() || userId == null || userId.isEmpty()) {
                return new ResponseEntity<>(new ErrorResponseDto(400, "Invalid orgId or userId", "Bad Request"), HttpStatus.BAD_REQUEST);
            }

            List<ActivityLog> activityLogs = activityLogService.getActivityLogs(orgId, userId);

            if (activityLogs.isEmpty()) {
                return new ResponseEntity<>(new ErrorResponseDto(404, "Organization or user not found", "Not Found"), HttpStatus.NOT_FOUND);
            }

            List<ActivityLogResponseDto> data = activityLogs.stream().map(log -> {
                ActivityLogResponseDto dto = new ActivityLogResponseDto();
                dto.setActivity(log.getActivity());
                dto.setTimestamp(log.getTimestamp().toString());
                return dto;
            }).collect(Collectors.toList());

            ApiResponseDto response = new ApiResponseDto("Activity logs retrieved successfully", true, 200, data);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponseDto(500, "A server error occurred", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
