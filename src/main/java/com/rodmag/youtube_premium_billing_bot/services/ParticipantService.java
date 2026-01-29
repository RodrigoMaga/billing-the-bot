package com.rodmag.youtube_premium_billing_bot.services;

import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import com.rodmag.youtube_premium_billing_bot.exceptions.DatabaseException;
import com.rodmag.youtube_premium_billing_bot.repositories.ParticipantRepository;
import com.rodmag.youtube_premium_billing_bot.services.exceptions.ResourceAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public Participant insert(Participant obj) {
        participantRepository.findByEmail(obj.getEmail())
                .ifPresent(p -> { throw new ResourceAlreadyExistsException("Email already exists: " + obj.getEmail());
                });
        participantRepository.findByPhone(obj.getPhone())
                .ifPresent(p -> {throw new ResourceAlreadyExistsException("Phone number already exists: " + obj.getPhone());
                });
        if (obj.getBillingOrder() > 1 && obj.getBillingOrder() < 6) {
            participantRepository.findByBillingOrder(obj.getBillingOrder()).ifPresent(p -> {throw new ResourceAlreadyExistsException("Billing Order already exists: " + obj.getBillingOrder());
            });
        } else if (obj.getBillingOrder() < 1 || obj.getBillingOrder() > 6) {
            throw new DatabaseException("The order must be from 1 to 6");
        }
        return participantRepository.save(obj);
    }

    public List<Participant> findAll() {
        return participantRepository.findAll();
    }

    public Optional<Participant> findById(Long id) {
        return participantRepository.findById(id);
    }
}
