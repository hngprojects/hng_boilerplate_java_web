package hng_java_boilerplate.blog.squeeze.service;

import hng_java_boilerplate.blog.squeeze.entity.SqueezeConfig;
import hng_java_boilerplate.blog.squeeze.repository.SqueezeConfigRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SqueezeConfigService {

    private final SqueezeConfigRepository squeezeConfigRepository;

    public SqueezeConfigService(SqueezeConfigRepository squeezeConfigRepository) {
        this.squeezeConfigRepository = squeezeConfigRepository;
    }

    public SqueezeConfig createSqueezePage(SqueezeConfig squeezeConfig) {
        return squeezeConfigRepository.save(squeezeConfig);
    }

    public Page<SqueezeConfig> getSqueezePages(Pageable pageable) {
        return squeezeConfigRepository.findAll(pageable);
    }

    public Page<SqueezeConfig> searchSqueezePages(String keyword, Pageable pageable) {
        Specification<SqueezeConfig> spec = (root, query, criteriaBuilder) -> {
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("pageTitle")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("urlSlug")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("headlineText")), likePattern)
            );
        };
        return squeezeConfigRepository.findAll(spec, pageable);
    }

    public SqueezeConfig toggleSqueezePageActive(UUID id) {
        SqueezeConfig squeezeConfig = squeezeConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Squeeze page not found"));
        squeezeConfig.setActive(!squeezeConfig.isActive());
        return squeezeConfigRepository.save(squeezeConfig);
    }

    public boolean deleteSqueezePage(String id) {
        if (squeezeConfigRepository.existsById(UUID.fromString(id))) {
            squeezeConfigRepository.deleteById(UUID.fromString(id));
            return true;
        }
        return false;
    }
}