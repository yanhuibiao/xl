package com.xl.common.dubbo.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xl.common.enums.IdentityStatus;
import lombok.Data;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@TableName("administrator")
public class Administrator extends BasePojo {
    //    @TableId(type = IdType.ASSIGN_UUID)  // 使用mybatis plus生成id（雪花算法）
    @TableId(type = IdType.ASSIGN_ID)       // 使用mybatis-plus才生效
    public String id;
    String username;
//    @JsonIgnore  //该字段不会出现在JSON响应中，适用于前端也不会传
//    @TableField(exist = false)	// 数据库中无此字段，或不想MyBatis-Plus处理,该字段不会被 MyBatis-Plus查询或更新
    @JsonProperty(access = WRITE_ONLY)  // 前端可传该字段，但不会出现在返回结果
    String password;
    IdentityStatus status;
    String phone;
    String accountNo;
    String roleId;
    LocalDateTime passwordExpireTime;


    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Administrator other = (Administrator) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
                && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getAccountNo() == null ? other.getAccountNo() == null : this.getAccountNo().equals(other.getAccountNo()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getPasswordExpireTime() == null ? other.getPasswordExpireTime() == null : this.getPasswordExpireTime().equals(other.getPasswordExpireTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getAccountNo() == null) ? 0 : getAccountNo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getPasswordExpireTime() == null) ? 0 : getPasswordExpireTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", phone=").append(phone);
        sb.append(", status=").append(status);
        sb.append(", account_id=").append(accountNo);
        sb.append(", create_time=").append(getCreateTime());
        sb.append(", password_expire_time=").append(getPasswordExpireTime());
        sb.append("]");
        return sb.toString();
    }
}