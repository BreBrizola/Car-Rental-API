package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.VehicleTermsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleTermsRepository extends JpaRepository<VehicleTermsEntity, Long> {
    public List<VehicleTermsEntity> findByVehicleId(Long vehicleId);
}
