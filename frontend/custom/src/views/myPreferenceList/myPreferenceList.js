import { getVolunteer, deleteVolunteerForm, changeCurrentForm, updateVolunteerName } from '../../api/volunteer'
import { getInfo } from '../../api/login'
export default {
  data() {
    return {
      tableData: [{
        id: 0,
        name: '',
        type: '',
        createtime: '',
        iscurrent: false,
        editName: false
      }],
      userId: 0,
      activeIndex: 0,
      checkList: [],
      // editName: false,
      active: {}
    }
  },
  methods: {
    initData() {
      getInfo().then(res => {
        this.userId = res.data.id
        const data = []
        getVolunteer().then(volunteer => {
          volunteer.data.forEach((item, index) => {
            data[index] = {
              id: item.id,
              name: item.name,
              type: item.generatedType ? '自动生成' : '手动生成',
              createtime: new Date(item.generatedTime).toLocaleDateString(),
              iscurrent: item.current ? '是' : '否',
              editName: false
            }
            if (data[index].iscurrent === '是') {
              this.activeIndex = data[index].id
            }
          })
          this.tableData = data
        })
      })
    },
    // 这几个方法绝对写麻烦了QAQ
    setCurrentPreference(preferData) {
      const params = {
        newFormId: preferData.id,
        preFormId: this.activeIndex
      }
      this.active = this.tableData.find(function(value) {
        return value.id === params.preFormId
      })
      changeCurrentForm(params).then((res) => {
        if (res.code === 200) {
          this.$message({
            message: '修改当前志愿表成功',
            type: 'success',
            duration: 700
          })
        }
        // console.log(this.active)
        const preIndex = this.tableData.indexOf(this.active)
        const newIndex = this.tableData.indexOf(preferData)
        // console.log('pre:' + preIndex)
        // console.log('new:' + newIndex)
        this.tableData[preIndex].iscurrent = '否'
        this.activeIndex = preferData.id
        this.tableData[newIndex].iscurrent = '是'
      })
    },
    deletePreference(preferData) {
    //   console.log(preferData)
      // 判断要删除的是否是当前使用的志愿
      if (preferData.iscurrent === '是') {
        this.$message({
          message: '不能删除当前使用志愿！如要删除请修改当前志愿表！',
          type: 'warning',
          duration: 2500
        })
        return
      }
      // confirm事件在删除前确认一下
      this.$confirm('此操作将永久删除该志愿表, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteVolunteerForm(preferData.id).then((res) => {
          if (res.code === 200) {
            this.$message({
              message: '删除志愿表成功！',
              type: 'success',
              duration: 700
            })
            const index = this.tableData.indexOf(preferData)
            if (preferData.iscurrent === '是') {
              this.activeIndex = 0
            }
            // 200if
            this.tableData.splice(index, 1)
            if (this.activeIndex === 0 && this.tableData.length) {
              this.tableData[0].iscurrent = '是'
            }
          } else {
            this.$message({
              message: '删除志愿表失败！',
              type: 'error',
              duration: 700
            })
          }
        })
      }
      )
    },
    handleSelectionChange(val) {
      this.checkList = val
    },
    editVolunteerName(preferData) {
      preferData.editName = !preferData.editName
      if (!preferData.editName) {
        // 保存志愿表名称
        if (preferData.name === '') {
          this.$message({
            message: '志愿名不能为空',
            type: 'error'
          })
          window.location.reload()
          return
        }
        // 提交修改志愿名
        const param = {
          formId: preferData.id,
          name: preferData.name
        }
        updateVolunteerName(param).then(res => {
          if (res.code === 200) {
            this.$message({
              message: '修改志愿名成功！',
              type: 'success',
              duration: 700
            })
          } else {
            this.$message({
              message: '修改志愿名失败',
              type: 'error',
              duration: 700
            })
          }
        })
      }
    }
  },
  mounted() {
    this.initData()
  }
}
