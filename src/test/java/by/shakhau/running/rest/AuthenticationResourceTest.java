package by.shakhau.running.rest;

import by.shakhau.running.security.UserAuthenticationDto;
import by.shakhau.running.security.jwt.JwtAuthenticationException;
import by.shakhau.running.security.jwt.JwtTokenProvider;
import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.util.DtoFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
        String token = "token";

        when(userService.findByName(user.getName())).thenReturn(user);
        when(jwtTokenProvider.createToken(user.getName(), user.getRoles())).thenReturn(token);
        getMockMvc()
                .perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(getMapper().writeValueAsString(dto)))
                .andExpect(content().string(token));
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
}