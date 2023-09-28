package ru.kuznetsov.bikeService.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private Long id;

    public ResourceNotFoundException(Long id) {
        super("Item with id: " + id + " not found!");
        this.id = id;
    }

    public ResourceNotFoundException(String message){
        super(message);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    public Long getId() {
        return id;
    }
}
