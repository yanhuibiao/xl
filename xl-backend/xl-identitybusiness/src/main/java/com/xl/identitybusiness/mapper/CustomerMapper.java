package com.xl.identitybusiness.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xl.common.dubbo.dao.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    @Select("select * from xl_users.customer where id=#{id}")
    Customer mybatisGetById(String id);

    int mybatisSave(@Param("customer") Customer customer);

    int mybatisSave1(@Param("id") String id,
                          @Param("username") String username,
                          @Param("password") String security_credential);

    List<Customer> findCustomer(@Param("user") Customer customer,
                                @Param("create_time1") LocalDateTime create_time_start,
                                @Param("create_time2") LocalDateTime create_time_end,
                                @Param("update_time1") LocalDateTime update_time_start,
                                @Param("update_time2") LocalDateTime update_time_end);
}