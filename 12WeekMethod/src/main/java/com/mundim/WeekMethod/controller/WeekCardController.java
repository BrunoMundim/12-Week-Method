package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.WeekCardDTO;
import com.mundim.WeekMethod.dto.update.UpdateWeekCardDTO;
import com.mundim.WeekMethod.entity.WeekCard;
import com.mundim.WeekMethod.service.WeekCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/weekcards")
@SecurityRequirement(name = "jwt")
public class WeekCardController {

    WeekCardService weekCardService;

    @Autowired
    public WeekCardController(WeekCardService weekCardService) {
        this.weekCardService = weekCardService;
    }

    @PostMapping
    @Operation(tags = "Week Card", summary = "Post a week card for the logged user")
    public ResponseEntity<WeekCard> createWeekCardForLoggedUser(@RequestBody WeekCardDTO weekCardDTO) {
        return new ResponseEntity<WeekCard>(weekCardService.createForLoggedUser(weekCardDTO), CREATED);
    }

    @GetMapping("/find-id")
    @Operation(tags = "Week Card", summary = "Get Week Card by ID")
    public ResponseEntity<WeekCard> findWeekCardById(@RequestParam Long weekCardId) {
        return ResponseEntity.ok(weekCardService.findById(weekCardId));
    }

    @GetMapping
    @Operation(tags = "Week Card", summary = "Get Week Cards by logged user")
    public ResponseEntity<List<WeekCard>> findWeekCardsForLoggedUser() {
        return ResponseEntity.ok(weekCardService.findForLoggedUser());
    }

    @PutMapping
    @Operation(tags = "Week Card", summary = "Update Week Card by ID")
    public ResponseEntity<WeekCard> updateWeekCardById(
            @RequestParam Long weekCardId,
            @RequestBody UpdateWeekCardDTO dto) {
        return ResponseEntity.ok(weekCardService.updateById(dto, weekCardId));
    }

    @DeleteMapping
    @Operation(tags = "Week Card", summary = "Delete Week Card by ID")
    public ResponseEntity<WeekCard> deleteWeekCardById(@RequestParam Long weekCardId) {
        return ResponseEntity.ok(weekCardService.deleteById(weekCardId));
    }

}
