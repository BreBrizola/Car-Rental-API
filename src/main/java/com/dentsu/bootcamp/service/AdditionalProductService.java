package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.model.AdditionalProductEntity;
import com.dentsu.bootcamp.repository.AdditionalProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionalProductService {

    @Autowired
    private AdditionalProductRepository additionalProductRepository;

    public AdditionalProductEntity getAdditionProducts(long id) {
        return additionalProductRepository.findById(id).get();
    }

    public List<AdditionalProductEntity> getAllAdditionalProducts() {
        return additionalProductRepository.findAll();
    }
}
