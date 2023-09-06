package ru.kuznetsov.bikeService.controllers.rest.addition;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.abstracts.CommonEntityController;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController extends CommonEntityController {
    @GetMapping()
    public ResponseEntity doSearch(Principal principal,
                                   @RequestParam(name = "shared", required = false, defaultValue = "false") boolean shared,
                                   @RequestParam(name = "value", defaultValue = "") String value,
                                   @RequestParam(name = "findBy", required = false, defaultValue = "standard") String findBy){
            List<AbstractShowableEntity> objects = this.doGlobalSearchProcedure(findBy, value, principal, shared);
    }
}
