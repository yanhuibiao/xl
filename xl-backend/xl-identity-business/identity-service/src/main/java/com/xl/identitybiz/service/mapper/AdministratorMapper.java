package com.xl.identitybiz.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xl.common.dubbo.entity.Administrator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface AdministratorMapper extends BaseMapper<Administrator> {

    @Select("<script>" +
            "SELECT * FROM administrator WHERE 1=1" +
            "<if test='username != null'> AND username LIKE #{username}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            "<if test='roleId != null'> AND role_id = #{roleId}</if>" +
            "<if test='phone != null'> AND phone LIKE #{phone}</if>" +
            "<if test='createTimeStart != null'> AND create_time &gt;= #{createTimeStart}</if>" +
            "<if test='createTimeEnd != null'> AND create_time &lt;= #{createTimeEnd}</if>" +
            "<if test='passwordExpireTimeStart != null'> AND password_expire_time &gt;= #{passwordExpireTimeStart}</if>" +
            "<if test='passwordExpireTimeEnd != null'> AND password_expire_time &lt;= #{passwordExpireTimeEnd}</if>" +
            "</script>")
    List<Administrator> findAdministrators(@Param("username") String username,
                                           @Param("status") String status,
                                           @Param("roleId") String roleId,
                                           @Param("phone") String phone,
                                           @Param("createTimeStart") LocalDateTime createTimeStart,
                                           @Param("createTimeEnd") LocalDateTime createTimeEnd,
                                           @Param("passwordExpireTimeStart") LocalDateTime passwordExpireTimeStart,
                                           @Param("passwordExpireTimeEnd") LocalDateTime passwordExpireTimeEnd);

}