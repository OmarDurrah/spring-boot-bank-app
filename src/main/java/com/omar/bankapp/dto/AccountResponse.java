package com.omar.bankapp.dto;

public class AccountResponse {
    private Long id;
    private double balance;



    public AccountResponse(){}

    public AccountResponse(Long id , double balance)
    {
        this.balance = balance ; 
        this.id = id ; 
    }


    public double getBalance ()
    {
        return balance; 
    }

    

    public Long getId()
    {
        return id ; 
    }
}


