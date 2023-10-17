package ru.kuznetsov.bikeService.controllers.rest.addition;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDto;
import ru.bikeservice.mainresources.models.users.UserModel;
import ru.bikeservice.mainresources.services.SearchService;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static ru.bikeservice.mainresources.models.support.EntitySupportService.convertListToDto;
import static ru.bikeservice.mainresources.models.support.EntitySupportService.sortBasic;

@RestController
@RequestMapping("/api/search")
public class SearchController extends AbstractController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }


    @GetMapping()
    public List<AbstractEntityDto> doSearch(Principal principal,
                                            @RequestParam(name = "shared", required = false, defaultValue = "false") boolean shared,
                                            @RequestParam(name = "searchValue") String searchValue,
                                            @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
                                            @RequestParam(name = "findBy", required = false, defaultValue = "standard") String findBy) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);

        List<AbstractShowableEntity> objects = null;
        try {
            objects = searchService.doGlobalSearchProcedure(findBy, searchValue, userModel, shared);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<AbstractShowableEntity> sortedList = sortBasic(objects, sort);

        return convertListToDto(sortedList);
    }
}
