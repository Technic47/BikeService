package ru.kuznetsov.emailsender.models;


import ru.kuznetsov.emailsender.models.users.UserModel;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;
    private Long id;

    private String token;
    private UserModel user;

    private Date expiryDate;

    public VerificationToken() {
    }

    public VerificationToken(String token, UserModel user) {
        this.token = token;
        this.user = user;
        this.expiryDate = this.calculateExpiryDate();
    }

    public void updateToken(){
        this.expiryDate = this.calculateExpiryDate();
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}