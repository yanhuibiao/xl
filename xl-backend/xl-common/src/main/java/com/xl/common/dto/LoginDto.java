package com.xl.common.dto;

import com.xl.common.dubbo.entity.Administrator;
import lombok.Data;


@Data
public class LoginDto extends Administrator {
    String captcha;
}
