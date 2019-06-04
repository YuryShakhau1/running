package by.shakhau.running.security.jwt;

import by.shakhau.running.persistence.entity.RefreshTokenEntity;
import by.shakhau.running.persistence.repository.RefreshTokenRepository;
import by.shakhau.running.service.UserService;
import by.shakhau.running.service.dto.Role;
import by.shakhau.running.service.dto.Token;
import by.shakhau.running.service.dto.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.access.token.expired}")
    private Long validAccessInMillis;

    @Value("${jwt.refresh.token.expired}")
    private Long validRefreshInMillis;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    private void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public Token createToken(String userName, List<Role> roles) {
        String accessToken = createToken(userName, roles, validAccessInMillis);
        String refreshToken = createToken(userName, roles, validRefreshInMillis);
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken);
        if (refreshTokenEntity == null) {
            refreshTokenEntity = new RefreshTokenEntity();
        }
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
        return new Token(accessToken, refreshToken);
    }

    public Token refreshToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new JwtAuthenticationException("Token " + refreshToken + " is not valid");
        }

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken);
        if (refreshTokenEntity == null || !refreshToken.equals((refreshTokenEntity.getToken()))) {
            throw new JwtAuthenticationException("Token " + refreshToken + " is not valid");
        }

        User user = userService.findByName(getUserName(refreshToken));
        String userName = user.getName();
        List<Role> roles = user.getRoles();
        String createdAccessToken = createToken(userName, roles, validAccessInMillis);
        String createdRefreshToken = createToken(userName, roles, validRefreshInMillis);
        return new Token(createdAccessToken, createdRefreshToken);
    }

    public Authentication authentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserName(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserName(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        String prefix = "Bearer_";
        if (bearerToken == null || !bearerToken.startsWith(prefix)) {
            return null;
        }

        return bearerToken.substring(prefix.length());
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Token " + token + " is not valid", e);
        }
    }

    private String createToken(String userName, List<Role> roles, long validTime) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("roles", getRoleNames(roles));

        Date dateNow = new Date();
        Date validity = new Date(dateNow.getTime() + validTime);

        return Jwts.builder()
                .setClaims(claims)
                .setId(userName)
                .setIssuedAt(dateNow)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private List<String> getRoleNames(List<Role> roles) {
        return roles.stream().map(r -> r.getName()).collect(Collectors.toList());
    }
}
