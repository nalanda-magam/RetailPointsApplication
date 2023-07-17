package com.chartercommunication.assesment.RetailPointsApplication.projections;

public interface PurchaseDetailsFetchProjection {
    String getCustomerId();

    String getCustomerName();

    Integer getMonthOfPurchase();

    Integer getPoints();
}
