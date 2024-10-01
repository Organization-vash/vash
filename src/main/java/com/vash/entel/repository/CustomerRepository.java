package com.vash.entel.repository;

import com.vash.entel.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByDocNumber(Long docNumber);
    boolean existsByDocNumber(Long docNumber);
    boolean existsByFullname(String fullname);
}
