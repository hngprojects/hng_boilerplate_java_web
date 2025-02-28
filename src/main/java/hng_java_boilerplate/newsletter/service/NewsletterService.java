package hng_java_boilerplate.newsletter.service;

import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.entity.Newsletter;
import hng_java_boilerplate.newsletter.repository.NewsletterRepository;
import hng_java_boilerplate.user.dto.response.Response;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsletterService {
    private final NewsletterRepository newsletterRepository;
    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;

    public SubscribeResponse subscribeToNewsletter(SubscribeRequest request) {
       User user =  userRepository.findByEmail(request.getEmail())
               .orElseThrow(() -> new NotFoundException("user not found with email"));

       Newsletter newsletter = new Newsletter();
       newsletter.setUser(user);
       newsletter.setCreatedAt(LocalDateTime.now());
       newsletter.setUpdatedAt(LocalDateTime.now());
       newsletterRepository.saveAndFlush(newsletter);

       emailService.sendNewsletterNotification(user);

       return new SubscribeResponse(201, "subscription successful");
    }

    public List<Newsletter> findNewsletterByUserId(String userId){
        return newsletterRepository.findByUser_Id(userId);
    }

    public List<Newsletter> findNewsletterByCreatedAtAfter(LocalDateTime date){
        return newsletterRepository.findNewsletterByCreatedAtAfter(date);
    }

    public Response<?> deleteNewsletterByUserId(String userId){
        newsletterRepository.deleteByUser_Id(userId);
        return Response.builder().status_code("success").message("Newsletter deleted successfully.").build();
    }
}
