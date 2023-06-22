import {remove, list, create, update, unlock, lock} from '@/api/system/task'
import {STATUS_CODE} from "../../../api/statusCode";
import date from "../../../utils/date";

export default {
  data() {
    return {
      formVisible: false,
      formTitle: '添加任务',
      dataList: [],
      isAdd: true,
      form: {
        id: '',
        name: '',
        jobClass: '',
        cron: '',
        note: '',
        concurrent: 0,
        params: ''
      },
      rules: {
        name: [
          {required: true, message: '请输入任务名', trigger: 'blur'},
          {min: 2, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        jobClass: [
          {required: true, message: '请输入执行类', trigger: 'blur'}
        ],
        cron: [
          {required: true, message: '请输入定时规则', trigger: 'blur'}
        ]

      },
      listQuery: {
        name: undefined,
        total: 0,
        size: 8,
        page: 1
      },
      listLoading: true,
      selRow: {},
      status: {
        "0": "待启动",
        "1": "运行中",
        "-1": "已禁用",
      }
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.fetchData()
    },
    formatStatus(row) {
      return this.status[row.status.toString()]
    },
    formatExecTime(row) {
      if (row.execAt > 0) {
        return date.formatDate.format(new Date(row.execAt), 'yyyy-MM-dd hh:mm');
      }
      return '';
    },
    formatResult(row) {
      if (row.execAt > 0) {
        if (row.execResult === 1) {
          return "执行成功"
        } else {
          return "执行失败"
        }
      }
      return '';
    },
    fetchData() {
      this.listLoading = true
      list(this.listQuery).then(res => {
        this.listLoading = false
        this.listQuery.total = res.data.totalPages;
        this.dataList = res.data.content;
      })
    },
    search() {
      this.fetchData()
    },
    reset() {
      this.listQuery.name = ''
      this.fetchData()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleClose() {

    },
    handleCurrentChange(currentRow, oldCurrentRow) {
      this.selRow = currentRow
    },
    resetForm() {
      this.form = {}
    },
    add() {
      this.resetForm()
      this.form.concurrent = 0;
      this.formTitle = '添加任务'
      this.formVisible = true
      this.isAdd = true
    },
    save() {
      var self = this
      this.$refs['form'].validate((valid) => {
        if (valid) {
          let params = {
            id: self.form.id,
            name: self.form.name,
            jobClass: self.form.jobClass,
            concurrent: self.form.concurrent,
            cron: self.form.cron,
            params: self.form.params,
            note: self.form.note
          }
          console.log(params)
          if (params.id > 0) {
            update(params).then(response => {
              console.log(response)
              if (response.code === STATUS_CODE.SUCCESS) {
                this.$message({
                  message: '提交成功',
                  type: 'success'
                })
              } else {
                this.$message({
                  message: response.msg,
                  type: 'danger'
                })
              }
              this.fetchData()
              this.formVisible = false
            })
          } else {
            create(params).then(response => {
              console.log(response)
              if (response.code === STATUS_CODE.SUCCESS) {
                this.$message({
                  message: '提交成功',
                  type: 'success'
                })
              } else {
                this.$message({
                  message: response.msg,
                  type: 'danger'
                })
              }
              this.fetchData()
              this.formVisible = false
            })
          }

        } else {
          return false
        }
      })
    },
    checkSel() {
      if (this.selRow && this.selRow.id) {
        return true
      }
      this.$message({
        message: '请选中操作项',
        type: 'warning'
      })
      return false
    },
    enable(id) {
      this.$confirm('确定启用该定时任务?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        unlock(id).then(response => {
          this.$message({
            message: '操作成功',
            type: 'success'
          })
          this.fetchData()
        })
      }).catch(() => {
      })
    },
    disable(id) {
      this.$confirm('确定禁用该定时任务?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        lock(id).then(response => {
          this.$message({
            message: '操作成功',
            type: 'success'
          })
          this.fetchData()
        })
      }).catch(() => {
      })
    },
    viewLog(taskId) {
      this.$router.push({path: '/system/taskLog', query: {taskId: taskId}})
    },
    edit(row) {
      this.isAdd = false
      this.form = row
      this.formTitle = '修改任务'
      this.formVisible = true
    },
    remove(id) {
      this.$confirm('确定删除该记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        remove(id).then(response => {
          this.$message({
            message: '操作成功',
            type: 'success'
          })
          this.fetchData()
        })
      }).catch(() => {
      })
    }

  }
}
