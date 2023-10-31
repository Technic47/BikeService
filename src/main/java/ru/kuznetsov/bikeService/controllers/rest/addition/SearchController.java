package ru.kuznetsov.bikeService.controllers.rest.addition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDto;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.dto.kafka.SearchKafkaDTO;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.users.UserModel;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static ru.bikeservice.mainresources.models.support.EntitySupportService.convertListToDto;
import static ru.bikeservice.mainresources.models.support.EntitySupportService.sortBasic;

@RestController
@RequestMapping("/api/search")
public class SearchController extends AbstractController {
    private final ReplyingKafkaTemplate<String, SearchKafkaDTO, EntityKafkaTransfer[]> searchTemplate;

    public SearchController(ReplyingKafkaTemplate<String, SearchKafkaDTO, EntityKafkaTransfer[]> searchTemplate) {
        this.searchTemplate = searchTemplate;
    }

    @Operation(summary = "Search")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search is ok",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "204", description = "No content found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AbstractEntityDto.class))})})
    @GetMapping()
    public ResponseEntity<List<AbstractEntityDto>> doSearch(Principal principal,
                                                            @RequestParam(name = "shared", required = false, defaultValue = "false") boolean shared,
                                                            @RequestParam(name = "searchValue") String searchValue,
                                                            @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
                                                            @RequestParam(name = "findBy", required = false, defaultValue = "standard") String findBy) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);

        List<AbstractShowableEntity> results;

        SearchKafkaDTO body = new SearchKafkaDTO(findBy, searchValue, userModel.getKafkaDto(), shared);

        ProducerRecord<String, SearchKafkaDTO> record = new ProducerRecord<>("createNewItem", body);
        RequestReplyFuture<String, SearchKafkaDTO, EntityKafkaTransfer[]> reply = searchTemplate.sendAndReceive(record);


        try {
            results = new ArrayList<>();
            EntityKafkaTransfer[] array = reply.get().value();
            for (EntityKafkaTransfer item : array) {
                results.add(item.getEntity());
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (results.size() == 0) {
            return ResponseEntity.status(204).build();
        }
        List<AbstractShowableEntity> sortedList = sortBasic(results, sort);

        return ResponseEntity.ok(convertListToDto(sortedList));
    }
}
