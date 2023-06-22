import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'
import AccountLayout from '../views/account/accountLayout'

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  {
    path: '/',
    component: Layout,
    redirect: 'recommand',
    name: 'dashboard',
    meta: {
      title: '主页',
      icon: 'dashboard',
      permCode: 'dashboard'
    },
    children: [
      {
        path: 'recommand',
        name: '智能推荐',
        component: () => import('@/views/recommand/index'),
        meta: {title: 'recommand', permCode: 'recommand'}
      },
      {
        path: 'screen',
        name: '手动填报',
        component: () => import('@/views/screen/index'),
        meta: {title: 'screen', permCode: 'screen'}
      },
      {
        path: 'preference',
        name: '志愿表',
        component: () => import('@/views/preference/index'),
        meta: {title: 'preference', permCode: 'preference'}
      },
      {
        path: 'stars',
        name: '收藏',
        component: () => import('@/views/stars/index'),
        meta: {title: 'stars', permCode: 'stars'}
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index')
  },
  {
    path: '/register',
    component: () => import('@/views/register/index')
  },
  {
    path: '/refresh',
    component: () => import('@/components/refresh/index')
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
});

export const asyncRouterMap = [
  {
    path: '/account',
    component: AccountLayout,
    redirect: 'profile',
    name: 'Account',
    alwaysShow: true,
    permCode: 10,
    meta: {
      permCode: 'account',
      title: '账户信息',
      icon: 'account'
    },
    children: [
      {
        path: 'profile',
        name: '个人资料',
        component: () => import('@/views/profile/index'),
        meta: {title: '个人资料', permCode: 'profile'}
      },
      {
        path: 'myPreferenceList',
        name: '我的志愿表',
        component: () => import('@/views/myPreferenceList/index'),
        meta: {title: '我的志愿表', permCode: 'myPreferenceList'}
      },
      {
        path: 'myOrder',
        name: '我的订单',
        component: () => import('@/views/myOrder/index'),
        meta: {title: '我的订单', permCode: 'myOrder'}
      },
      {
        path: 'updatePwd',
        name: '修改密码',
        component: () => import('@/views/updatePwd/index'),
        meta: {title: '修改密码', permCode: 'updatePwd'}
      }
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
  }
]
