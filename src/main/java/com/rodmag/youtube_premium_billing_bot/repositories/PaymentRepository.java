package com.rodmag.youtube_premium_billing_bot.repositories;

import com.rodmag.youtube_premium_billing_bot.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    Optional<Payment> findFirstByMonthAndYearAndParticipant_Id(Integer month, Integer year, Long participantId);
}
