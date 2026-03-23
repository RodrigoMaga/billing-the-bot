package com.rodmag.billing_the_bot.repository;

import com.rodmag.billing_the_bot.entity.Payment;
import com.rodmag.billing_the_bot.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    Optional<Payment> findFirstByMonthAndYearAndParticipant_Id(Integer month, Integer year, Long participantId);
    Optional<Payment> findTopByOrderByIdDesc();
    List<Payment> findAllByPaymentStatus(PaymentStatus status);
}
