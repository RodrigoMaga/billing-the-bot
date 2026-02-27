package com.rodmag.youtube_premium_billing_bot.controllers;

import com.rodmag.youtube_premium_billing_bot.controllers.dto.request.NewParticipantRequestDto;
import com.rodmag.youtube_premium_billing_bot.controllers.dto.response.ParticipantResponseDto;
import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import com.rodmag.youtube_premium_billing_bot.services.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/participants")
@Validated
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ParticipantResponseDto insert(@Valid @RequestBody NewParticipantRequestDto obj){
        Participant participant = new Participant();
        participant.setName(obj.name());
        participant.setEmail(obj.email());
        participant.setPhone(obj.phone());
        participant.setNotificationEnable(obj.notificationEnable());
        Participant saved = participantService.insert(participant);
        return new ParticipantResponseDto(saved);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ParticipantResponseDto> findAll() {
        return participantService.findAll()
                .stream()
                .map(ParticipantResponseDto::new)
                .toList();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public Optional<ParticipantResponseDto> findById(@PathVariable Long id) {
        return participantService.findById(id)
                .map(ParticipantResponseDto::new);
    }

}
