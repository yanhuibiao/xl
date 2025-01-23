"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      quickEntries: [
        { name: "赣通码", icon: "/static/icons/code.png" },
        { name: "亮证照", icon: "/static/icons/id.png" },
        { name: "数字档案", icon: "/static/icons/file.png" },
        { name: "智能小赣事", icon: "/static/icons/ai.png" }
      ],
      serviceList: [
        { name: "社保查询", icon: "/static/icons/insurance.png" },
        { name: "失业保险", icon: "/static/icons/unemployment.png" },
        { name: "医保账户", icon: "/static/icons/medical.png" },
        { name: "高龄老人", icon: "/static/icons/elderly.png" },
        { name: "养老待遇", icon: "/static/icons/pension.png" },
        { name: "养老认证", icon: "/static/icons/auth.png" },
        { name: "办事预约", icon: "/static/icons/appointment.png" },
        { name: "驾驶证记分", icon: "/static/icons/license.png" }
      ]
    };
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.f($data.quickEntries, (item, index, i0) => {
      return {
        a: item.icon,
        b: common_vendor.t(item.name),
        c: index
      };
    }),
    b: common_vendor.f($data.serviceList, (item, index, i0) => {
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
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map
