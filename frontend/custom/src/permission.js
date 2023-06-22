import router from './router'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css' // Progress 进度条样式
import {getToken} from '@/utils/auth'
import {getPermissions} from "./utils/auth"; // 验权
import store from './store'
const whiteList = ['/login', '/recommand', '/register', '/refresh'] // 不重定向白名单


router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    if (to.path === '/login' || to.path === '/register') {
      next({path: '/'})
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else if (store.getters.addRouters.length === 0) {
      store.dispatch('GenerateRoutes', {permissions: getPermissions()}).then(() => {
        // 添加可访问路由表
        router.addRoutes(store.getters.addRouters)
        next({...to, replace: true}) // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
      })
    } else {
      next()
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next('/login')
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
