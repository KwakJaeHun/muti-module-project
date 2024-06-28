package com.jhkwak.orderservice.repository.refund;

import com.jhkwak.orderservice.entity.refund.RefundList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<RefundList, Long> {

    List<RefundList> findByRefundRequestOnBeforeAndStatus(LocalDate now, char s);
}
