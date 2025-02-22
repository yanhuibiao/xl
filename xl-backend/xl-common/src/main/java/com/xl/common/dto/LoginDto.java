package com.xl.common.dto;

import com.xl.common.dubbo.dao.Customer;
import lombok.Data;

@Data
public class LoginDto extends Customer {
    String captcha;
}
