"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      codeServices: [
        {
          title: "码上打印",
          desc: "一键链接，远程打印",
          icon: "/static/icons/print.png"
        },
        {
          title: "扫码免证",
          desc: "扫码免证办理",
          icon: "/static/icons/scan.png"
        },
        {
          title: "亮码免证",
          desc: "出码免证办理",
          icon: "/static/icons/show.png"
        }
      ],
      hallServices: [
        { name: "智能填表", icon: "/static/icons/form.png" },
        { name: "取件通知", icon: "/static/icons/notice.png" },
        { name: "大厅评价", icon: "/static/icons/rate.png" }
      ]
    };
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.f($data.codeServices, (item, index, i0) => {
      return {
        a: item.icon,
        b: common_vendor.t(item.title),
        c: common_vendor.t(item.desc),
        d: index
      };
    }),
    b: common_vendor.f($data.hallServices, (item, index, i0) => {
      return {
        a: item.icon,
        b: common_vendor.t(item.name),
        c: index
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/hall/hall.js.map
