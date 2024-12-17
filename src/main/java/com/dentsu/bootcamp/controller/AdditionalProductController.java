package com.dentsu.bootcamp.controller;

import com.dentsu.bootcamp.dto.AdditionalProductDTO;
import com.dentsu.bootcamp.service.AdditionalProductService;
import io.reactivex.rxjava3.core.Observable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("additional-products")
public class AdditionalProductController {

    private final AdditionalProductService additionalProductService;

    public AdditionalProductController (AdditionalProductService additionalProductService){
        this.additionalProductService = additionalProductService;
    }

    @Operation(summary = "Retrieve all the additional products")
    @GetMapping("/ListAll")
    public Observable<List<AdditionalProductDTO>> getAllAdditionalProducts() {
        return additionalProductService.getAllAdditionalProducts();
    }

    @Operation(summary = "Retrieve a specific product, searched by Id", description = "Pass the product id (number).")
    @GetMapping("/{id}")
    public Observable<AdditionalProductDTO> getAdditionalProductById(@Parameter(description = "The unique identifier of the product.")@PathVariable(value = "id") Long id) {
        return additionalProductService.getAdditionProducts(id);
    }
}
