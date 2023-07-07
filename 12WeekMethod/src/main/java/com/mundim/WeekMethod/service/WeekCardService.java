package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.WeekCardDTO;
import com.mundim.WeekMethod.dto.update.UpdateWeekCardDTO;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.entity.WeekCard;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.exception.UnauthorizedRequestException;
import com.mundim.WeekMethod.repository.WeekCardRepository;
import com.mundim.WeekMethod.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeekCardService {

    private final WeekCardRepository weekcardRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public WeekCardService(WeekCardRepository weekcardRepository,
                           UserService userService,
                           AuthenticationService authenticationService) {
        this.weekcardRepository = weekcardRepository;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public WeekCard createWeekCardForLoggedUser(WeekCardDTO weekCardDTO) {
        Long userId = authenticationService.findUserByBearer().getId();
        return weekcardRepository.save(new WeekCard(weekCardDTO, userId));
    }

    public List<WeekCard> findWeekCardsForLoggedUser() {
        Long userId = authenticationService.findUserByBearer().getId();
        return weekcardRepository.findWeekCardByUserId(userId);
    }

    public WeekCard findWeekCardById(Long weekCardId) {
        verifyUserAuthorizationForWeekCard(weekCardId);
        return weekcardRepository.findById(weekCardId)
                .orElseThrow(() -> new BadRequestException("Week Card not found"));
    }

    public WeekCard updateWeekCardById(UpdateWeekCardDTO dto, Long weekCardId) {
        WeekCard weekCard = findWeekCardById(weekCardId);
        verifyUserAuthorizationForWeekCard(weekCardId);

        if (dto.description() != null) weekCard.setDescription(dto.description());
        if (dto.weekStartDate() != null) {
            weekCard.setWeekStartDate(dto.weekStartDate());
            weekCard.setWeekEndDate(weekCard.getWeekStartDate().plusDays(7));
        }
        if (dto.notes() != null) weekCard.setNotes(dto.notes());

        return weekcardRepository.save(weekCard);
    }

    public WeekCard deleteWeekCardById(Long weekCardId) {
        verifyUserAuthorizationForWeekCard(weekCardId);
        WeekCard weekCard = findWeekCardById(weekCardId);
        weekcardRepository.deleteById(weekCardId);
        return weekCard;
    }

    public WeekCard addTaskToWeekCard(Long taskId, Long weekCardId) {
        verifyUserAuthorizationForWeekCard(weekCardId);
        WeekCard weekCard = findWeekCardById(weekCardId);
        weekCard.getWeekTasksIds().add(taskId);
        return weekcardRepository.save(weekCard);
    }

    public WeekCard removeTaskFromWeekCard(Long taskId, Long weekCardId) {
        verifyUserAuthorizationForWeekCard(weekCardId);
        WeekCard weekCard = findWeekCardById(weekCardId);
        weekCard.getWeekTasksIds().remove(taskId);
        return weekcardRepository.save(weekCard);
    }

    public void verifyUserAuthorizationForWeekCard(Long weekCardId) {
        User user = authenticationService.findUserByBearer();
        WeekCard weekCard = weekcardRepository.findById(weekCardId)
                .orElseThrow(() -> new BadRequestException("Week Card not found"));

        if (!weekCard.getUserId().equals(user.getId()) &&
                !user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            throw new UnauthorizedRequestException("Unauthorized action");
    }

}
