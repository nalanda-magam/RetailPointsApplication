package com.chartercommunication.assesment.RetailPointsApplication.service;

import com.chartercommunication.assesment.RetailPointsApplication.dto.CustomerPointsDto;
import com.chartercommunication.assesment.RetailPointsApplication.dto.PointsPerMonth;
import com.chartercommunication.assesment.RetailPointsApplication.dto.PurchaseRequest;
import com.chartercommunication.assesment.RetailPointsApplication.model.Customer;
import com.chartercommunication.assesment.RetailPointsApplication.model.PurchaseDetails;
import com.chartercommunication.assesment.RetailPointsApplication.projections.PurchaseDetailsFetchProjection;
import com.chartercommunication.assesment.RetailPointsApplication.repository.CustomerRepository;
import com.chartercommunication.assesment.RetailPointsApplication.repository.PurchaseDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PurchaseDetailsRepository purchaseDetailsRepository;

    /* Method to add a transaction */
    public void purchase(PurchaseRequest purchaseRequest) {
        LOGGER.info("========================--Customer checked in to Purchase--========================");
        if (!validateRequest(purchaseRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }

        Customer customer = customerRepository.findByCustomerId(purchaseRequest.getCustomerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid customer ID"));
        LOGGER.info("========================--Customer fetched - {}--========================", customer.getCustomerId());

        PurchaseDetails purchaseDetails = new PurchaseDetails().setCustomerId(customer.getId()).setPurchaseTimestamp(purchaseRequest.getPurchaseTimestamp()).setPoints(calculatePoints(purchaseRequest.getAmount()));
        purchaseDetailsRepository.save(purchaseDetails);
        LOGGER.info("========================--Purchase points for customer - {} are - {}--========================", customer.getCustomerId(), purchaseDetails.getPoints());
    }

    /* To record collection of transactions */
    public void purchase(List<PurchaseRequest> purchaseRequest) {
        purchaseRequest.forEach(this::purchase);
    }

    /* Method to get a report of Customer's
    three-month period earned reward
    points per month and total. */
    public List<CustomerPointsDto> getReport() {
        //Map to group data as per requirement
        Map<String, Map<Integer, List<PurchaseDetailsFetchProjection>>> report = purchaseDetailsRepository.getPurchaseDetailsReport().stream()
                .collect(Collectors.groupingBy(PurchaseDetailsFetchProjection::getCustomerId,
                        Collectors.groupingBy(PurchaseDetailsFetchProjection::getMonthOfPurchase)));
        List<CustomerPointsDto> response = new ArrayList<>();

        //Logic to Flatten the map into an Object List
        for (Map.Entry<String, Map<Integer, List<PurchaseDetailsFetchProjection>>> e1 : report.entrySet()) {
            CustomerPointsDto customerPointsDto = new CustomerPointsDto().setCustomerId(e1.getKey());
            List<PointsPerMonth> pointsPerMonths = new ArrayList<>();
            int total = 0;
            for (Map.Entry<Integer, List<PurchaseDetailsFetchProjection>> e2 : e1.getValue().entrySet()) {
                if (customerPointsDto.getCustomerName() == null) {
                    customerPointsDto.setCustomerName(e2.getValue().get(0).getCustomerName());
                }
                int monthlyPoints = e2.getValue().stream().mapToInt(PurchaseDetailsFetchProjection::getPoints).sum();
                PointsPerMonth pointsPerMonth = new PointsPerMonth().setMonth(Month.of(e2.getKey()).name()).setPoints(monthlyPoints);
                total += monthlyPoints;
                pointsPerMonths.add(pointsPerMonth);
            }
            customerPointsDto.setTotalPoints(total).setPointsPerMonth(pointsPerMonths);
            response.add(customerPointsDto);
        }
        return response;
    }

    /* Points Calculation */
    private int calculatePoints(BigDecimal amount) {
        int roundOffAmount = amount.intValue(); //120
        int points = 0;
        if (roundOffAmount > 100) { //120 > 100
            points += ((roundOffAmount - 100) * 2) + 50; // 120 - 100 * 2
        } else if (roundOffAmount > 50) {
            points += roundOffAmount - 50;
        }
        return points;
    }

    /* To validate a Purchase Request */
    private boolean validateRequest(PurchaseRequest request) {
        if (request.getCustomerId() == null || request.getCustomerId().length() == 0) {
            return false;
        }
        if (request.getPurchaseTimestamp() == null) {
            return false;
        }
        return request.getAmount() != null || request.getAmount().intValue() <= BigDecimal.ZERO.intValue();
    }
}
