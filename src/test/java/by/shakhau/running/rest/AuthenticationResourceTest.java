package by.shakhau.running.rest;

import by.shakhau.running.security.UserAuthenticationDto;
import by.shakhau.running.security.jwt.JwtAuthenticationException;
import by.shakhau.running.security.jwt.JwtTokenProvider;
import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.Token;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.util.DtoFactory;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationResourceTest extends ResourceTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserService userService;

    @Test
    public void testLogin() throws Exception {
        User user = DtoFactory.getUser();
        UserAuthenticationDto dto = DtoFactory.getUserAuthenticationDto();
        Token token = DtoFactory.getToken();

        when(userService.findByName(user.getName())).thenReturn(user);
        when(jwtTokenProvider.createToken(user.getName(), user.getRoles())).thenReturn(token);
        Token tokenCreated = getMapper().readValue(getMockMvc()
                .perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(getMapper().writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString(), Token.class);

        assertThat(token.getAccessToken()).isEqualTo(tokenCreated.getAccessToken());
        assertThat(token.getRefreshToken()).isEqualTo(tokenCreated.getRefreshToken());
    }

    @Test
    public void testLoginWhenBadCredentials() throws Exception {
        UserAuthenticationDto dto = DtoFactory.getUserAuthenticationDto();
        Authentication authentication = new UsernamePasswordAuthenticationToken(dto.getUserName(), dto.getPassword());

        when(authenticationManager.authenticate(authentication)).thenThrow(new JwtAuthenticationException("Not authenticated"));

        getMockMvc()
                .perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(getMapper().writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testLoginWhenUserNotFound() throws Exception {
        User user = DtoFactory.getUser();
        UserAuthenticationDto dto = DtoFactory.getUserAuthenticationDto();

        when(userService.findByName(user.getName())).thenReturn(null);

        getMockMvc()
                .perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(getMapper().writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testRefreshToken() throws Exception {
        UserAuthenticationDto dto = DtoFactory.getUserAuthenticationDto();
        Token token = DtoFactory.getToken();

        when(jwtTokenProvider.createRefreshToken(token.getRefreshToken())).thenReturn(token);
        Token tokenCreated = getMapper().readValue(getMockMvc()
                .perform(post("/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON).content(token.getRefreshToken()))
                .andReturn().getResponse().getContentAsString(), Token.class);

        assertThat(token.getAccessToken()).isEqualTo(tokenCreated.getAccessToken());
        assertThat(token.getRefreshToken()).isEqualTo(tokenCreated.getRefreshToken());
    }
}