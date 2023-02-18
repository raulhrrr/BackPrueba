package com.prueba.api.entities;

import com.prueba.api.utils.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "transaction", schema = "back")
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at", nullable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    public LocalDateTime updatedAt;

    @Column(name = "type_transaction", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_account", nullable = false)
    private Account account;

    @PrePersist
    void createAt() {
        this.createdAt = this.updatedAt = LocalDateTime.now();

    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

}
