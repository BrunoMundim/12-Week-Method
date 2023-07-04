package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.WeekCardDTO;
import com.mundim.WeekMethod.entity.WeekCard;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.repository.WeekCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeekCardService {

    private WeekCardRepository weekcardRepository;

    @Autowired
    public WeekCardService(WeekCardRepository weekcardRepository){
        this.weekcardRepository = weekcardRepository;
    }

    public WeekCard createWeekCard(WeekCardDTO weekCardDTO){
        return weekcardRepository.save(new WeekCard(weekCardDTO));
    }

    public WeekCard findWeekCardById(Long weekCardId){
        return weekcardRepository.findById(weekCardId)
                .orElseThrow(() -> new BadRequestException("Week Card not found"));
    }

    public WeekCard updateWeekCardById(WeekCardDTO weekCardDTO, Long weekCardId){
        WeekCard weekCard = findWeekCardById(weekCardId);
        if(weekCardDTO.userId() != null) weekCard.setUserId(weekCardDTO.userId());
        if(weekCardDTO.description() != null) weekCard.setDescription(weekCardDTO.description());
        if(weekCardDTO.weekStartDate() != null) weekCard.setWeekStartDate(weekCardDTO.weekStartDate());
        if(weekCardDTO.weekEndDate() != null) weekCard.setWeekEndDate(weekCardDTO.weekEndDate());
        if(weekCardDTO.notes() != null) weekCard.setNotes(weekCardDTO.notes());
        return weekcardRepository.save(weekCard);
    }

    public WeekCard deleteWeekCardById(Long weekCardId){
        WeekCard weekCard = findWeekCardById(weekCardId);
        weekcardRepository.deleteById(weekCardId);
        return weekCard;
    }

    public WeekCard addTaskToWeekCard(Long taskId, Long weekCardId){
        WeekCard weekCard = findWeekCardById(weekCardId);
        weekCard.getWeekTasksIds().add(taskId);
        return weekcardRepository.save(weekCard);
    }

    public WeekCard removeTaskFromWeekCard(Long taskId, Long weekCardId){
        WeekCard weekCard = findWeekCardById(weekCardId);
        weekCard.getWeekTasksIds().remove(taskId);
        return weekcardRepository.save(weekCard);
    }

}
