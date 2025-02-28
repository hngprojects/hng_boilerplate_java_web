package hng_java_boilerplate.newsletter.service;

import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.dto.SubscribersDto;
import hng_java_boilerplate.newsletter.dto.SubscribersResponse;
import hng_java_boilerplate.newsletter.entity.Newsletter;
import hng_java_boilerplate.newsletter.repository.NewsletterRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
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

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;

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

    public SubscribersResponse getSubscribersResponse(Integer page, Integer size) {
        int effectivePage = (page == null || page < 0) ? DEFAULT_PAGE : page;
        int effectiveSize = (size == null || size <= 0) ? DEFAULT_SIZE : size;

        Pageable pageable = buildPageable(effectivePage, effectiveSize);
        Page<Newsletter> newsletterPage = newsletterRepository.findAll(pageable);
        List<SubscribersDto> subscriberDtos = mapNewslettersToSubscribers(newsletterPage.getContent());

        return buildSubscribersResponse(newsletterPage, subscriberDtos);
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
