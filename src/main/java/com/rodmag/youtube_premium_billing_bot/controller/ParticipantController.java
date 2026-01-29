package com.rodmag.youtube_premium_billing_bot.controller;

import com.rodmag.youtube_premium_billing_bot.controller.dto.request.NewParticipantRequestDto;
import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import com.rodmag.youtube_premium_billing_bot.services.ParticipantService;
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
    public Participant insert(@RequestBody NewParticipantRequestDto obj){
        Participant participant = new Participant();
        participant.setName(obj.name());
        participant.setEmail(obj.email());
        participant.setPhone(obj.phone());
        participant.setBillingOrder(obj.billingOrder());
        return participantService.insert(participant);
    }

    @GetMapping
    public List<Participant> findAll() {
        return participantService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Optional<Participant> findById(@PathVariable Long id) {
        return participantService.findById(id);
    }

}
