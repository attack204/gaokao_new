import request from '@/utils/request'

/**
 * 分页查询信息列表
 */
export function getSpiderList(query) {
  return request({
    url: '/xhr/v1/getAllMissions',
    method: 'get',
    params: query
  })
}

/**
 * 调用爬虫接口
 */
export function startSpider() {
  return request({
    url: '/xhr/v1/startMission',
    method: 'get',
  })
}
