package hng_java_boilerplate.blogPosts.controller;

import hng_java_boilerplate.blogPosts.entity.BlogPost;
import hng_java_boilerplate.blogPosts.entity.BlogSuperAdminUser;
import hng_java_boilerplate.blogPosts.service.BlogPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/blogs")
@SessionAttributes("post")
public class BlogPostController {
    private final BlogPostService blogPostService;
    private final BlogSuperAdminUser blogSuperAdminUser;

    @GetMapping("/{blog_id}")
    public String getBlogPost(@PathVariable String id, Model model, Principal principal) {
        String authUserName = "anonymousUser";
        if (principal != null) {
            authUserName = principal.getName();
        }

        Optional<BlogPost> optionalPost = this.blogPostService.getById(id);

        if (optionalPost.isPresent()) {
            BlogPost post = optionalPost.get();
            model.addAttribute("post", post);


            if (authUserName.equals(post.getAuthor().getUsername())) {
                model.addAttribute("isOwner", true);
            }
            return "post";

        } else {
            return "404";
        }
    }

    @Secured("ROLE_USER")
    @PostMapping("/createNewPost")
    public String createNewPost(@Valid @RequestBody @ModelAttribute BlogPost post, BindingResult result, SessionStatus sessionStatus) {
        if (result.hasErrors()) {
            System.err.println("Post not validated");
            return "postForm";
        }
        //save post if all is good
        this.blogPostService.save(post);
        System.err.println("SAVE post:" + post);
        sessionStatus.setComplete();
        return "redirect:/post/" + post.getId();
    }

    @Secured("ROLE_USER")
    @GetMapping("/edit/{blog_id}")
    public String editPost(@PathVariable String blog_id, Model model, Principal principal) {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Optional<BlogPost> optionalPost = blogPostService.getById(blog_id);
        if (optionalPost.isPresent()) {
            BlogPost post = optionalPost.get();

            if (authUsername.equals(post.getAuthor().getUsername())) {
                model.addAttribute("post:" + post);
                System.err.println("EDIT Post" + post);
                return "postForm";
            } else {
                System.err.println("This user does not have permission to edit this post");
                return "403";
            }
        } else {
            System.err.println("Could not find the post with id" + blog_id);
            return "error";
        }

    }

    @Secured("ROLE_USER")
    @GetMapping("/delete/{blog_id}")
    public String delete(@PathVariable String blog_id, Principal principal) {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Optional<BlogPost> optionalPost = blogPostService.getById(blog_id);
        if (optionalPost.isPresent()) {
            BlogPost post = optionalPost.get();

            if (authUsername.equals(post.getAuthor().getUsername())) {
                this.blogPostService.delete(post);

                System.err.println("DELETED Post" + post);
                return "redirect/";
            } else {
                System.err.println("Current User has no permission to delete post");
                return "403";
            }
        } else {
            System.err.println("Could not find the post with id" + blog_id);
            return "error";
        }
    }
}

