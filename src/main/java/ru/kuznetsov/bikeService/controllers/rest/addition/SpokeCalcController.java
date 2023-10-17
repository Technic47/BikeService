package ru.kuznetsov.bikeService.controllers.rest.addition;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.dto.CalcDataDto;
import ru.bikeservice.mainresources.services.SpokeCalcService;

@RestController
@RequestMapping("/api/spokeCalc")
public class SpokeCalcController {
    private final SpokeCalcService service;

    public SpokeCalcController(SpokeCalcService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<Double> calculate(@RequestBody CalcDataDto data){
        double result = service.calculate(data);
        return ResponseEntity.ok(result);
    }
}
