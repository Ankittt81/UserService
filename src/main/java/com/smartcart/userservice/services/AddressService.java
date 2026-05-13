package com.smartcart.userservice.services;

import com.smartcart.userservice.dtos.addressdto.AddressResponseDto;
import com.smartcart.userservice.dtos.addressdto.CreateAddressRequestDto;

import java.util.List;

public interface AddressService {
    AddressResponseDto addAddress(CreateAddressRequestDto dto,Long userId);
    List<AddressResponseDto> getAddresses(Long  userId);
    AddressResponseDto updateAddress(Long addressId,CreateAddressRequestDto dto);
    void deleteAddress(Long id);
    void setDefaultAddress(Long addressId,Long userId);
}
