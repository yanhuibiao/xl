package com.xl.transaction.service;

import com.xl.common.entity.AccountDto;
import com.xl.common.entity.ResponseEntity;

public interface TransferService {
    ResponseEntity<?> transfer(AccountDto transferRequest);
} 