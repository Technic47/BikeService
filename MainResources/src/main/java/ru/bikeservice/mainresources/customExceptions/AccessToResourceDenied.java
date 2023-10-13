package ru.bikeservice.mainresources.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AccessToResourceDenied extends RuntimeException {
    private Long id;

    public AccessToResourceDenied(Long id) {
        super("You don`t have access to modify item with id: " + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
