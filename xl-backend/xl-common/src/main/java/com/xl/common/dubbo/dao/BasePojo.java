package com.xl.common.dubbo.dao;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BasePojo implements Serializable {

    @TableField(fill = FieldFill.INSERT)    // 插入时自动更新，使用mybatis-plus才生效
    public LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)    // 更新时自动更新，使用mybatis-plus才生效
    public LocalDateTime lastUpdateTime;
}
