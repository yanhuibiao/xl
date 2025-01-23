<template>
  <view class="hall">
    <!-- 码上服务 -->
    <view class="code-service">
      <view class="section-title">码上服务</view>
      <view class="service-grid">
        <view class="service-item" v-for="(item, index) in codeServices" :key="index">
          <image :src="item.icon"></image>
          <view class="title">{{item.title}}</view>
          <view class="desc">{{item.desc}}</view>
        </view>
      </view>
    </view>
    
    <!-- 大厅服务 -->
    <view class="hall-service">
      <view class="section-title">大厅服务</view>
      <view class="service-list">
        <view class="service-item" v-for="(item, index) in hallServices" :key="index">
          <image :src="item.icon"></image>
          <text>{{item.name}}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      codeServices: [
        { 
          title: '码上打印',
          desc: '一键链接，远程打印',
          icon: '/static/icons/print.png'
        },
        {
          title: '扫码免证',
          desc: '扫码免证办理',
          icon: '/static/icons/scan.png'
        },
        {
          title: '亮码免证',
          desc: '出码免证办理',
          icon: '/static/icons/show.png'
        }
      ],
      hallServices: [
        { name: '智能填表', icon: '/static/icons/form.png' },
        { name: '取件通知', icon: '/static/icons/notice.png' },
        { name: '大厅评价', icon: '/static/icons/rate.png' }
      ]
    }
  }
}
</script>