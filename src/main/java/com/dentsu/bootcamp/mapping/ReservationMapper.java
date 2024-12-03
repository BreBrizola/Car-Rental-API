package com.dentsu.bootcamp.mapping;

import com.dentsu.bootcamp.dto.AdditionalProductDTO;
import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.ReservationDTO;
import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.exception.LocationNotFoundException;
import com.dentsu.bootcamp.exception.VehicleNotFoundException;
import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.ReservationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.LocationRepository;
import com.dentsu.bootcamp.repository.VehicleRepository;
import com.dentsu.bootcamp.service.LocationService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ReservationMapper implements Function<ReservationEntity, ReservationDTO> {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private LocationService locationService;


    @Override
    public ReservationDTO apply(ReservationEntity reservationEntity) {
        ReservationDTO reservationDto = new ReservationDTO();
        reservationDto.setId(reservationEntity.getId());
        reservationDto.setConfirmationNumber(reservationEntity.getConfirmationNumber());
        reservationDto.setFirstName(reservationEntity.getFirstName());
        reservationDto.setLastName(reservationEntity.getLastName());
        reservationDto.setTotalPrice(reservationEntity.getTotalPrice());
        reservationDto.setEmail(reservationEntity.getEmail());
        reservationDto.setPhone(reservationEntity.getPhone());
        reservationDto.setPickupTime(reservationEntity.getPickupTime());
        reservationDto.setReturnTime(reservationEntity.getReturnTime());
        reservationDto.setPickupDate(reservationEntity.getPickupDate());
        reservationDto.setReturnDate(reservationEntity.getReturnDate());

        if (reservationEntity.getPickupLocation() != null) {
            LocationEntity pickupEntity = locationRepository.findById(reservationEntity.getPickupLocation().getId())
                    .orElseThrow(() -> new LocationNotFoundException("Pickup location not found"));

            LocationDTO pickupDto = locationMapper.apply(pickupEntity);
            pickupDto.setWeather(locationService.getLocationWeather(reservationEntity.getPickupLocation()));
            reservationDto.setPickupLocation(pickupDto);
        }

        if (reservationEntity.getReturnLocation() != null) {
            LocationEntity returnEntity = locationRepository.findById(reservationEntity.getReturnLocation().getId())
                    .orElseThrow(() -> new LocationNotFoundException("Return location not found"));

            LocationDTO returnDto = locationMapper.apply(returnEntity);
            //returnDto.setWeather(locationService.getLocationWeather(reservationEntity.getReturnLocation()));
            reservationDto.setReturnLocation(returnDto);
        }

        if (reservationEntity.getVehicle() != null){
            VehicleEntity vehicleEntity = vehicleRepository.findById(reservationEntity.getVehicle().getId())
                    .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));

            VehicleDTO vehicleDto = vehicleMapper.apply(vehicleEntity);
            reservationDto.setVehicle(vehicleDto);
        }

        if (reservationEntity.getAdditionalProducts() != null) {
            List<AdditionalProductDTO> additionalProductDTOs = reservationEntity.getAdditionalProducts().stream()
                    .map(additionalProduct -> {
                        AdditionalProductDTO additionalProductDTO = new AdditionalProductDTO();
                        additionalProductDTO.setId(additionalProduct.getId());
                        additionalProductDTO.setName(additionalProduct.getName());
                        additionalProductDTO.setPrice(additionalProduct.getPrice());
                        return additionalProductDTO;
                    })
                    .collect(Collectors.toList());

            reservationDto.setAdditionalProducts(additionalProductDTOs);
        }

        return reservationDto;
    }
}
