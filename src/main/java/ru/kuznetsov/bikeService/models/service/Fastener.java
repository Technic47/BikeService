package ru.kuznetsov.bikeService.models.service;

import javax.validation.constraints.NotEmpty;

public class Fastener {
    @NotEmpty(message = "Fill this field!")
    private String type;
    @NotEmpty(message = "Fill this field!")
    private String specs;
    private String description;

    public Fastener(String type, String specs, String description) {
        this.type = type;
        this.specs = specs;
        this.description = description;
    }

    public Fastener(String type, String specs) {
        this(type, specs, "");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
