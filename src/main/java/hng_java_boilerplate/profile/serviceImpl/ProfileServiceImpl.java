package hng_java_boilerplate.profile.serviceImpl;

import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.response.DeactivateUserResponse;
import hng_java_boilerplate.profile.service.ProfileService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public DeactivateUserResponse deactivateUser(DeactivateUserRequest request) {
        User authUser = userService.getLoggedInUser();

        String confirmation = request.getConfirmation().toLowerCase();

        if (confirmation.equals("true") && authUser.getIsDeactivated()) {
            throw new BadRequestException("User has been deactivated");
        }

        if (!confirmation.equals("true")) throw new BadRequestException("Confirmation needs to be true for deactivation");

        authUser.setIsDeactivated(true);
        userRepository.save(authUser);

        //todo: call email service to notify user of account deactivated

        return new DeactivateUserResponse(200, "Account Deactivated Successfully");
    }
}
