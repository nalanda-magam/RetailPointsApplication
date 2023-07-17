package com.chartercommunication.assesment.RetailPointsApplication.controller;


import com.chartercommunication.assesment.RetailPointsApplication.dto.CustomerPointsDto;
import com.chartercommunication.assesment.RetailPointsApplication.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/report")
    public ResponseEntity<List<CustomerPointsDto>> getReport() {
        return ResponseEntity.ok(customerService.getReport());
    }

}
