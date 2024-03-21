package ru.bikeservice.mainresources.models;

import ru.bikeservice.mainresources.models.dto.AuthenticationRequestDto;
import ru.bikeservice.mainresources.models.users.UserModel;

public class RegistrationRequestDto extends AuthenticationRequestDto {
    private String email;

    public UserModel toUserModel() {
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
