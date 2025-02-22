<template>
  <view class="mine">
    <!-- 用户信息 -->
    <view class="userMapper-info">
      <text class="level">LV4</text>
      <text class="welcome">晚上好! 欢迎访问赣服通~</text>
    </view>
    
    <!-- 功能列表 -->
    <view class="function-list">
      <view class="function-item" v-for="(item, index) in functions" :key="index">
        <image :src="item.icon"></image>
        <view class="content">
          <view class="title">{{item.title}}</view>
          <view class="desc">{{item.desc}}</view>
        </view>
        <text class="arrow">></text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      functions: [
        {
          title: '数字档案管理',
          desc: '管理已授权过的数字服务',
          icon: '/static/icons/archive.png'
        },
        {
          title: '我的办件',
          desc: '办理进度随时了解',
          icon: '/static/icons/process.png'
        },
        {
          title: '我的授权',
          desc: '权限在握 信任为基',
          icon: '/static/icons/auth.png'
        },
        {
          title: '我的预约',
          desc: '一键预约 办件贴心',
          icon: '/static/icons/appointment.png'
        },
        {
          title: '我的证书',
          desc: '证件收纳 随时可用',
          icon: '/static/icons/certificate.png'
        }
      ]
    }
  }
}
</script>