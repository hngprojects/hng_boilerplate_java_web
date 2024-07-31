package hng_java_boilerplate.comments.controller;

import hng_java_boilerplate.comments.entity.Comment;
import hng_java_boilerplate.comments.exceptions.ResourceNotFoundException;
import hng_java_boilerplate.comments.service.CommentService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/comments")
public class CommentController {
    private final UserService userService;
    private final CommentService commentService;
    @PostMapping("api/v1/blogs/{blog_id}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable String blog_id,@RequestBody Comment comment){
        Comment createdComment = commentService.createComment(blog_id, comment);
        return ResponseEntity.ok(createdComment);
    }
    @GetMapping("api/v1/blogs/{blog_id}/comments")
    public ResponseEntity<Collection<org.hibernate.annotations.Comment>> getCommentsForAPost(
            @PathVariable String blog_id) {
        Collection<org.hibernate.annotations.Comment> comments = commentService.getCommentsForAPost(blog_id);
        return ResponseEntity.ok(comments);
    }
   // @PostMapping("/delete/{comment_id}")
    //public ResponseEntity<Comment> deleteAComment(String blog_id,@PathVariable String commentId){
      //  Comment deletedComment = commentService.deleteAComment(commentId);}

    @PostMapping("/{commentId}/reply")
    public ResponseEntity<Comment> replyToComment(
            @PathVariable String commentId,
            @RequestBody Comment reply,
            @RequestParam String userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getUser();
        Comment repliedComment = commentService.replyToComment(commentId, reply, user);
        return ResponseEntity.ok(repliedComment);
    }
    @PostMapping("/{commentId}/like")
    public ResponseEntity<Comment> likeComment(
            @PathVariable String commentId,
            @RequestParam String userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getUser();
        Comment likedComment = commentService.likeComment(commentId, user);
        return ResponseEntity.ok(likedComment);
    }
    @PostMapping("/{commentId}/dislike")
    public ResponseEntity<Comment> dislikeComment(
            @PathVariable String commentId,
            @RequestParam String userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getUser();
        Comment dislikedComment = commentService.dislikeComment(commentId, user);
        return ResponseEntity.ok(dislikedComment);
    }
    @PatchMapping("/{commentId}/edit")
    public ResponseEntity<Comment> editComment(
            @PathVariable String commentId,
            @RequestBody String newText,
            @RequestParam String userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getUser();
        Comment editedComment = commentService.editComment(commentId, newText, user);
        return ResponseEntity.ok(editedComment);
    }
}
