package com.chartercommunication.assesment.RetailPointsApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPointsDto {

    private String customerId;

    private String customerName;

    private List<PointsPerMonth> pointsPerMonth;

    private int totalPoints;


}
