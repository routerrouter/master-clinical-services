package master.ao.accountancy.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import master.ao.accountancy.domain.enums.CostumerType;
import master.ao.accountancy.domain.enums.MovementStatus;
import master.ao.accountancy.domain.enums.MovementType;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CashMovement {

    private UUID cashMovementId;
    private UUID userId;
    private AccountNature nature;

   // private int movc_codigo,natcodigo, usucodigo,entcodigo,tmccodigo,status;

    private String movc_descricao,forma_pagamento;
    private String hora;
    private BigDecimal movementValue;
    private BigDecimal deliveredValue;
    private BigDecimal change; // troco
    private String recipient;
    private String requester;
    private UUID invoiceNumber;

    @Enumerated(value = EnumType.STRING)
    private CostumerType costumerType;  

    @Enumerated(value = EnumType.STRING)
    private MovementType movementType;


    @Enumerated(value = EnumType.STRING)
    private MovementStatus movementStatus;

    private String processNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate cashMovementDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;
}
