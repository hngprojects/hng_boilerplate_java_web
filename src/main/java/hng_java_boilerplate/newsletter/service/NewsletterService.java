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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public Page<Newsletter> findNewsletterByUserId(String userId, Pageable pageable){
        return newsletterRepository.findByUser_Id(userId,pageable);
    }

    public Page<Newsletter> findNewsletterByCreatedAtAfter(LocalDateTime date, Pageable pageable){
        return newsletterRepository.findNewsletterByCreatedAtAfter(date,pageable);
    }

    public Response<?> deleteNewsletterByUserId(String userId){
        newsletterRepository.deleteByUser_Id(userId);
        return Response.builder().status_code("success").message("Newsletter deleted successfully.").build();
    }
}
