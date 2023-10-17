package ru.kuznetsov.bikeService.controllers.rest.addition;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDto;
import ru.bikeservice.mainresources.models.users.UserModel;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.bikeservice.mainresources.models.support.EntitySupportService.createDtoFrom;

@RestController
@RequestMapping("/api/users")
public class UserControllerREST extends AbstractController {
    @GetMapping()
    public UserDto getUser(Principal principal) {
        String name = principal.getName();
        UserModel findUser = userService.findByUsernameOrNull(name);
        return new UserDto(findUser);
    }

    @GetMapping("/created")
    public ResponseEntity getCreatedItems(Principal principal) {
        String name = principal.getName();
        UserModel findUser = userService.findByUsernameOrNull(name);
        Long userId = findUser.getId();
        Map<Object, Object> response = new HashMap<>();
        response.put("documents", convertToDto(documentService.findByCreator(userId)));
        response.put("fasteners", convertToDto(fastenerService.findByCreator(userId)));
        response.put("manufacturers", convertToDto(manufacturerService.findByCreator(userId)));
        response.put("consumables", convertToDto(consumableService.findByCreator(userId)));
        response.put("tools", convertToDto(toolService.findByCreator(userId)));
        response.put("parts", convertToDto(partService.findByCreator(userId)));
        response.put("bikes", convertToDto(bikeService.findByCreator(userId)));
        return ResponseEntity.ok(response);
    }

    private <T extends AbstractShowableEntity> List<AbstractEntityDto> convertToDto(List<T> list) {
        List<AbstractEntityDto> dtoList = new ArrayList<>();
        list.forEach(item -> dtoList.add(createDtoFrom(item)));

        return dtoList;
    }
}
