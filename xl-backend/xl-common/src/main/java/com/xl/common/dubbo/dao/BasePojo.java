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
    //    @TableId(type = IdType.ASSIGN_UUID)  // 使用mybatis plus生成id（雪花算法）
    @TableId(type = IdType.ASSIGN_ID)       // 使用mybatis-plus才生效
    public String id;
    @TableField(fill = FieldFill.INSERT)    // 插入时自动更新，使用mybatis-plus才生效
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)    // 更新时自动更新，使用mybatis-plus才生效
    private LocalDateTime lastUpdateTime;
}
