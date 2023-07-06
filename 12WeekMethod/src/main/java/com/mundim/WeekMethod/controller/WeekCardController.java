package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.WeekCardDTO;
import com.mundim.WeekMethod.entity.WeekCard;
import com.mundim.WeekMethod.service.WeekCardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

// TODO Find week card by User ID
// TODO Criar WeekCardView para mostrar datas corretamente

@RestController
@RequestMapping("/weekcards")
@SecurityRequirement(name = "jwt")
public class WeekCardController {

    WeekCardService weekCardService;
    @Autowired
    public WeekCardController(WeekCardService weekCardService) {this.weekCardService = weekCardService;}


    @PostMapping
    public ResponseEntity<WeekCard> createWeekCard(@RequestBody WeekCardDTO weekCardDTO){
        return new ResponseEntity<WeekCard>(weekCardService.createWeekCard(weekCardDTO), CREATED);
    }

    @GetMapping
    public ResponseEntity<WeekCard> findWeekCardById(@RequestParam Long weekCardId){
        return ResponseEntity.ok(weekCardService.findWeekCardById(weekCardId));
    }

    @PutMapping
    public ResponseEntity<WeekCard> updateWeekCardById(
            @RequestParam Long weekCardId,
            @RequestBody WeekCardDTO weekCardDTO){
        return ResponseEntity.ok(weekCardService.updateWeekCardById(weekCardDTO, weekCardId));
    }

    @DeleteMapping
    public ResponseEntity<WeekCard> deleteWeekCardById(
            @RequestParam Long weekCardId,
            @RequestBody WeekCardDTO weekCardDTO){
        return ResponseEntity.ok(weekCardService.deleteWeekCardById(weekCardId));
    }

}
