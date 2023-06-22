import {
  getCurrentVolunteer,
  createVolunteerForm,
  changeCurrentForm,
  updateVolunteerName,
  upVolunteer,
  downVolunteer,
  swapVolunteer,
  deleteVolunteer
} from "@/api/volunteer";
import { STATUS_CODE } from "@/api/statusCode";
export default {
  data() {
    return {
      loading: true,
      isExist: false,
      activeTable: 'first',
      dialogVisible: false,
      //新建和修改表名称共用一个对话框，mode为true时表示新建
      mode: true,
      formId: 0,
      userInfo: {
        id: 0,
        score: 0,
        subject: [],
        provinceRank: 0
      },
      toSwapIndex: undefined,
      currentFormName: '暂无志愿表，点击添加',
      firstTableData: [
      // {
      //   num: '志愿1',
      //   school: 'school',
      //   profession: 'profession',
      //   isExist: false,
      //   id: 0
      // }
      ],
      secondTableData: [

      ],
      tableData: [],
      createData: {
        generatedType: true,
        name: '',
        score: 0,
        subject: []
      },
      createRules: {
        name: [
          { required: true, message: '请输入志愿表名称', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    initData() {
      this.loading = true;
      this.userInfo = JSON.parse(localStorage.getItem("userGaoKaoInfo"));
      this.createData.score = this.userInfo.score;
      this.createData.subject = this.userInfo.subject;
      //初始化表格数据全为空
      this.firstTableData.splice(0);
      this.secondTableData.splice(0);
      for (let index = 1; index <= 96; ++index) {
        //let然后浅拷贝被坑了一下QAQ
        this.firstTableData.push({
          num: '志愿' + index,
          name: '',
          professionalName: '',
          isExist: false,
          id: 0,
          swapVisible: false,
        });
        this.secondTableData.push({
          num: '志愿' + index,
          name: '',
          professionalName: '',
          isExist: false,
          id: 0,
          swapVisible: false,
        });
      }
      //console.log('crdt', this.createData);
      getCurrentVolunteer().then(res => {
        //console.log(res);
        if (res.code === STATUS_CODE.SUCCESS) {
          this.isExist = true;
          this.userInfo.id = res.data.userId;
          this.formId = res.data.id;
          this.currentFormName = res.data.name;
          this.userInfo.score = res.data.score;
          this.userInfo.subject = res.data.subject;
          res.data.volunteerList.forEach(volunteer => {
            //console.log(volunteer);
            // this.tableData[volunteer.volunteerPosition - 1] = {
            //   school: volunteer.name,
            //   profession: volunteer.professionalName,
            //   isExist: true,
            //   id: volunteer.id,
            //   universityCode: volunteer.universityCode,
            //   fee: volunteer.fee,
            //   time: volunteer.time,
            //   lowestScore: volunteer.lowestScore,
            //   lowestPosition: volunteer.lowestPosition,
            //   enrollment: volunteer.enrollment
            // }
            //一段与二段区分开
            if (volunteer.volunteerSection) {
              console.log(1);
              this.firstTableData[volunteer.volunteerPosition - 1] = Object.assign(this.firstTableData[volunteer.volunteerPosition - 1], volunteer);
              this.firstTableData[volunteer.volunteerPosition - 1].isExist = 1;
            } else {
              console.log(2);
              this.secondTableData[volunteer.volunteerPosition - 1] = Object.assign(this.secondTableData[volunteer.volunteerPosition - 1], volunteer);
              this.secondTableData[volunteer.volunteerPosition - 1].isExist = 1;
            }
          });
          this.loading = false;
        }
      }).catch(res => {
        if (res.code === STATUS_CODE.FAIL) {
          this.isExist = false;
          this.loading = false;
        }
      });
      this.tableData = this.firstTableData;
      console.log(this.tableData, this.firstTableData, this.secondTableData);
    },
    createForm() {
      this.$refs['createVolForm'].validate(isvalid => {
        if (isvalid) {
          createVolunteerForm(this.createData).then(res => {
            if (res.code === STATUS_CODE.SUCCESS) {
              this.$message({
                message: '创建成功',
                type: 'success'
              });
              changeCurrentForm({
                newFormId: res.data,
                preFormId: this.formId,
                userId: this.userInfo.id
              }).then(res => {
                if (res.code === STATUS_CODE.SUCCESS) {
                  this.dialogVisible = false;
                  this.formId = res.data;
                  this.initData();
                }
              });
            }
          });
        }
      })
    },
    updateName() {
      this.$refs['createVolForm'].validate(isvalid => {
        if (isvalid) {
          console.log(this.formId);
          console.log(this.createData.name);
          let params = {
            formId: this.formId,
            name: this.createData.name
          }

          updateVolunteerName(params).then(res => {
            if (res.code === STATUS_CODE.SUCCESS) {
              this.$message({
                message: '修改成功',
                type: 'success'
              });
              this.currentFormName = this.createData.name;
              this.dialogVisible = false;
            }
          });
        }
      });
    },
    toScreen() {
      this.$router.push('/screen');
    },
    upVol(index) {
      if (index == 0) {
        this.$message({
          message: '不能继续上移了(>_<)',
          type: 'error'
        });
        return;
      }
      upVolunteer({
        formId: this.formId,
        section: true,
        volunteerId: this.tableData[index].id,
        volunteerPosition: index + 1
      }).then(res => {
        if (res.code === STATUS_CODE.SUCCESS) {
          this.$message({
            message: '移动成功',
            type: 'success'
          });
          this.initData();
        }
      });
    },
    downVol(index) {
      if (index == 95) {
        this.$message({
          message: '不能继续下移了(>_<)',
          type: 'error'
        });
        return;
      }
      downVolunteer({
        formId: this.formId,
        section: true,
        volunteerId: this.tableData[index].id,
        volunteerPosition: index + 1
      }).then(res => {
        if (res.code === STATUS_CODE.SUCCESS) {
          this.$message({
            message: '移动成功',
            type: 'success'
          });
          this.initData();
        }
      });
    },
    swapVol(index) {
      let tempIndex = this.toSwapIndex - 1;
      if (tempIndex < 0 || 95 < tempIndex) {
        this.$message({
          message: '填写志愿位置应该在1~96之间(>_<)',
          type: 'error'
        });
        return;
      }
      swapVolunteer({
        firstVolunteerId: this.tableData[index].id,
        firstVolunteerPosition: index + 1,
        formId: this.formId,
        secondVolunteerId: this.tableData[tempIndex].id,
        secondVolunteerPosition: tempIndex + 1,
        section: true
      }).then(res => {
        if (res.code === STATUS_CODE.SUCCESS) {
          this.$message({
            message: '移动成功',
            type: 'success'
          });
          this.initData();
        }
      });
    },
    deleteVol(index) {
      console.log(this.formId);
      deleteVolunteer({
        id: this.formId,
        section: true,
        volunteerPosition: index + 1
      }).then(res => {
        if (res.code === STATUS_CODE.SUCCESS) {
          this.$message({
            message: '删除成功',
            type: 'success'
          });
          this.initData();
        }
      });
    },
    changeCurrentData() {
      this.tableData = this.activeTable == 'first' ? this.firstTableData : this.secondTableData;
    }
  },
  mounted() {
    this.initData();
  },
  computed: {
    userInfoStr() {
      const subjects = ['', '物理', '化学', '生物', '历史', '地理', '政治']
      let subs = this.userInfo.subject;
      return '高考分数: ' + this.userInfo.score
        + ' / 排名: ' + this.userInfo.provinceRank
        + ' / 选科: ' + subjects[subs[0]] + ' ' + subjects[subs[1]] + ' ' + subjects[subs[2]]
    }
  }
}
