package hng_java_boilerplate.waitlist.service;

import hng_java_boilerplate.waitlist.entity.WaitlistPage;
import hng_java_boilerplate.waitlist.repository.WaitlistPageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WaitlistPageService {

    private final WaitlistPageRepository waitlistPageRepository;

    public WaitlistPageService(WaitlistPageRepository waitlistPageRepository) {
        this.waitlistPageRepository = waitlistPageRepository;
    }

    public WaitlistPage createWaitlistPage(WaitlistPage waitlistPage) {
        return waitlistPageRepository.save(waitlistPage);
    }

    public Page<WaitlistPage> getWaitlistPages(Pageable pageable) {
        return waitlistPageRepository.findAll(pageable);
    }

    public Page<WaitlistPage> searchWaitlistPages(String keyword, Pageable pageable) {
        Specification<WaitlistPage> spec = (root, query, criteriaBuilder) -> {
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("pageTitle")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("urlSlug")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("headlineText")), likePattern)
            );
        };
        return waitlistPageRepository.findAll(spec, pageable);
    }

    public WaitlistPage toggleWaitlistPageActive(UUID id) {
        WaitlistPage waitlistPage = waitlistPageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Waitlist page not found"));
        waitlistPage.setActive(!waitlistPage.isActive());
        return waitlistPageRepository.save(waitlistPage);
    }
}