package by.shakhau.running.service;

import by.shakhau.running.persistence.entity.RefreshTokenEntity;

public interface RefreshTokenService {

    RefreshTokenEntity findByToken(String token);
    void removeExpired();
    RefreshTokenEntity save(RefreshTokenEntity refreshToken);
}
