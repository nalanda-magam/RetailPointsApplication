package com.chartercommunication.assesment.RetailPointsApplication.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PurchaseRequest {

    private String customerId;

    private LocalDateTime purchaseTimestamp;

    private BigDecimal amount;
}
