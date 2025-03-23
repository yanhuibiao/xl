package com.xl.transaction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xl.common.dubbo.api.TradeEntryService;
import com.xl.common.dubbo.entity.TradeEntry;
import com.xl.transaction.mapper.TradeEntryMapper;

public class TradeEntryServiceImpl extends ServiceImpl<TradeEntryMapper, TradeEntry> implements TradeEntryService {
}
