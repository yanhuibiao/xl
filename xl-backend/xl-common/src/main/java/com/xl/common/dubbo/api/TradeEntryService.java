package com.xl.common.dubbo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xl.common.dubbo.entity.TradeEntry;
import com.xl.common.dubbo.entity.TradeOrder;

import java.util.List;


public interface TradeEntryService extends IService<TradeEntry> {
   void createTradeEntry(TradeOrder tradeOrder);

   List<TradeEntry> getTradeEntry(String orderNo);

   void deleteTradeEntry(String orderNo);
}
