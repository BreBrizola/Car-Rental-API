package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.VehicleEntity;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends RxJava3CrudRepository<VehicleEntity, Long> {
}
