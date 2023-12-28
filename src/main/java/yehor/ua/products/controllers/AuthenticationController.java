package yehor.ua.products.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yehor.ua.products.dto.AuthenticationResponseDto;
import yehor.ua.products.dto.CredentialsDto;
import yehor.ua.products.services.AuthenticationService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/add")
    public ResponseEntity<AuthenticationResponseDto> addUser(@RequestBody CredentialsDto credentialsDto) {
        return new ResponseEntity<>(authenticationService.addUser(credentialsDto), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody CredentialsDto credentialsDto) {
        return ResponseEntity.ok(authenticationService.authenticate(credentialsDto));
    }
}
