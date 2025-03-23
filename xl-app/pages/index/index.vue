<template>
  <view class="index">
    <!-- 搜索框 -->
    <view class="search-box">
      <input type="text" placeholder="驾驶证记分" />
      <button class="search-btn">搜索</button>
    </view>
    
    <!-- 快捷功能区 -->
    <view class="quick-tradeEntry">
      <view class="tradeEntry-item" v-for="(item, index) in quickEntries" :key="index">
        <image :src="item.icon"></image>
        <text>{{item.name}}</text>
      </view>
    </view>
    
    <!-- 服务列表 -->
    <view class="service-list">
      <view class="service-item" v-for="(item, index) in serviceList" :key="index">
        <image :src="item.icon"></image>
        <text>{{item.name}}</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      quickEntries: [
        { name: '赣通码', icon: '/static/icons/code.png' },
        { name: '亮证照', icon: '/static/icons/id.png' },
        { name: '数字档案', icon: '/static/icons/file.png' },
        { name: '智能小赣事', icon: '/static/icons/ai.png' }
      ],
      serviceList: [
        { name: '社保查询', icon: '/static/icons/insurance.png' },
        { name: '失业保险', icon: '/static/icons/unemployment.png' },
        { name: '医保账户', icon: '/static/icons/medical.png' },
        { name: '高龄老人', icon: '/static/icons/elderly.png' },
        { name: '养老待遇', icon: '/static/icons/pension.png' },
        { name: '养老认证', icon: '/static/icons/auth.png' },
        { name: '办事预约', icon: '/static/icons/appointment.png' },
        { name: '驾驶证记分', icon: '/static/icons/license.png' }
      ]
    }
  }
}
</script>

<style lang="scss">
.index {
  padding: 20rpx;
  
  .search-box {
    display: flex;
    background: #f5f5f5;
    border-radius: 30rpx;
    padding: 10rpx 20rpx;
    
    input {
      flex: 1;
    }
  }
  
  .quick-tradeEntry {
    display: flex;
    justify-content: space-between;
    margin-top: 30rpx;
    
    .tradeEntry-item {
      text-align: center;
      
      image {
        width: 80rpx;
        height: 80rpx;
      }
    }
  }
  
  .service-list {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20rpx;
    margin-top: 30rpx;
    
    .service-item {
      text-align: center;
      
      image {
        width: 60rpx;
        height: 60rpx;
      }
    }
  }
}
</style>