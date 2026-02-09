package com.rodmag.youtube_premium_billing_bot.entities;

import com.rodmag.youtube_premium_billing_bot.exceptions.DatabaseException;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "participant")
public class Participant implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",  nullable = false, length = 100)
    private String name;

    @Column(name = "email",  nullable = false, length = 150)
    private String email;

    @Column(name = "phone",  nullable = false, length = 20)
    private String phone;

    @Column(name = "billing_order",  nullable = false)
    private Integer billingOrder;

    @OneToMany(mappedBy = "participant")
    private List<Payment> payments = new ArrayList<>();

    public Participant() {
    }

    public Participant(Long id, String name, String email, String phone, Integer billingOrder) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.billingOrder = billingOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getBillingOrder() {
        return billingOrder;
    }

    public void setBillingOrder(Integer billingOrder) {
        this.billingOrder = billingOrder;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Participant that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", order=" + billingOrder +
                '}';
    }

    public void validateBillingOrder() {
        if (billingOrder == null || billingOrder < 1 || billingOrder > 6)
            throw new DatabaseException("Billing order must be between 1 and 6");
    }
}


