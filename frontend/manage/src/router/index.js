import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

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
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
});

export const asyncRouterMap = [
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    name: 'dashboard',
    meta: {
      title: '主页',
      icon: 'dashboard',
      permCode: 'dashboard'
    },
    hidden: false,
    children: [
      {
        path: 'home',
        name: 'home',
        component: () => import('@/views/dashboard/index'),
        meta: {title: 'dashboard', permCode: 'home'}
      },
      {
        path: '/account/updatePwd',
        name: '修改密码',
        component: () => import('@/views/account/updatePwd.vue'),
        hidden: true,
        meta: {title: '修改密码'}
      }
    ]
  },
  {
    path: '/data',
    component: Layout,
    redirect: '#',
    name: 'data',
    alwaysShow: true,
    meta: {
      permCode: 'data',
      title: 'operation',
      icon: 'edit'
    },
    children: [
      {
        path: 'spider',
        name: 'spider',
        component: () => import('@/views/business/spider/index'),
        meta: {title: '爬虫管理', permCode: 'spider'}
      },
      // {
      //   path: 'service',
      //   name: 'service',
      //   component: () => import('@/views/business/good/index'),
      //   meta: {title: '大学管理', permCode: 'goods'}
      // },
      {
        path: 'order',
        name: 'order',
        component: () => import('@/views/business/order/index'),
        meta: {title: '订单管理', permCode: 'order'},
      },
      {
        path: 'appuser',
        name: 'appuser',
        component: () => import('@/views/business/member/index'),
        meta: {title: '会员管理', permCode: 'usermember',}
      },
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '#',
    name: 'System',
    alwaysShow: true,
    permCode: 10,
    meta: {
      permCode: 'system',
      title: 'systemMgr',
      icon: 'table'
    },
    children: [
      {
        path: 'mgr',
        name: 'Account',
        component: () => import('@/views/system/user/index'),
        meta: {
          permCode: 'user',
          title: 'userMgr'
        }
      },
      {
        path: 'role',
        name: 'roleMgr',
        component: () => import('@/views/system/role/index'),
        meta: {
          permCode: 'role',
          title: 'roleMgr'
        }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index'),
        meta: {
          permCode: 'perm',
          title: 'menuMgr'
        }
      },
      // {
      //   path: 'task',
      //   name: 'Task',
      //   component: () => import('@/views/system/task/index'),
      //   meta: {permCode: 'task', title: 'taskMgr'}
      // }
      // {
      //   path: 'taskLog',
      //   name: 'taskLog',
      //   component: () => import('@/views/system/task/taskLog.vue'),
      //   hidden: true,
      //   meta: {title: 'taskLog'}
      //
      // }
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  }
]
