package org.jayhenri.ecommerce.repository;

import org.jayhenri.ecommerce.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRegistrationRepository extends JpaRepository<Customer, UUID> {
}
