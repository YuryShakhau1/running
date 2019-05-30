package by.shakhau.running.rest;

import by.shakhau.running.security.UserAuthenticationDto;
import by.shakhau.running.security.jwt.JwtAuthenticationException;
import by.shakhau.running.security.jwt.JwtTokenProvider;
import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.util.DtoFactory;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AuthenticationResourceTest extends ResourceTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserService userService;

    @Test
    public void testLogin() {
        User user = DtoFactory.getUser();
        UserAuthenticationDto dto = DtoFactory.getUserAuthenticationDto();
        String token = "token";

        when(userService.findByName(user.getName())).thenReturn(user);
        when(jwtTokenProvider.createToken(user.getName(), user.getRoles())).thenReturn(token);

        String tokenCreated = getRestTemplate()
                .postForObject(
                        "http://localhost:" + getPort() + "/auth/login",
                        dto,
                        String.class);

        assertThat(token).isEqualTo(tokenCreated);
    }

    @Test
    public void testLoginWhenBadCredentials() {
        UserAuthenticationDto dto = DtoFactory.getUserAuthenticationDto();
        Authentication authentication = new UsernamePasswordAuthenticationToken(dto.getUserName(), dto.getPassword());

        when(authenticationManager.authenticate(authentication)).thenThrow(new JwtAuthenticationException("Not authenticated"));

        ResponseEntity<String> response = getRestTemplate()
                .postForEntity(
                        "http://localhost:" + getPort() + "/auth/login",
                        dto,
                        String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void testLoginWhenUserNotFound() {
        User user = DtoFactory.getUser();
        UserAuthenticationDto dto = DtoFactory.getUserAuthenticationDto();

        when(userService.findByName(user.getName())).thenReturn(null);

        ResponseEntity<String> response = getRestTemplate()
                .postForEntity(
                        "http://localhost:" + getPort() + "/auth/login",
                        dto,
                        String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}