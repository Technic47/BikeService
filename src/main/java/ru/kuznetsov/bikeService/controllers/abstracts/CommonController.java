package ru.kuznetsov.bikeService.controllers.abstracts;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.models.abstracts.AbstractEntity;

import javax.validation.Valid;

public interface CommonController<T extends AbstractEntity> {
    @GetMapping()
    public String index(Model model);

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model);

    @GetMapping(value = "/new")
    public String newItem(Model model);

    @PostMapping()
    public String create(@Valid T item,
                         BindingResult bindingResult
            , @RequestPart("newImage") MultipartFile file
    );

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id);

    @PatchMapping("/{id}")
    public String update(@Valid T item, BindingResult bindingResult,
                         @PathVariable("id") Long id);

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id);
}
