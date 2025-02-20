package com.ecommerce.account_service.payload;

import com.ecommerce.account_service.entity.Address;
import com.ecommerce.account_service.entity.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AccountDTO {

    private Long id;
//  Add validation in dto, not in entity
    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    private String userEmail;

    @NotBlank(message = "User name cannot be empty")
    @Size(min = 3, max = 50, message = "User name must be between 3 and 50 characters")
    private String userName;

    private Address shippingAddress;
    private Address billingAddress;
    private PaymentMethod paymentMethod;

    public AccountDTO() {
    }

    public AccountDTO(Long id, String userEmail, String userName, Address shippingAddress, Address billingAddress, PaymentMethod paymentMethod) {
        this.id = id;
        this.userEmail = userEmail;
        this.userName = userName;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
