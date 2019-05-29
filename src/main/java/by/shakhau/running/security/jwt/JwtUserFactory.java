package by.shakhau.running.security.jwt;

import by.shakhau.running.service.dto.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {}

    public static JwtUser createUser(User user) {
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
        return new JwtUser(user.getName(), user.getPassword(), authorities);
    }
}
