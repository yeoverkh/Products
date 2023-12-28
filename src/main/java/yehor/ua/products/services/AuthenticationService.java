package yehor.ua.products.services;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yehor.ua.products.dto.AuthenticationResponseDto;
import yehor.ua.products.dto.CredentialsDto;
import yehor.ua.products.models.UserEntity;
import yehor.ua.products.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto addUser(CredentialsDto credentialsDto) {
        throwExceptionIfUserWithUsernameExists(credentialsDto.username());

        UserEntity newUser = createUserFromDto(credentialsDto);

        userRepository.save(newUser);

        return getAuthResponseWithGeneratedToken(newUser);
    }

    private void throwExceptionIfUserWithUsernameExists(String username) {
        Optional<UserEntity> foundOptionalUser = userRepository.findByUsername(username);

        boolean isUserPresentByUsername = foundOptionalUser.isPresent();

        if (isUserPresentByUsername) {
            throw new EntityExistsException();
        }
    }

    private UserEntity createUserFromDto(CredentialsDto credentialsDto) {
        return new UserEntity(
                credentialsDto.username(),
                passwordEncoder.encode(credentialsDto.password())
        );
    }

    private AuthenticationResponseDto getAuthResponseWithGeneratedToken(UserEntity newUser) {
        String jwt = jwtService.generateToken(newUser);

        return new AuthenticationResponseDto(jwt);
    }

    public AuthenticationResponseDto authenticate(CredentialsDto credentialsDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentialsDto.username(), credentialsDto.password())
        );

        UserEntity foundUser = getUserByUsername(credentialsDto);

        return getAuthResponseWithGeneratedToken(foundUser);
    }

    private UserEntity getUserByUsername(CredentialsDto credentialsDto) {
        return userRepository.findByUsername(credentialsDto.username())
                .orElseThrow(EntityNotFoundException::new);
    }
}
