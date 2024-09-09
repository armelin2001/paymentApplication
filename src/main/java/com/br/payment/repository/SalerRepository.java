package com.br.payment.repository;

import com.br.payment.entity.Saler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalerRepository extends JpaRepository<Saler, Long> {
    Optional<Saler> findByCode(String code);
}
