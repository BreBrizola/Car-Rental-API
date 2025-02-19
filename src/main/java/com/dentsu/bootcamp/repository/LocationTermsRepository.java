package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.LocationTermsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationTermsRepository extends JpaRepository<LocationTermsEntity, Long> {
    List<LocationTermsEntity> findByLocationId(Long locationId);
}
