package by.shakhau.running.service.dto;

import java.util.Date;

public class TokenWithDateExpire {

    private Date expireDate;
    private String token;

    public TokenWithDateExpire(Date expireDate, String token) {
        this.expireDate = expireDate;
        this.token = token;
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
