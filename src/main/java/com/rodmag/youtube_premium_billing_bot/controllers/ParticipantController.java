package com.rodmag.youtube_premium_billing_bot.controllers;

import com.rodmag.youtube_premium_billing_bot.controllers.dto.request.NewParticipantRequestDto;
import com.rodmag.youtube_premium_billing_bot.controllers.dto.response.ParticipantResponseDto;
import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import com.rodmag.youtube_premium_billing_bot.services.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/participants")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public ParticipantResponseDto insert(@RequestBody @Valid NewParticipantRequestDto obj){
        Participant participant = new Participant();
        participant.setName(obj.name());
        participant.setEmail(obj.email());
        participant.setPhone(obj.phone());
        participant.setBillingOrder(obj.billingOrder());
        Participant saved = participantService.insert(participant);
        return new ParticipantResponseDto(saved);
    }

    @GetMapping
    public List<ParticipantResponseDto> findAll() {
        return participantService.findAll()
                .stream()
                .map(ParticipantResponseDto::new)
                .toList();
    }

    @GetMapping(value = "/{id}")
    public Optional<ParticipantResponseDto> findById(@PathVariable Long id) {
        return participantService.findById(id)
                .map(ParticipantResponseDto::new);
    }

}
