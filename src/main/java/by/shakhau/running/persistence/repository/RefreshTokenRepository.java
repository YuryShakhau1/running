package by.shakhau.running.persistence.repository;

import by.shakhau.running.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    RefreshTokenEntity findByToken(String token);

    @Modifying
    @Query("delete from RefreshTokenEntity rt where rt.expireDate < ?1")
    void removeExpired(Date dateNow);
}
