package com.xl.common.config.autoconfig;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xl.common.dubbo.entity.TradeAccount;
import com.xl.common.dubbo.entity.TradeEntry;
import com.xl.common.dubbo.entity.TradeOrder;
import com.xl.common.utils.Generator;
import com.xl.common.utils.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 提供了一个便捷的自动填充功能，用于在插入或更新数据时自动填充某些字段，如创建时间、更新时间等。
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);

        // 处理签名和number(No)
        Object originalObject = metaObject.getOriginalObject();
        if (originalObject instanceof TradeAccount) {
            TradeAccount tradeAccount = (TradeAccount) originalObject;
            String signContent = tradeAccount.generateSignContent();
            String signature = SignatureUtil.generateSignature(signContent);
            this.strictInsertFill(metaObject, "signature", () -> signature, String.class);
            this.strictInsertFill(metaObject, "accountNo", Generator::generateNumberId, String.class);
        } else if (originalObject instanceof TradeEntry) {
            TradeEntry tradeEntry = (TradeEntry) originalObject;
            String signContent = tradeEntry.generateSignContent();
            String signature = SignatureUtil.generateSignature(signContent);
            this.strictInsertFill(metaObject, "signature", () -> signature, String.class);
            this.strictInsertFill(metaObject, "entryNo", Generator::generateNumberId, String.class);
        } else if (originalObject instanceof TradeOrder) {
            TradeOrder tradeOrder = (TradeOrder) originalObject;
            String signContent = tradeOrder.generateSignContent();
            String signature = SignatureUtil.generateSignature(signContent);
            this.strictInsertFill(metaObject, "signature", () -> signature, String.class);
            this.strictInsertFill(metaObject, "orderNo", Generator::generateNumberId, String.class);
        }

        /**
         * 下面这些可以参考写法
         */
//        // 获取原始对象
//        Object originalObject = metaObject.getOriginalObject();
//        Class<?> entityClass = originalObject.getClass();
//        // 获取实体类的表信息
//        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
//        if (tableInfo == null) {
//            log.warn("无法获取 {} 的表信息", entityClass.getName());
//            return;
//        }
//        // 获取所有带有INSERT填充策略的字段
//        List<TableFieldInfo> insertFields = tableInfo.getFieldList().stream()
//                .filter(field -> FieldFill.INSERT == field.getFieldFill() || FieldFill.INSERT_UPDATE == field.getFieldFill())
//                .toList();
//        log.info("找到 {} 个需要INSERT填充的字段", insertFields.size());
//        for (TableFieldInfo field : insertFields) {
//            String fieldName = field.getProperty();
//            log.debug("准备填充字段: {}", fieldName);
//            // 检查字段是否为null，只有为null才填充
//            if (getFieldValByName(fieldName, metaObject) == null) {
//                // 根据字段名进行不同的填充
//                if ("createTime".equals(fieldName) || "lastUpdateTime".equals(fieldName)) {
//                    strictInsertFill(metaObject, fieldName, LocalDateTime::now, LocalDateTime.class);
////                    this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());  //字段名称与类属性值一样，不是跟表字段一样
//                    log.debug("已填充时间字段: {}", fieldName);
//                } else if (originalObject instanceof TradeAccount && fieldName.contains("No")) {
//                    this.strictInsertFill(metaObject, fieldName, Generator::generateNumberId, String.class);
//                }else {  //可加其他条件
//                    this.strictInsertFill(metaObject, fieldName, Generator::generateNumberId, String.class);
//                }
//            }
//        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "lastUpdateTime", LocalDateTime.class, LocalDateTime.now());

        // 处理签名
        Object originalObject = metaObject.getOriginalObject();
        if (originalObject instanceof TradeAccount) {
            TradeAccount tradeAccount = (TradeAccount) originalObject;
            String signContent = tradeAccount.generateSignContent();
            String signature = SignatureUtil.generateSignature(signContent);
            metaObject.setValue("signature", signature);
        } else if (originalObject instanceof TradeEntry) {
            TradeEntry tradeEntry = (TradeEntry) originalObject;
            String signContent = tradeEntry.generateSignContent();
            String signature = SignatureUtil.generateSignature(signContent);
            metaObject.setValue("signature", signature);
        } else if (originalObject instanceof TradeOrder) {
            TradeOrder tradeOrder = (TradeOrder) originalObject;
            String signContent = tradeOrder.generateSignContent();
            String signature = SignatureUtil.generateSignature(signContent);
            metaObject.setValue("signature", signature);
        }
    }
}
