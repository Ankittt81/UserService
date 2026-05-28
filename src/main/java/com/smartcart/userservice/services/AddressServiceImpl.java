package com.smartcart.userservice.services;

import com.smartcart.userservice.dtos.addressdto.AddressResponseDto;
import com.smartcart.userservice.dtos.addressdto.CreateAddressRequestDto;
import com.smartcart.userservice.mappers.AddressMapper;
import com.smartcart.userservice.models.Address;
import com.smartcart.userservice.models.User;
import com.smartcart.userservice.repositories.AddressRepository;
import com.smartcart.userservice.repositories.UserRepository;
import com.smartcart.userservice.security.CustomUserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    private UserRepository userRepository;
    private AddressRepository addressRepository;
    private AddressMapper addressMapper;

    public AddressServiceImpl(UserRepository userRepository, AddressRepository addressRepository, AddressMapper addressMapper) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }
    @Override
    public AddressResponseDto addAddress(CreateAddressRequestDto dto,Long userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not found"));
        long addressCount=addressRepository.countByUser(user);
        if(addressCount>=3){
            throw new RuntimeException("Maximum Address limit reached");
        }
        Address address=addressMapper.toEntity(dto);
        boolean hasAdress=addressRepository.existsByUser(user);
        if(!hasAdress){
            address.setIsDefault(true);
        }
        address.setUser(user);


        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public List<AddressResponseDto> getAddresses(Long  userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not found"));
        List<Address> addresses=addressRepository.findByUser(user);
        List<AddressResponseDto> responses= addresses.stream().map(address -> addressMapper.toDto(address)).toList();
        return responses;
    }

    @Override
    public AddressResponseDto getAddressById(CustomUserPrincipal principal, Long addressId) {
        Address  address=addressRepository.findById(addressId).orElseThrow(()->new RuntimeException("Address Not found"));
        //Role Check
        boolean isAdmin=principal.getRoles().contains("ROLE_ADMIN");
        //OwnerShip Validation
        if(!isAdmin  && !address.getUser().getId().equals(principal.getUserId())){
            throw new RuntimeException("Unauthorized Access");
        }
        return addressMapper.toDto(address);
    }

    @Override
    public AddressResponseDto updateAddress(Long addressId,CreateAddressRequestDto dto) {
        Address address=addressRepository.findById(addressId).orElseThrow(()->new RuntimeException("Address Not found"));

         address=addressMapper.toUpdate(dto,address);

        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long addressId) {
        Address address=addressRepository.findById(addressId).orElseThrow(()->new RuntimeException("Address Not found"));
        addressRepository.delete(address);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long addressId,Long  userId) {
        Address address=addressRepository.findById(addressId).orElseThrow(()->new RuntimeException("Address Not found"));
        addressRepository.clearDefaultForUser(userId);
        address.setIsDefault(true);
    }

}
