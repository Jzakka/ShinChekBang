package com.ll.ShinChekBang.boundedContext.order.repository;

import com.ll.ShinChekBang.boundedContext.order.temporary.Bill;
import org.springframework.data.repository.CrudRepository;

public interface BillRepository extends CrudRepository<Bill, Long> {
}
