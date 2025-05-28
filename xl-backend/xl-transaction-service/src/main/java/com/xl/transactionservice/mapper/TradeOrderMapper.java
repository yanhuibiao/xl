package com.xl.transactionservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xl.common.dubbo.entity.TradeOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradeOrderMapper extends BaseMapper<TradeOrder> {
}
