"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      functions: [
        {
          title: "数字档案管理",
          desc: "管理已授权过的数字服务",
          icon: "/static/icons/archive.png"
        },
        {
          title: "我的办件",
          desc: "办理进度随时了解",
          icon: "/static/icons/process.png"
        },
        {
          title: "我的授权",
          desc: "权限在握 信任为基",
          icon: "/static/icons/auth.png"
        },
        {
          title: "我的预约",
          desc: "一键预约 办件贴心",
          icon: "/static/icons/appointment.png"
        },
        {
          title: "我的证书",
          desc: "证件收纳 随时可用",
          icon: "/static/icons/certificate.png"
        }
      ]
    };
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.f($data.functions, (item, index, i0) => {
      return {
        a: item.icon,
        b: common_vendor.t(item.title),
        c: common_vendor.t(item.desc),
        d: index
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/my/my.js.map
