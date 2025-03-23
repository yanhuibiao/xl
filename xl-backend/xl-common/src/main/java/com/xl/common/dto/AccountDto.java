package com.xl.common.dto;

import lombok.Data;

@Data
public class AccountDto {
    private String fromAccountId;
    private String toAccountId;
    private Double amount;
} 