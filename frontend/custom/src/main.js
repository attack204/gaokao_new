import Vue from 'vue'
import Cookies from 'js-cookie'
import ECharts from 'vue-echarts/components/ECharts.vue'
import ElementUI from 'element-ui'
import vip from '@/components/Vip/index'

import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss'
import 'normalize.css/normalize.css'

import VueParticles from 'vue-particles'
import App from './App'
import router from './router'
import store from './store'
import i18n from './lang'

import '@/icons'
import './permission'

Vue.use(VueParticles)
Vue.use(ElementUI, {
  size: Cookies.get('size') || 'medium',
  i18n: (key, value) => i18n.t(key, value)
})
Vue.component('v-chart', ECharts)
Vue.component('vip', vip)
Vue.directive('perm', {
  inserted(el, binding, vnode) {
    const {value} = binding
    const permissions = store.getters && store.getters.permCodes
    if (value) {
      const hasPermission = permissions.indexOf(value) !== -1
      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  }
})
Vue.config.productionTip = false

initPrototypeFun()

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})

function initPrototypeFun() {
  // eslint-disable-next-line no-extend-native
  Date.prototype.Format = function (fmt) {
    const o = {
      'M+': this.getMonth() + 1, // 月份
      'd+': this.getDate(), // 日
      'h+': this.getHours(), // 小时
      'm+': this.getMinutes(), // 分
      's+': this.getSeconds(), // 秒
      'q+': Math.floor((this.getMonth() + 3) / 3), // 季度
      'S': this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length))
    }
    let k
    for (k in o) {
      if (new RegExp('(' + k + ')').test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
      }
    }
    return fmt
  }
}

