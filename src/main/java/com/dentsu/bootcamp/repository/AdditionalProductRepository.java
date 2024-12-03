package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.AdditionalProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalProductRepository extends JpaRepository<AdditionalProductEntity, Long> {
}
