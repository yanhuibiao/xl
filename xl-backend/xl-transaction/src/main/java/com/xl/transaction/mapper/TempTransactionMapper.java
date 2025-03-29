package com.xl.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xl.common.dubbo.entity.TempTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 分布式事务表 Mapper 接口
 */
@Mapper
public interface TempTransactionMapper extends BaseMapper<TempTransaction> {

    /**
     * 根据XID查询事务
     *
     * @param xid 全局事务ID
     * @return 事务对象
     */
    @Select("SELECT * FROM temp_transaction WHERE xid = #{xid}")
    TempTransaction selectByXid(@Param("xid") String xid);
    
    /**
     * 查询指定状态的事务列表
     *
     * @param status 事务状态
     * @return 事务列表
     */
    @Select("SELECT * FROM temp_transaction WHERE status = #{status}")
    List<TempTransaction> selectByStatus(@Param("status") Long status);
    
    /**
     * 更新事务状态
     *
     * @param xid 全局事务ID
     * @param status 新状态
     * @return 影响行数
     */
    @Update("UPDATE temp_transaction SET status = #{status} WHERE xid = #{xid}")
    int updateStatus(@Param("xid") String xid, @Param("status") Long status);
    
    /**
     * 增加重试次数
     *
     * @param xid 全局事务ID
     * @return 影响行数
     */
    @Update("UPDATE temp_transaction SET retry_count = retry_count + 1 WHERE xid = #{xid}")
    int increaseRetryCount(@Param("xid") String xid);
    
    /**
     * 查询需要重试的事务列表（状态异常且重试次数小于最大值）
     *
     * @param status 异常状态
     * @param maxRetry 最大重试次数
     * @return 需要重试的事务列表
     */
    @Select("SELECT * FROM temp_transaction WHERE status = #{status} AND retry_count < #{maxRetry}")
    List<TempTransaction> selectForRetry(@Param("status") Long status, @Param("maxRetry") Long maxRetry);
} 