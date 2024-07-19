package hng_java_boilerplate.util;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.user.entity.User;
import jakarta.persistence.PrePersist;

import java.util.UUID;

public class UUIDGenarator {

    @PrePersist
    public void generateUUID(Object entity) {
        if (entity instanceof User user) {
            if (user.getId() == null) {
                user.setId(UUID.randomUUID().toString());
            }
        } else if (entity instanceof Profile profile) {
            if (profile.getId() == null) {
                profile.setId(UUID.randomUUID().toString());
            }
        } else if (entity instanceof Product product) {
            if (product.getId() == null) {
                product.setId(UUID.randomUUID().toString());
            }
        } else if (entity instanceof Organisation organisation) {
            if (organisation.getId() == null) {
                organisation.setId(UUID.randomUUID().toString());
            }
        }
    }
}
