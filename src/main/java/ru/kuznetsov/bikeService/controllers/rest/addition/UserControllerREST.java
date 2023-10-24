package ru.kuznetsov.bikeService.controllers.rest.addition;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDto;
import ru.bikeservice.mainresources.models.dto.kafka.IndexKafkaDTO;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.UserDto;
import ru.kuznetsov.bikeService.models.users.UserModel;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static ru.bikeservice.mainresources.models.support.EntitySupportService.createDtoFrom;

@RestController
@RequestMapping("/api/users")
public class UserControllerREST extends AbstractController {
    private final ReplyingKafkaTemplate<String, IndexKafkaDTO, List<AbstractShowableEntity>> indexKafkaTemplate;

    public UserControllerREST(ReplyingKafkaTemplate<String,
            IndexKafkaDTO, List<AbstractShowableEntity>> indexKafkaTemplate) {
        this.indexKafkaTemplate = indexKafkaTemplate;
    }

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
        try {
            response.put("documents", convertToDto(getItems(Document.class.getSimpleName(), userId)));
            response.put("fasteners", convertToDto(getItems(Fastener.class.getSimpleName(), userId)));
            response.put("manufacturers", convertToDto(getItems(Manufacturer.class.getSimpleName(), userId)));
            response.put("consumables", convertToDto(getItems(Consumable.class.getSimpleName(), userId)));
            response.put("tools", convertToDto(getItems(Tool.class.getSimpleName(), userId)));
            response.put("parts", convertToDto(getItems(Part.class.getSimpleName(), userId)));
            response.put("bikes", convertToDto(getItems(Bike.class.getSimpleName(), userId)));
            return ResponseEntity.ok(response);
        } catch(Exception e) {
          return ResponseEntity.internalServerError().build();
        }
    }

    private List<AbstractShowableEntity> getItems(String type, Long id) throws ExecutionException, InterruptedException {
        IndexKafkaDTO body = new IndexKafkaDTO(type, id, false, false);
        ProducerRecord<String, IndexKafkaDTO> record = new ProducerRecord<>("showIndex", body);
        RequestReplyFuture<String, IndexKafkaDTO, List<AbstractShowableEntity>> reply = indexKafkaTemplate.sendAndReceive(record);

        return reply.get().value();
    }

    private <T extends AbstractShowableEntity> List<AbstractEntityDto> convertToDto(List<T> list) {
        List<AbstractEntityDto> dtoList = new ArrayList<>();
        list.forEach(item -> dtoList.add(createDtoFrom(item)));

        return dtoList;
    }
}
