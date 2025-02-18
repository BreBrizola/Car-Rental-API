package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.model.TermsEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.model.VehicleTermsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleTermsRepository extends JpaRepository<VehicleTermsEntity, Long> {
    public List<VehicleTermsEntity> findByVehicle(VehicleEntity vehicle);
}
