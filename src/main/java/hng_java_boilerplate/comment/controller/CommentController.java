package hng_java_boilerplate.comment.controller;


import hng_java_boilerplate.comment.dto.CommentDataDto;
import hng_java_boilerplate.comment.dto.RequestDto;
import hng_java_boilerplate.comment.dto.ResponseDto;
import hng_java_boilerplate.comment.entity.Comment;
import hng_java_boilerplate.comment.service.CommentService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

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


}
