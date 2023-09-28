package ru.kuznetsov.emailsender.models.users;

public class UserModel {
    private Long id;
    private String username;
    private String email;
    private boolean active;
    private boolean enabled;

    public UserModel() {
        this.enabled = false;
    }

    public String getName() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }


    @Override
    public String toString() {
        return "Username='" + username +
                "', id=" + id +
                ", enabled=" + enabled;
    }
}
