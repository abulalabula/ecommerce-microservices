package com.ecommerce.account_service.payload;

import com.ecommerce.account_service.entity.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PaymentMethodDTO {

    private Long id;

    @NotBlank(message = "Card type is required")
    private CardType cardType;

    @Pattern(regexp = "\\d{15,19}", message = "Card number must be between 15 and 19 digits")
    private String cardNumber; // Instead of exposing full card number

//    MM/YY: 0x or 1{0, 1, 2} and 2 digits year
    @NotBlank(message = "Expiry date cannot be empty")
    @Pattern(regexp = "(0[1-9]|1[0-2])/([0-9]{2})", message = "Expiry date must be in MM/YY format")
    private String expiryDate;

    public PaymentMethodDTO() {
    }

    public PaymentMethodDTO(Long id, CardType cardType, String cardNumber, String expiryDate) {
        this.id = id;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
