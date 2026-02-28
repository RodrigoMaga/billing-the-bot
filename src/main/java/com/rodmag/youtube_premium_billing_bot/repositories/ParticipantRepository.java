package com.rodmag.youtube_premium_billing_bot.repositories;

import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findFirstByEmailOrPhoneOrBillingOrder(String email, String phone, Integer billingOrder);
    @Query("SELECT COALESCE(MAX(p.billingOrder), 0) FROM Participant p")
    Integer findMaxBillingOrder();


}
