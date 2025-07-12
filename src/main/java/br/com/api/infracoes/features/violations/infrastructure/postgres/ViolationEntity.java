package br.com.api.infracoes.features.violations.infrastructure.postgres;

import br.com.api.infracoes.features.equipments.infrastructure.postgres.EquipmentEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "violations")
public class ViolationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "equipments_serial", nullable = false, insertable = false, updatable = false)
    private EquipmentEntity equipmentsSerial;

    @NotNull
    @Column(name = "equipments_serial", nullable = false)
    private String equipmentsSerialId;

    @NotNull
    @Column(name = "occurrence_date", nullable = false)
    private OffsetDateTime occurrenceDate;

    @NotNull
    @Column(name = "measured_speed", nullable = false)
    private Double measuredSpeed;

    @NotNull
    @Column(name = "considered_speed", nullable = false)
    private Double consideredSpeed;

    @NotNull
    @Column(name = "regulated_speed", nullable = false)
    private Double regulatedSpeed;

    @Size(max = 300)
    @NotNull
    @Column(name = "picture", nullable = false, length = 300)
    private String picture;

    @Size(max = 70)
    @NotNull
    @Column(name = "type", nullable = false, length = 70)
    private String type;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

}