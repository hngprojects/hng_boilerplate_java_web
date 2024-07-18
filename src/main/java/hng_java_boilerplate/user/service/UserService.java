package hng_java_boilerplate.user.service;

import hng_java_boilerplate.user.dto.GetUserDto;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public GetUserDto getUserWithDetails(String userId) throws BadPaddingException {
        User user = userRepository.findById(userId)
                .orElseThrow(BadPaddingException::new);

        GetUserDto userDto = GetUserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        GetUserDto.ProfileDto profile = GetUserDto.ProfileDto
                .builder()
                .first_name(user.getProfile().getFirstName())
                .last_name(user.getProfile().getLastName())
                .phone(user.getProfile().getPhone())
                .avatar_url(user.getProfile().getAvatarUrl())
                .build();

        List<GetUserDto.OrganisationDto> organisations = user.getOrganisations()
                .stream()
                .map((org) -> GetUserDto.OrganisationDto
                        .builder()
                        .org_id(org.getId())
                        .name(org.getName())
                        .description(org.getDescription())
                        .build()).toList();

        List<GetUserDto.ProductDto> products = user.getProducts()
                .stream().map((product) -> GetUserDto.ProductDto
                        .builder()
                        .product_id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .build()).toList();

        userDto.setProfile(profile);
        userDto.setProducts(products);
        userDto.setOrganisations(organisations);

        return userDto;
    };
}
