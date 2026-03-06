package com.rodmag.youtube_premium_billing_bot.service;

import com.rodmag.youtube_premium_billing_bot.entity.Participant;
import com.rodmag.youtube_premium_billing_bot.exception.BillingOrderOutOfRangeException;
import com.rodmag.youtube_premium_billing_bot.exception.ParticipantNotFoundException;
import com.rodmag.youtube_premium_billing_bot.exception.ResourceAlreadyExistsException;
import com.rodmag.youtube_premium_billing_bot.repository.ParticipantRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTest {

    @Mock
    ParticipantRepository  participantRepository;

    ParticipantService participantService;

    @BeforeEach
    void setup() {
            System.out.println("Setting up test environment...");
            participantService = new ParticipantService(participantRepository);
    }

    @Test
    void should_fail_when_inserting_participant_with_billingOrder_greater_than_6() {

        System.out.println("Running test: should_fail_when_inserting_participant_with_billingOrder_greater_than_6");
        // given

        Participant participant = new Participant();
        participant.setBillingOrder(null);

        Mockito.when(participantRepository.findMaxBillingOrder()).thenReturn(10);
        // when/then
        Assertions.assertThatThrownBy(() -> participantService.insert(participant))
                        .isInstanceOf(BillingOrderOutOfRangeException.class);
    }

    @Test
    void should_fail_when_inserting_participant_with_billingOrder_less_than_1() {

        System.out.println("Running test: should_fail_when_inserting_participant_with_billingOrder_less_than_1");
        // given
        Participant participant = new Participant();
        participant.setBillingOrder(null);

        Mockito.when(participantRepository.findMaxBillingOrder()).thenReturn(-5);
        // when/then
        Assertions.assertThatThrownBy(() -> participantService.insert(participant))
                        .isInstanceOf(BillingOrderOutOfRangeException.class);
    }

    @Test
    void should_insert_participant_with_valid_billingOrder() {

        System.out.println("Running test: should_insert_participant_with_valid_billingOrder");
        // given
        Participant participant = new Participant();
        participant.setBillingOrder(null);

        Mockito.when(participantRepository.findMaxBillingOrder()).thenReturn(3);
        Mockito.when(participantRepository.save(participant)).thenReturn(participant);
        // when
        Participant savedParticipant = participantService.insert(participant);
        // then
        Assertions.assertThat(savedParticipant).isNotNull();
    }

    @Test
    void should_fail_when_inserting_participant_with_existing_email() {

        System.out.println("Running test: should_fail_when_inserting_participant_with_existing_email");
        // given
        Participant participant = new Participant();
        participant.setEmail("test@email.com");
        participant.setPhone("699999");
        participant.setBillingOrder(null);

        Participant existingParticipant = new Participant();
        existingParticipant.setEmail("test@email.com");
        existingParticipant.setBillingOrder(1);

        Mockito.when(participantRepository.findMaxBillingOrder()).thenReturn(0);
        Mockito.when(participantRepository.findFirstByEmailOrPhoneOrBillingOrder(
                participant.getEmail(),
                participant.getPhone(), 1))
                .thenReturn(Optional.of(existingParticipant));

        // when/then
        Assertions.assertThatThrownBy(() -> participantService.insert(participant))
                .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    void should_fail_when_inserting_participant_with_existing_phone() {
        System.out.println("Running test: should_fail_when_inserting_participant_with_existing_email");
        // given
        Participant participant = new Participant();
        participant.setEmail("test@email.com");
        participant.setPhone("699999");
        participant.setBillingOrder(null);

        Participant existingParticipant = new Participant();
        existingParticipant.setEmail("test1@email.com");
        existingParticipant.setPhone("699999");
        existingParticipant.setBillingOrder(1);

        Mockito.when(participantRepository.findMaxBillingOrder()).thenReturn(0);
        Mockito.when(participantRepository.findFirstByEmailOrPhoneOrBillingOrder(
                        participant.getEmail(),
                        participant.getPhone(), 1))
                .thenReturn(Optional.of(existingParticipant));

        // when/then
        Assertions.assertThatThrownBy(() -> participantService.insert(participant))
                .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    void should_fail_when_inserting_participant_with_existing_billingOrder() {
        System.out.println("Running test: should_fail_when_inserting_participant_with_existing_billingOrder");
        // given
        Participant participant = new Participant();
        participant.setEmail("test@email.com");
        participant.setPhone("699999");
        participant.setBillingOrder(null);

        Participant existingParticipant = new Participant();
        existingParticipant.setEmail("test1@email.com");
        existingParticipant.setPhone("699998");
        existingParticipant.setBillingOrder(1);

        Mockito.when(participantRepository.findMaxBillingOrder()).thenReturn(0);
        Mockito.when(participantRepository.findFirstByEmailOrPhoneOrBillingOrder(
                        participant.getEmail(),
                        participant.getPhone(), 1))
                .thenReturn(Optional.of(existingParticipant));

        // when/then
        Assertions.assertThatThrownBy(() -> participantService.insert(participant))
                .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    void should_insert_participant_with_unique_email_phone_and_billingOrder() {
        System.out.println("Running test: should_insert_participant_with_unique_email_phone_and_billingOrder");
        // given
        Participant participant = new Participant();
        participant.setEmail("test@email.com");
        participant.setPhone("699999");
        participant.setBillingOrder(null);

        Mockito.when(participantRepository.findMaxBillingOrder()).thenReturn(0);
        Mockito.when(participantRepository.findFirstByEmailOrPhoneOrBillingOrder(
                        participant.getEmail(),
                        participant.getPhone(), 1))
                .thenReturn(Optional.empty());
        Mockito.when(participantRepository.save(participant)).thenReturn(participant);

        // when
        Participant savedParticipant = participantService.insert(participant);
        // then
        Assertions.assertThat(savedParticipant).isNotNull();
    }

    @Test
    void should_find_participant_by_id() {
        System.out.println("Running test: should_find_participant_by_id");

        // given
        Participant participant = new Participant();
        participant.setId(1L);
        Mockito.when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));

        // when
        Optional<Participant> foundParticipant = participantService.findById(1L);
        // then
        Assertions.assertThat(foundParticipant).isPresent();
        Assertions.assertThat(foundParticipant.get().getId()).isEqualTo(1L);
    }

    @Test
    void should_fail_when_participant_not_found_by_id() {
        System.out.println("Running test: should_throw_exception_when_participant_not_found_by_id");

        // given
        Mockito.when(participantRepository.findById(1L)).thenReturn(Optional.empty());

        // when/then
        Assertions.assertThatThrownBy(() -> participantService.findById(1L))
                .isInstanceOf(ParticipantNotFoundException.class);
    }


    @Nested
    @DisplayName("Find All Participants Tests")
    class FindAllTestSuite {

        @Test
        void should_find_all_participants() {
            System.out.println("Running test: should_find_all_participants");

            // given
            Participant participant1 = new Participant();
            Participant participant2 = new Participant();
            Mockito.when(participantRepository.findAll()).thenReturn(List.of(participant1,
                    participant2));

            // when
            List<Participant> participants = participantService.findAll();
            // then
            Assertions.assertThat(participants).hasSize(2);
            Mockito.verify(participantRepository, Mockito.times(1)).findAll();
        }

        @Test
        void should_return_empty_list_when_no_participants_found() {
            System.out.println("Running test: should_return_empty_list_when_no_participants_foind");

            // given
            Mockito.when(participantRepository.findAll()).thenReturn(List.of());

            // when
            List<Participant> participants = participantService.findAll();
            // then
            Assertions.assertThat(participants).isEmpty();
        }
    }
}





























