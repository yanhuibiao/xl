package com.xl.identitybusiness.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xl.common.dubbo.api.AdministratorService;
import com.xl.common.dubbo.entity.Administrator;
import com.xl.common.enums.IdentityStatus;
import com.xl.common.enums.ResponseCodeEnum;
import com.xl.common.exception.BusinessException;
import com.xl.common.utils.JwtUtils;
import com.xl.identitybusiness.mapper.AdministratorMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// 实现UserDetailsService是为了实现spring security的一个导入用户方法
@DubboService
@Service
public class AdministratorServiceImpl extends ServiceImpl<AdministratorMapper, Administrator> implements AdministratorService, UserDetailsService {
    @Autowired
    AdministratorMapper administratorMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Administrator> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Administrator administrator = administratorMapper.selectOne(queryWrapper);
        if (administrator == null) {throw new UsernameNotFoundException("User not found");}
        return new User(administrator.getUsername(), administrator.getPassword(), AuthorityUtils.createAuthorityList(administrator.getRoleId()));
//        return administrator;
    }


    public String login(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BusinessException(ResponseCodeEnum.LOGIN_FAILED);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return jwtUtils.getToken(userDetails.getUsername());
    }

    public String register(Administrator administrator) {
        administrator.setPassword(passwordEncoder.encode(administrator.getPassword()));
        administrator.setStatus(IdentityStatus.PendingActive);
        try {
            administratorMapper.insert(administrator);
        }catch (DuplicateKeyException e){
            throw new BusinessException(ResponseCodeEnum.DATA_INTEGRITY_VIOLATION);
        }
        return administrator.getId();   //id回填
    }


    public List<Administrator> findAdministrators(String username, String status, String roleId, String phone,
                                                  LocalDateTime createTimeStart, LocalDateTime createTimeEnd,
                                                  LocalDateTime passwordExpireTimeStart, LocalDateTime passwordExpireTimeEnd) {
        // 在传递给Mapper之前，处理模糊查询参数
        if (username != null) {
            username = "%" + username + "%";
        }
        if (phone != null) {
            phone = "%" + phone + "%";
        }
        return administratorMapper.findAdministrators(username, status, roleId, phone, createTimeStart, createTimeEnd, passwordExpireTimeStart, passwordExpireTimeEnd);
    }

}
