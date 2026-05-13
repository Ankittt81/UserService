package com.smartcart.userservice.mappers;


import com.smartcart.userservice.dtos.addressdto.AddressResponseDto;
import com.smartcart.userservice.dtos.addressdto.CreateAddressRequestDto;
import com.smartcart.userservice.models.Address;
import com.smartcart.userservice.models.AddressType;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(CreateAddressRequestDto dto){
        Address address=new Address();
        address.setFullName(dto.getFullName());
        address.setMobile(dto.getMobile());
        address.setAlternateMobile(dto.getAlternateMobile());
        address.setHouseNo(dto.getHouseNo());
        address.setArea(dto.getArea());
        address.setLandmark(dto.getLandmark());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        address.setAddressType(AddressType.valueOf(dto.getAddressType()));
        address.setCustomLabel(dto.getCustomLabel());
        return address;
    }

    public AddressResponseDto toDto(Address address){
        AddressResponseDto addressResponseDto=new AddressResponseDto();
        addressResponseDto.setFullName(address.getFullName());
        addressResponseDto.setMobile(address.getMobile());
        addressResponseDto.setAddressType(address.getAddressType());
        addressResponseDto.setId(address.getId());
        addressResponseDto.setHouseNo(address.getHouseNo());
        addressResponseDto.setArea(address.getArea());
        addressResponseDto.setCity(address.getCity());
        addressResponseDto.setState(address.getState());
        addressResponseDto.setPincode(address.getPincode());
        addressResponseDto.setIsDefault(address.getIsDefault());
        addressResponseDto.setCustomLabel(address.getCustomLabel());
        addressResponseDto.setLandmark(address.getLandmark());
        addressResponseDto.setAlternateMobile(address.getAlternateMobile());

        return addressResponseDto;
    }
    public Address toUpdate(CreateAddressRequestDto dto,Address address){
        address.setFullName(dto.getFullName());
        address.setMobile(dto.getMobile());
        address.setAlternateMobile(dto.getAlternateMobile());
        address.setHouseNo(dto.getHouseNo());
        address.setArea(dto.getArea());
        address.setLandmark(dto.getLandmark());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        address.setAddressType(AddressType.valueOf(dto.getAddressType()));
        address.setCustomLabel(dto.getCustomLabel());
        return address;
    }
}
