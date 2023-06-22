import { asyncRouterMap, constantRouterMap } from '@/router'

// permission judge function
function hasPermission(routeItem, permCodes) {
  // if (!routeItem.meta || !routeItem.meta.permCode) {
  //   return true;
  // }
  // return permCodes.indexOf(routeItem.meta.permCode) > -1;
  //用户登录即可获取所有页面访问权限，这里直接返回true了
  return true;
}

function filterAsyncRouterByMenu(itemMap, permCodes) {
  return itemMap.filter(route => {
    const hasPerm = hasPermission(route, permCodes);
    if (hasPerm) {
      if (route.children && route.children.length) {
        route.children = filterAsyncRouterByMenu(route.children, permCodes)
      }
      return true
    }
    return false
  })
}

const permission = {
  state: {
    perm_routes: constantRouterMap,
    addRouters: [],
    permCodes: [],
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.perm_routes = constantRouterMap.concat(routers)
      console.log(state.perm_routes)
    },
    SET_PERM: (state, permCodes) => {
      state.permCodes = permCodes;
    }
  },
  actions: {
    GenerateRoutes({commit}, data) {
      console.log(data)
      return new Promise(resolve => {
        commit('SET_PERM', data.permissions)
        const accessedRouters = filterAsyncRouterByMenu(asyncRouterMap, data.permissions)
        commit('SET_ROUTERS', accessedRouters)
        resolve()
      })
    }
  }
}

export default permission
