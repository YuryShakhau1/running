package by.shakhau.running.rest;

import by.shakhau.running.security.UserAuthenticationDto;
import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.util.AssertHelper;
import by.shakhau.running.util.DtoFactory;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class UserResourceTest extends ResourceTest {

    @MockBean
    private UserService userService;

    @Test
    public void testAvailableWhenUserIsAvailable() {
        User user = DtoFactory.getUser();

        when(userService.findByName(user.getName())).thenReturn(null);

        Boolean available = getRestTemplate()
                .getForObject(
                        "http://localhost:" + getPort() + "/user/" + user.getName() + "/available",
                        Boolean.class);

        assertThat(available).isEqualTo(true);
    }

    @Test
    public void testAvailableWhenUserIsNotAvailable() {
        User user = DtoFactory.getUser();

        when(userService.findByName(user.getName())).thenReturn(user);

        Boolean available = getRestTemplate()
                .getForObject(
                        "http://localhost:" + getPort() + "/user/" + user.getName() + "/available",
                        Boolean.class);

        assertThat(available).isEqualTo(false);
    }

    @Test
    public void testCreateUser() {
        UserAuthenticationDto userAuth = DtoFactory.getUserAuthenticationDto();
        User user = DtoFactory.getUser();

        when(userService.createUser(user.getName(), user.getPassword())).thenReturn(user);

        User userCreated = getRestTemplate()
                .postForObject(
                        "http://localhost:" + getPort() + "/user",
                        userAuth,
                        User.class);

        assertThat(userCreated.getPassword()).isNull();
        userCreated.setPassword(user.getPassword());
        AssertHelper.assertUsers(user, userCreated);
    }
}