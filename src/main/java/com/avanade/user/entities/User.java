package com.avanade.user.entities;

import com.avanade.user.payloads.request.RequestUser;
import com.avanade.user.payloads.response.ResponseUser;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    private LocalDate birthday;

    @CreationTimestamp
    @Column(nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Date updatedAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "address_id")
    private Address address;

    public User() {
    }

    public User(RequestUser request) {
        this.name = request.name();
        this.email = request.email();
        this.birthday = request.birthday();
        this.address = new Address(request.address());
    }

    public ResponseUser toResponse() {
        return new ResponseUser(id, name, email, birthday, address.toResponse(), createdAt, updatedAt);
    }
}
