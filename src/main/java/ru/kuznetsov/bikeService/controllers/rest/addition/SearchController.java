package ru.kuznetsov.bikeService.controllers.rest.addition;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDto;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.SearchService;

import java.security.Principal;
import java.util.List;

import static ru.kuznetsov.bikeService.models.fabric.EntitySupportService.convertListToDto;
import static ru.kuznetsov.bikeService.models.fabric.EntitySupportService.sortBasic;

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
                                            @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
                                            @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
                                            @RequestParam(name = "findBy", required = false, defaultValue = "standard") String findBy) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);

        List<AbstractShowableEntity> objects = searchService.doGlobalSearchProcedure(findBy, searchValue, userModel, shared);
        List<AbstractShowableEntity> sortedList = sortBasic(objects, sort);

        return convertListToDto(sortedList);
    }
}
