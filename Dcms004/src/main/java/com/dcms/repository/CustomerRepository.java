package com.dcms.repository;

import com.dcms.pojo.cus.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository {
    List<Customer> findAll(Integer page, Integer limit);

    Customer findById(String CustomerId);

    Integer getCount(String CustomerIdPrefix);

    int save(Customer customer);

    int update(Customer customer);

    int deleteById(String CustomerId);

    int getAllCount();

    List<Customer> findByName(String customerName);
}
