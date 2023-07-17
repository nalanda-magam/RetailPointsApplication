package com.chartercommunication.assesment.RetailPointsApplication.repository;

import com.chartercommunication.assesment.RetailPointsApplication.model.PurchaseDetails;
import com.chartercommunication.assesment.RetailPointsApplication.projections.PurchaseDetailsFetchProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseDetailsRepository extends JpaRepository<PurchaseDetails, Long> {

    @Query(nativeQuery = true, value = "SELECT c.customer_name as customerName, c.customer_id as customerId," +
            " month(pd.purchase_timestamp) as monthOfPurchase, sum(pd.points) as points FROM PURCHASE_DETAILS pd" +
            " join customer c on c.id = pd.customer_id group by c.customer_id, month(pd.purchase_timestamp)" +
            " order by pd.customer_id")
    List<PurchaseDetailsFetchProjection> getPurchaseDetailsReport();
}