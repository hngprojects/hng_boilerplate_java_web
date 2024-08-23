package hng_java_boilerplate.blog.comment.controller;


import hng_java_boilerplate.blog.comment.dto.ErrorResponse;
import hng_java_boilerplate.blog.comment.dto.RequestDto;
import hng_java_boilerplate.blog.comment.dto.CommentDataDto;
import hng_java_boilerplate.blog.comment.dto.ResponseDto;
import hng_java_boilerplate.blog.comment.entity.Comment;
import hng_java_boilerplate.blog.comment.service.CommentService;
import hng_java_boilerplate.exception.exception_class.NotFoundException;

import hng_java_boilerplate.authentication.user.entity.User;
import hng_java_boilerplate.authentication.user.serviceImpl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserServiceImpl userService;

    @PostMapping("/add")
    public ResponseEntity<?> createComment(@Valid @RequestBody RequestDto request){
        User loggedInUser = userService.getLoggedInUser();
        if(loggedInUser == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
        }
        try{
            Comment comment = commentService.createComment(loggedInUser.getId(), loggedInUser.getName(), request.getComment());
            CommentDataDto commentDataDto = new CommentDataDto(
                    comment.getUser().getName(),
                    comment.getUser().getId(),
                    comment.getComment(),
                    comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yy-MM-dd:HH:mm:ss")));
            ResponseDto responseDto = new ResponseDto("ok", "Comment added successfully", commentDataDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input data", e);

        }

    }

    @DeleteMapping("/delete/{commentId}")
    @PreAuthorize("@CommentService.isUserAuthorizedToDeleteComment(#commentId, principal.username)")
    public ResponseEntity<Object> deleteComment(@PathVariable String commentId, String userId) {
        try {
            commentService.softDeleteComment(commentId, userId);
            Map<String, Object> response = new HashMap<>();

            response.put("message", "Comment successfully deleted");
            response.put("status_code", "204");

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (NotFoundException ex) {
            ErrorResponse error = new ErrorResponse(
                    "Comment not found",
                    "Invalid commentId",
                    HttpStatus.NOT_FOUND.value()
            );
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}
