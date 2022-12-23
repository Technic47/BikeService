package ru.kuznetsov.bikeService.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONConverter<T> {
    public String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj, new TypeToken<T>() {
        }.getType());
    }

    public T fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<T>() {
        }.getType());
    }
}
