package by.shakhau.running.service.impl;

import by.shakhau.running.persistence.entity.RefreshTokenEntity;
import by.shakhau.running.persistence.repository.RefreshTokenRepository;
import by.shakhau.running.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshTokenEntity findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Scheduled(fixedRateString = "${jwt.refresh.token.remove.expired.interval}")
    @Override
    public void removeExpired() {
        refreshTokenRepository.removeExpired(new Date());
    }

    @Override
    public RefreshTokenEntity save(RefreshTokenEntity refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }
}
