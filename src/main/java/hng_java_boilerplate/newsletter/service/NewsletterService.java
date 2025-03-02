package hng_java_boilerplate.newsletter.service;

import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.dto.SubscribersDto;
import hng_java_boilerplate.newsletter.dto.SubscribersResponse;
import hng_java_boilerplate.newsletter.entity.Newsletter;
import hng_java_boilerplate.newsletter.repository.NewsletterRepository;
import hng_java_boilerplate.user.dto.response.Response;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public SubscribersResponse getSubscribersResponse(int page, int size) {
        Pageable pageable = buildPageable(page, size);
        Page<Newsletter> newsletterPage = newsletterRepository.findAll(pageable);
        List<SubscribersDto> subscriberDto = mapNewslettersToSubscribers(newsletterPage.getContent());

        return buildSubscribersResponse(newsletterPage, subscriberDto);
    }

    private Pageable buildPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by("createdAt").descending());
    }

    private List<SubscribersDto> mapNewslettersToSubscribers(List<Newsletter> newsletters) {
        return newsletters.stream()
                .map(newsletter -> {
                    User user = userRepository.findById(newsletter.getUserId())
                            .orElseThrow(() -> new NotFoundException("User not found for subscription id: " + newsletter.getId()));
                    return SubscribersDto.builder()
                            .id(newsletter.getId())
                            .email(user.getEmail())
                            .subscribedAt(newsletter.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private SubscribersResponse buildSubscribersResponse(Page<Newsletter> newsletterPage, List<SubscribersDto> subscriberDtos) {
        return SubscribersResponse.builder()
                .subscribers(subscriberDtos)
                .page(newsletterPage.getNumber())
                .size(newsletterPage.getSize())
                .totalElements(newsletterPage.getTotalElements())
                .totalPages(newsletterPage.getTotalPages())
                .build();

    }
}
