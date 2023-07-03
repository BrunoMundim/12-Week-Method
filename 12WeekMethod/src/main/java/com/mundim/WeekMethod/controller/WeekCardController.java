package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.WeekCardDTO;
import com.mundim.WeekMethod.entity.WeekCard;
import com.mundim.WeekMethod.service.WeekCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

// TODO Find week card by User ID
//TODO Criar WeekCardView para mostrar datas corretamente

@RestController
@RequestMapping("/weekcards")
@Api(tags = "week card")
public class WeekCardController {

    WeekCardService weekCardService;
    @Autowired
    public WeekCardController(WeekCardService weekCardService) {this.weekCardService = weekCardService;}


    @PostMapping
    @ApiOperation(value = "Create Week Card")
    public ResponseEntity<WeekCard> createWeekCard(@RequestBody WeekCardDTO weekCardDTO){
        return new ResponseEntity<WeekCard>(weekCardService.createWeekCard(weekCardDTO), CREATED);
    }

    @GetMapping
    @ApiOperation(value = "Find Week Card by ID")
    public ResponseEntity<WeekCard> findWeekCardById(@RequestParam Long weekCardId){
        return ResponseEntity.ok(weekCardService.findWeekCardById(weekCardId));
    }

    @PutMapping
    @ApiOperation(value = "Update Week Card by ID")
    public ResponseEntity<WeekCard> updateWeekCardById(
            @RequestParam Long weekCardId,
            @RequestBody WeekCardDTO weekCardDTO){
        return ResponseEntity.ok(weekCardService.updateWeekCardById(weekCardDTO, weekCardId));
    }

    @DeleteMapping
    @ApiOperation(value = "Delete Week Card by ID")
    public ResponseEntity<WeekCard> deleteWeekCardById(
            @RequestParam Long weekCardId,
            @RequestBody WeekCardDTO weekCardDTO){
        return ResponseEntity.ok(weekCardService.deleteWeekCardById(weekCardId));
    }

}
