package com.br.payment.repository;

import com.br.payment.entity.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Long> {
    Optional<Charge> findByCode(String code);
}
