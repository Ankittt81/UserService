package com.smartcart.userservice.controllers;

import com.smartcart.userservice.dtos.ApiResponse;
import com.smartcart.userservice.dtos.addressdto.AddressResponseDto;
import com.smartcart.userservice.dtos.addressdto.CreateAddressRequestDto;
import com.smartcart.userservice.security.CustomUserPrincipal;
import com.smartcart.userservice.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/addresses")
public class AddressController {
    private AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> addAddress(@Valid @RequestBody CreateAddressRequestDto dto, @AuthenticationPrincipal CustomUserPrincipal principal){
        AddressResponseDto response= addressService.addAddress(dto, principal.getUserId());
        return ResponseEntity.ok(new ApiResponse("Added Successfully",response));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getAddresses(@AuthenticationPrincipal CustomUserPrincipal principal){
        List<AddressResponseDto> response=addressService.getAddresses(principal.getUserId());
        return ResponseEntity.ok(new ApiResponse("Success ",response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long id){
        addressService.deleteAddress(id);
        return ResponseEntity.ok(new ApiResponse("Deleted Successfully",null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAddress(@PathVariable Long id,@RequestBody CreateAddressRequestDto createAddressRequestDto){
        AddressResponseDto response=addressService.updateAddress(id,createAddressRequestDto);
        return ResponseEntity.ok(new ApiResponse("Updated Successfully",response));
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<ApiResponse> setDefaultAddress(@PathVariable Long id,@AuthenticationPrincipal CustomUserPrincipal principal){
        addressService.setDefaultAddress(id,principal.getUserId());
        return ResponseEntity.ok(new ApiResponse("Updated Successfully",null));
    }
}
