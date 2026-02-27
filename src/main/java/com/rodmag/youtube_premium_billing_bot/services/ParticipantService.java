package com.rodmag.youtube_premium_billing_bot.services;

import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import com.rodmag.youtube_premium_billing_bot.exceptions.ParticipantNotFoundException;
import com.rodmag.youtube_premium_billing_bot.repositories.ParticipantRepository;
import com.rodmag.youtube_premium_billing_bot.exceptions.ResourceAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public Participant insert(Participant obj) {

        Integer maxBillingOrder = participantRepository.findMaxBillingOrder();
        obj.setBillingOrder(maxBillingOrder + 1);
        obj.validateBillingOrder();

        participantRepository.findFirstByEmailOrPhoneOrBillingOrder(obj.getEmail(), obj.getPhone(), obj.getBillingOrder())
                .ifPresent(participant -> {
                    if (participant.getEmail().equals(obj.getEmail())) {
                        throw new ResourceAlreadyExistsException("Email already exists: " + obj.getEmail());
                    }
                    if (participant.getPhone().equals(obj.getPhone())) {
                        throw new ResourceAlreadyExistsException("Phone number already exists: " + obj.getPhone());
                    }
                    if (participant.getBillingOrder().equals(obj.getBillingOrder())) {
                        throw new ResourceAlreadyExistsException("Billing order already exists: " + obj.getBillingOrder());
                    }
                }
        );

        return participantRepository.save(obj);

    }

    public List<Participant> findAll() {
        return participantRepository.findAll();
    }

    public Optional<Participant> findById(Long id) {
        return Optional.of(participantRepository.findById(id)
                .orElseThrow(() -> new ParticipantNotFoundException("Participant not found with id: " + id)));
    }
}