package com.rodmag.youtube_premium_billing_bot.repositories;

import com.rodmag.youtube_premium_billing_bot.entities.Payment;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findFirstByMonthAndYearAndParticipant_Id(Integer month, Integer year, Long participantId);
}
