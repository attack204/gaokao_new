import {
  changeStarStatus,
  getBatch, getCurrentInfo, getMajorType, getRegion, getSchoolType, getVOList, submitVolunteer
} from "../../api/screen";

export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      //志愿列表
      volunteerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      dialogFormVisible: false,
      //禁用参数
      edit: true,
      //多选参数
      multiProps: {multiple: true},
      //请求参数
      listQuery: {
        page: 1,
        limit: 10,
        total: 0,
        batch: [],
        region: [],
        schoolType: [],
        schoolTeSe: [],
        schoolXingZhi: [],
        universityName: undefined,
        majorName: undefined,
        type: 0,
      },
      level: 8,
      location: undefined,
      classification: undefined,
      majorType: undefined,
      levelOptions: [],
      locationOptions: [],
      majorOptions: [],
      classifyOptions: [],
      responseList:
        {
          rate: undefined,
          volunteerVO: {}
        },
      boolOptions: [
        {
          value: 1,
          label: '一段志愿'
        }, {
          value: 0,
          label: '二段志愿'
        }
      ],
      //表单参数
      form: {
        formId: undefined,
        volunteerId: undefined,
        volunteerPosition: undefined,
        section: undefined,
        name: undefined,
        professionalName: undefined,
      },
      // 表单校验
      rules: {
        volunteerPosition: {required: true, message: "志愿序号不能为空", trigger: "blur"},
        section: {required: true, message: "志愿批次不能为空", trigger: "blur"},
      }
    }
  },

  created() {
    this.selectBatch();
    this.selectRegion();
    this.selectSchoolType();
    this.selectMajorType();
    this.getCurrent();
    this.fetchData();
  },

  methods: {
    //请求列表数据
    fetchData() {
      this.loading = true;
      this.getArrayBatch();
      this.getArrayRegion();
      this.getClassification();
      //默认加载全部数据
      if (!this.listQuery.type) {
        this.listQuery.type = 0;
      }
      if (!this.listQuery.universityName) {
        this.listQuery.universityName = "";
      }
      if (!this.listQuery.majorName) {
        this.listQuery.majorName = "";
      }
      getVOList(this.listQuery).then(response => {
        this.loading = false;
        this.responseList = [];
        this.responseList = response.data.content;
        this.volunteerList = [];
        this.responseList.forEach(item => {
          this.volunteerList.push({
            "rate": item.rate,
            "volunteerId": item.volunteerVO.id,
            "name": item.volunteerVO.name,
            "province": item.volunteerVO.province,
            "lowestScore": item.volunteerVO.score,
            "lowestPosition": item.volunteerVO.position,
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
        this.total = response.data.totalElements;
        this.listQuery.total = response.data.totalPages;
      })
    },
    getArrayBatch() {
      if (this.level) {
        let a = [this.level];
        this.listQuery.batch = [].concat(a);
      } else {
        this.listQuery.batch = []
      }
    },
    getArrayRegion() {
      let j;
      let info = this.location;
      if (this.location) {
        this.listQuery.region = [];
        for (j = 0; j < info.length; j++) {
          let array = info[j][1];
          this.listQuery.region.push(array);
        }
      } else {
        this.listQuery.region = [];
      }
    },
    getClassification() {
      let i;
      let info1 = this.classification;
      const newInfo = [];
      if (this.classification) {
        for (i = 0; i < info1.length; i++) {
          let array1 = info1[i][1];
          newInfo.push(array1);
        }
      }
      let feature, character, genre = [];
      feature = newInfo.filter(function (value) {
        return value >= 609 && value <= 612
      });
      this.listQuery.schoolTeSe = feature;
      character = newInfo.filter(function (value) {
        return value >= 616 && value <= 617
      });
      this.listQuery.schoolXingZhi = character;
      genre = newInfo.filter(function (value) {
        return value >= 619 && value <= 631
      });
      this.listQuery.schoolType = genre;
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
    getCurrent() {
      getCurrentInfo().then(response => {
        if (response) {
          this.form.formId = response.data.id;
          console.log("this.form.formId")
          console.log(this.form.formId)
        }
      })
    },
    cancel() {
      this.dialogFormVisible = false;
      this.reset();
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
    },
    changeLocation(value) {
      this.$forceUpdate();
      console.log(value)
    },
    changeClassify(value) {
      this.$forceUpdate();
      console.log(value)
    },
    selectBatch() {
      getBatch().then(response => {
        if (response) {
          this.levelOptions = response.data;
        }
      })
    },
    selectRegion() {
      getRegion().then(response => {
        if (response) {
          this.locationOptions = response.data;
        }
      })
    },
    selectSchoolType() {
      getSchoolType().then(response => {
        if (response) {
          this.classifyOptions = response.data;
        }
      })
    },
    selectMajorType() {
      getMajorType().then(response => {
        if (response) {
          this.majorOptions = response.data;
        }
      })
    },
    /** 搜索按钮操作 */
    handleQuery(type) {
      this.listQuery.page = 1;
      this.listQuery.type = type;
      this.fetchData();
    },
    // 分页方法
    fetchNext() {
      this.listQuery.page = this.listQuery.page + 1
      this.fetchData()
    },
    fetchPrev() {
      this.listQuery.page = this.listQuery.page - 1
      this.fetchData()
    },
    fetchPage(page) {
      this.listQuery.page = page
      this.fetchData(page)
    },
    changeSize(limit) {
      this.listQuery.limit = limit
      this.fetchData()
    },
  }

}
