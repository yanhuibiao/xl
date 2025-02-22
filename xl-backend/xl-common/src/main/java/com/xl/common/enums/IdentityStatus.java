package com.xl.common.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;

public enum IdentityStatus {
    PendingActive("02","PendingActive"),
    Active("03","Active"),
    Suspended("04","Suspended"),
    Frozen("04","Frozen"),
    Dormant("08","Dormant"),
    Close("06","Close");
    //标记数据库存的值是枚举值
    @EnumValue
    public final String identityCode;

    public final String identityName;

    IdentityStatus(String identityCode, String identityName) {
        this.identityCode = identityCode;
        this.identityName = identityName;
    }
}
