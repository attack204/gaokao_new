
const getters = {
  sidebar: state => state.app.sidebar,
  language: state => state.app.language,
  device: state => state.app.device,
  perm_routes: state => state.permission.perm_routes,
  addRouters: state => state.permission.addRouters,
  permCodes: state => state.permission.permCodes,
  dialogVisible: state => state.app.dialogVisible
}
export default getters
