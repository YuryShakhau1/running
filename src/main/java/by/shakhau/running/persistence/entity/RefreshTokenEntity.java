package by.shakhau.running.persistence.entity;

import javax.persistence.*;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity implements Entity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "token", nullable = false)
    private String token;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
