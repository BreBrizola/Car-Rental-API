package com.dentsu.bootcamp.mapping;

import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.model.VehicleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class VehicleMapper implements Function<VehicleEntity, VehicleDTO> {

    @Autowired
    private LocationMapper locationMapper;

    @Override
    public VehicleDTO apply(VehicleEntity vehicleEntity) {
        VehicleDTO vehicleDto = new VehicleDTO();

        vehicleDto.setId(vehicleEntity.getId());
        vehicleDto.setModel(vehicleEntity.getModel());
        vehicleDto.setPrice(vehicleEntity.getPrice());
        //vehicleDto.setLocation(locationMapper.apply(vehicleEntity.getLocation()));
        return vehicleDto;
    }
}
