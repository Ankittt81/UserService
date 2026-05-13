package com.smartcart.userservice.dtos.addressdto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAddressRequestDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Pattern(
            regexp = "\\d{10}",
            message = "Mobile number must be 10 digits"
    )
    private String mobile;

    @Pattern(
            regexp = "^$|\\d{10}",
            message = "Alternate mobile must be 10 digits"
    )
    private String alternateMobile;

    @NotBlank(message = "House number is required")
    private String houseNo;

    @NotBlank(message = "Area is required")
    private String area;

    private String landmark;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @Pattern(
            regexp = "\\d{6}",
            message = "Pincode must be 6 digits"
    )
    private String pincode;

    private String addressType;

    private String customLabel;
}
