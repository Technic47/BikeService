package ru.bikeservice.mainresources.models.dto.kafka;

import ru.bikeservice.mainresources.models.dto.KafkaUserDto;

public class SearchKafkaDTO {
    private String findBy;
    private String searchValue;
    private KafkaUserDto kafkaUserDto;
    private boolean shared;
    private String category;

    public SearchKafkaDTO(String findBy, String searchValue, KafkaUserDto kafkaUserDto, boolean shared, String category) {
        this.findBy = findBy;
        this.searchValue = searchValue;
        this.kafkaUserDto = kafkaUserDto;
        this.shared = shared;
        this.category = category;
    }

    public SearchKafkaDTO(String findBy, String searchValue, KafkaUserDto kafkaUserDto, boolean shared) {
        this.findBy = findBy;
        this.searchValue = searchValue;
        this.kafkaUserDto = kafkaUserDto;
        this.shared = shared;
    }

    public String getFindBy() {
        return findBy;
    }

    public void setFindBy(String findBy) {
        this.findBy = findBy;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public KafkaUserDto getUserDTO() {
        return kafkaUserDto;
    }

    public void setUserDTO(KafkaUserDto kafkaUserDto) {
        this.kafkaUserDto = kafkaUserDto;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
