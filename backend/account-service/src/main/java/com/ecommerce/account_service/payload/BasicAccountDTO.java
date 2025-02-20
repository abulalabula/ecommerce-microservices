package com.ecommerce.account_service.payload;

public class BasicAccountDTO {
    private Long id;
    private String userEmail;
    private String userName;

    public BasicAccountDTO() {
    }

    public BasicAccountDTO(Long id, String userEmail, String userName) {
        this.id = id;
        this.userEmail = userEmail;
        this.userName = userName;
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
}

