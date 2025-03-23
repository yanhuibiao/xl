package com.xl.transaction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xl.common.dubbo.api.TradeOrderService;
import com.xl.common.dubbo.entity.TradeOrder;
import com.xl.transaction.mapper.TradeOrderMapper;

public class TradeOrderServiceImpl extends ServiceImpl<TradeOrderMapper, TradeOrder> implements TradeOrderService {
}
