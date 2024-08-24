package hng_java_boilerplate.newsletter.service;

import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.entity.Newsletter;
import hng_java_boilerplate.newsletter.repository.NewsletterRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
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
       newsletter.setUserId(user.getId());
       newsletter.setCreatedAt(LocalDateTime.now());
       newsletter.setUpdatedAt(LocalDateTime.now());
       newsletterRepository.saveAndFlush(newsletter);

       emailService.sendNewsletterNotification(user);

       return new SubscribeResponse(201, "subscription successful");
    }
}
