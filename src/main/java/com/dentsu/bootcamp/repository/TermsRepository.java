package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.TermsEntity;
import com.dentsu.bootcamp.model.VehicleTermsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermsRepository extends JpaRepository<TermsEntity, Long> {
}
