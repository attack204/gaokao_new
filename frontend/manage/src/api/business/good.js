import request from '@/utils/request'

/**
 * 分页查询商品信息列表
 */
export function getGood(query) {
  return request({
    url: '/xhr/v1/goods/',
    method: 'get',
    params: query
  })
}

/**
 * 新建商品信息
 */
export function addGood(params) {
  return request({
    url: '/xhr/v1/goods/create',
    method: 'post',
    data: params
  })
}

/**
 * 修改商品信息
 */
export function updateGood(goodId, params) {
  return request({
    url: '/xhr/v1/goods/' + goodId,
    method: 'post',
    data: params
  })
}

/**
 * 爬取高校信息
 */
export function fetchRemoteData() {
  return request({
      url: '/xhr/vl/university/fetch',
      method: 'get',
      //params: {}
  })
}

/**
 * 更新数据库
 */
export function updateDatabase() {
  return request({
      url: '/xhr/vl/university/update',
      method: 'get',
  })
}

/**
 * 下架商品
 */
export function offGood(goodId) {
  return request({
    url: '/xhr/v1/goods/offSell/' + goodId,
    method: 'post'
  })
}

/**
 * 上架商品
 */
export function onGood(goodIds) {
  return request({
    url: '/xhr/v1/goods/onSell/' + goodIds,
    method: 'post'
  })
}

/**
 * 封禁商品
 */
export function closeGood(goodIds) {
  return request({
    url: '/xhr/v1/goods/close/' + goodIds,
    method: 'post'
  })
}

/**
 * 解封商品
 */
export function unCloseGood(goodIds) {
  return request({
    url: '/xhr/v1/goods/unClose/' + goodIds,
    method: 'post'
  })
}

/**
 * 商品暂停服务
 */
export function outOfGood(goodId) {
  return request({
    url: '/xhr/v1/goods/outOfService/' + goodId,
    method: 'post'
  })
}

/**
 * 审核商品通过
 */
export function auditPassGood(goodId, params) {
  return request({
    url: '/xhr/v1/goods/auditPass/' + goodId,
    method: 'post',
    data: params
  })
}

/**
 * 审核商品不通过、驳回
 */
export function auditUnPassGood(goodId, auditOpinion) {
  return request({
    url: '/xhr/v1/goods/auditUnPass/' + goodId,
    method: 'post',
    params: {auditOpinion: auditOpinion}
  })
}

/**
 * 查询分类信息列表,下拉框使用
 */
export function goodGetCategory() {
  return request({
    url: '/xhr/v1/categories/all',
    method: 'get'
  })
}

/**
 * 查询分组信息列表,下拉框使用
 */
export function goodGetTag() {
  return request({
    url: '/xhr/v1/tags/all',
    method: 'get'
  })
}

/**
 * 查询商品-规格属性关系
 */
export function GoodsWithSpecAttr(query) {
  return request({
    url: '/xhr/v1/goodsSpecAttr/listByParam',
    method: 'get',
    params: query
  })
}

/**
 * 新增商品-规格属性关系
 */
export function createRelation(params) {
  return request({
    url: '/xhr/v1/goodsSpecAttr/create',
    method: 'post',
    data: params
  })
}

/**
 * 修改商品-规格属性关系
 */
export function updateRelation(goodId,params) {
  return request({
    url: '/xhr/v1/goodsSpecAttr/update' + goodId,
    method: 'post',
    data: params
  })
}

/**
 * 删除商品-规格属性关系
 */
export function removeRelation(goodId) {
  return request({
    url: '/xhr/v1/goodsSpecAttr/remove' + goodId,
    method: 'post'
  })
}
