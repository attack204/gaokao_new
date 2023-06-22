import {changeStarStatus, getCurrentInfo, getStars, submitVolunteer} from "../../api/screen";

export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      //收藏列表
      starsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      dialogFormVisible: false,
      //禁用参数
      edit: true,
      responseList:
        {
          rate: undefined,
          volunteerVO: {}
        },
      query: {
        page: 1,
        size: 10,
        total: 0,
      },
      //表单参数
      form: {
        formId: undefined,
        volunteerId: undefined,
        volunteerPosition: undefined,
        name: undefined,
        professionalName: undefined,
      },
      // 表单校验
      rules: {
        volunteerPosition: {required: true, message: "志愿序号不能为空", trigger: "blur"}
      }
    }
  },

  created() {
    this.getCurrent();
    this.fetchData();
  },

  methods: {
    //请求列表数据
    fetchData() {
      this.loading = true;
      getStars(this.query).then(response => {
        this.responseList = [];
        this.responseList = response.data.content;
        this.starsList = [];
        this.responseList.forEach(item => {
          this.starsList.push({
            "rate": item.rate,
            "volunteerId": item.volunteerVO.id,
            "name": item.volunteerVO.name,
            "province": item.volunteerVO.province,
            "lowestScore": item.volunteerVO.lowestScore,
            "lowestPosition": item.volunteerVO.lowestPosition,
            "professionalName": item.volunteerVO.professionalName,
            "category": item.volunteerVO.category,
            "enrollment": item.volunteerVO.enrollment,
            "time": item.volunteerVO.time,
            "fee": item.volunteerVO.fee,
            "universityCode": item.volunteerVO.universityCode,
            "privateIsOrNot": item.volunteerVO.privateIsOrNot,
            "publicIsOrNot": item.volunteerVO.publicIsOrNot,
            "majorCode": item.volunteerVO.majorCode,
            "subjectRestrictionType": item.volunteerVO.subjectRestrictionType,
            "myStar": item.volunteerVO.myStar
          })
        })
        this.loading = false;
        this.total = response.data.totalElements;
        this.query.total = response.data.totalPages;
      })
    },
    getCurrent() {
      getCurrentInfo().then(response => {
        if (response) {
          this.form.formId = response.data.id;
          console.log("this.form.formId")
          console.log(this.form.formId)
        }
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.query.page = 1;
      this.fetchData();
    },
    reset() {
      this.form = {
        universityName: undefined,
        majorName: undefined,
        volunteerPosition: undefined
      };
      this.formReset("form");
    },
    handleFill(row) {
      this.title = "填报志愿";
      this.edit = true;
      this.dialogFormVisible = true;
      this.form.name = row.name;
      this.form.professionalName = row.professionalName;
      this.form.volunteerId = row.volunteerId;
    },
    handleStar(row) {
      let text = undefined;
      if (row.myStar === false) {
        text = "收藏";
      } else {
        text = "取消收藏";
      }
      let id = row.volunteerId;
      this.$confirm('确认' + text + '吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return changeStarStatus(id);
      }).then(() => {
        this.$message({
          type: 'success',
          message: '更新信息成功'
        })
        this.fetchData();
      });
    },
    cancel() {
      this.dialogFormVisible = false;
      this.reset();
    },
    fetchPage(page) {
      this.query.page = page
      this.fetchData(page)
    },
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          submitVolunteer(this.form).then(response => {
            if (response.code === 200) {
              this.$message({
                type: 'success',
                message: '已保存为第' + this.form.volunteerPosition + '志愿！'
              })
            }
            if (response) {
              this.$refs.form.resetFields()
            }
            this.dialogFormVisible = false;
            this.fetchData();
          });
        }
      });
    }
  }
}
