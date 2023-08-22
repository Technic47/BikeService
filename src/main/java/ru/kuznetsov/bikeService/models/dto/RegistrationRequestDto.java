package ru.kuznetsov.bikeService.models.dto;

import ru.kuznetsov.bikeService.models.users.UserModel;

public class RegistrationRequestDto extends AuthenticationRequestDto {
    private String email;

    public UserModel toUserModel(){
        UserModel newModel = new UserModel(this.username, this.password);
        newModel.setEmail(this.email);
        return newModel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
