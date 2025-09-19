package com.chatesito.financial.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String identificationType;

    @Column(nullable = false, unique = true, length = 11)
    private String identificationNumber;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column
    private LocalDate updatedAt;

    // Constructors
    public Client() {}

    public Client(Long id, String identificationType, String identificationNumber, String firstName, String lastName, String email, LocalDate birthDate, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getIdentificationType() {
        return identificationType;
    }
    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }
    public String getIdentificationNumber() {
        return identificationNumber;
    }
    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDate getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Auto set dates
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}