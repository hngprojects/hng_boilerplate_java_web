package hng_java_boilerplate.organisation.specification;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.user.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class UserSpecification implements Specification<User> {
    private final Organisation organisation;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.isTrue(root.join("organisations").in(organisation));
    }
}
