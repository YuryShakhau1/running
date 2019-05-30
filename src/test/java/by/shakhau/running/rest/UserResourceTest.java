package by.shakhau.running.rest;

import by.shakhau.running.security.UserAuthenticationDto;
import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.User;
import by.shakhau.running.util.AssertHelper;
import by.shakhau.running.util.DtoFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class UserResourceTest extends ResourceTest {

    @MockBean
    private UserService userService;

    @Test
    @WithUserDetails("testUser")
    public void testFindAll() throws Exception {
        List<User> users = Arrays.asList(DtoFactory.getUser());

        when(userService.findAll()).thenReturn(users);

        List<User> usersFound = getMapper().readValue(getMockMvc()
                .perform(get("/user/all"))
                        .andReturn().getResponse().getContentAsString(), new TypeReference<List<User>>() {});

        assertThat(usersFound.stream().findFirst().get().getPassword()).isNull();
        usersFound.stream().findFirst().get().setPassword(users.stream().findFirst().get().getPassword());
        AssertHelper.assertUsers(users, usersFound);
    }

    @Test
    public void testAvailableWhenUserIsAvailable() throws Exception {
        User user = DtoFactory.getUser();

        when(userService.findByName(user.getName())).thenReturn(null);

        getMockMvc()
                .perform(get("/user/" + user.getName() + "/available"))
                .andExpect(content().string("true"));
    }

    @Test
    public void testAvailableWhenUserIsNotAvailable() throws Exception {
        User user = DtoFactory.getUser();

        when(userService.findByName(user.getName())).thenReturn(user);

        getMockMvc()
                .perform(get("/user/" + user.getName() + "/available"))
                .andExpect(content().string("false"));
    }

    @Test
    public void testCreateUser() throws Exception {
        UserAuthenticationDto userAuth = DtoFactory.getUserAuthenticationDto();
        User user = DtoFactory.getUser();

        when(userService.createUser(user.getName(), user.getPassword())).thenReturn(user);

        User userCreated = getMapper().readValue(getMockMvc()
                .perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON).content(getMapper().writeValueAsString(userAuth)))
                .andReturn().getResponse().getContentAsString(), User.class);

        assertThat(userCreated.getPassword()).isNull();
        userCreated.setPassword(user.getPassword());
        AssertHelper.assertUsers(user, userCreated);
    }
}