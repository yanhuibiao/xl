package com.xl.common.entity;

import com.xl.common.dubbo.dao.Administrator;
import com.xl.common.dubbo.dao.Customer;
import lombok.Data;


@Data
public class LoginDto extends Administrator {
    String captcha;
}
