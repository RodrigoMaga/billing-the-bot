CREATE TABLE participant (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY
                                 COMMENT 'Unique identifier for each participant',

                             name VARCHAR(100) NOT NULL
                                 COMMENT 'Full name of the participant',

                             email VARCHAR(150) NOT NULL
                                 COMMENT 'Email address of the participant',

                             phone VARCHAR(20) NOT NULL
                                 COMMENT 'Phone number of the participant',

                             billing_order INT NOT NULL
                                 COMMENT 'Sequence or position of the participant in the billing order',

                             notification_enable BOOLEAN NOT NULL DEFAULT TRUE
                                 COMMENT 'Enable or disable notifications for this participant',

                             created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                 COMMENT 'Creation timestamp',

                             updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                                 COMMENT 'Last update timestamp',

    CONSTRAINT uk_participant_email UNIQUE (email),
                             CONSTRAINT uk_participant_phone UNIQUE (phone),
                             CONSTRAINT uk_participant_order UNIQUE (billing_order)
)
    COMMENT = 'Stores registered participants for YouTube Premium billing';

CREATE TABLE payment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY
                             COMMENT 'Unique identifier for each payment record',

                         participant_id BIGINT NOT NULL
                             COMMENT 'Reference to the participant who owns this payment',

                         status ENUM('PAID', 'NOT_PAID') NOT NULL
                             COMMENT 'Payment status: PAID or NOT_PAID',

                         payment_month INT NOT NULL
                             COMMENT 'Month of the payment (1 to 12)',

                         payment_year INT NOT NULL
                             COMMENT 'Year of the payment (e.g. 2024)',

                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                             COMMENT 'Creation timestamp',

                         updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                             COMMENT 'Last update timestamp',

                         CONSTRAINT uk_payment_participant_month
                             UNIQUE (payment_month, payment_year),

                         CONSTRAINT fk_payment_participant
                             FOREIGN KEY (participant_id)
                                 REFERENCES participant(id)
                                 ON DELETE RESTRICT
                                 ON UPDATE CASCADE
)
    COMMENT = 'Stores monthly payment information for participants';