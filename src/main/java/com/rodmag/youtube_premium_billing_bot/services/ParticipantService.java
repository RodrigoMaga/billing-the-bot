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

        obj.validateBillingOrder();

        participantRepository.findByEmail(obj.getEmail())
                .ifPresent(p -> { throw new ResourceAlreadyExistsException("Email already exists: " + obj.getEmail());
                });
        participantRepository.findByPhone(obj.getPhone())
                .ifPresent(p -> {throw new ResourceAlreadyExistsException("Phone number already exists: " + obj.getPhone());
                });

        participantRepository.findByBillingOrder(obj.getBillingOrder())
                .ifPresent(p -> {throw new ResourceAlreadyExistsException("Billing order already exists: " + obj.getBillingOrder());
                });
        return participantRepository.save(obj);
    }

    public List<Participant> findAll() {
        return participantRepository.findAll();
    }

    public Optional<Participant> findById(Long id) {
        return participantRepository.findById(id);
    }
}
