import { getBatch, getMajorType, getRegion, getSchoolType } from "../../api/screen";
import { autoGenerateForm } from "@/api/recommand"
import { changeCurrentForm } from "@/api/volunteer"
import { STATUS_CODE } from "@/api/statusCode";
import { getInfo } from '../../api/login'

export default {
  data() {
    const validatePlan = (rule, value, callback) => {
      let sum = 0;
      this.plan.forEach(num => {
        sum += parseInt(num);
      });
      //console.log(sum);
      if (sum != 96) {
        callback(new Error('三个志愿总和应该是96'));
      } else {
        callback();
      }
    };
    const validateInput = (rule, value, callback) => {
      let total = 0;
      for(let i = 0; i < this.Input_form.PlanArr.length; ++i) {
        let planNum = parseInt(this.Input_form.PlanArr[i].value);
        if (window.isNaN(planNum)) {
          this.Input_form.PlanArr[i].value = 0;
          planNum = 0;
        }
        if (!(0 <= planNum && planNum <= 96)) {
          callback(new Error('三个志愿总和应该是96'));
        }
        total += planNum;
      };
      if(total != 96) {
        callback(new Error('三个志愿总和应该是96'));
      } else {
        callback();
      }
    };
    return {
      activeTab: 2,
      dialogFormVisible: false,
      disabled: true,
      //selectAllLocation: false,
      userInfo: {
        score: 0,
        subject: [],
        provinceRank: 0,
        id: 0
      },
      form: {
        major: [],
        school: [],
        batch: undefined,
        region: [],
        schoolTeSe: [],
        schoolXingZhi: [],
        schoolType: []
      },
      //多选参数
      multiProps: {multiple: true},
      levelOptions: [],
      //majorOptions: [],
      locationOptions: [],
      //totalLocationLength: 0,
      classifyOptions: [],
      plan: [25, 46, 25],
      isAutoRecommand: true,
      validateRules: {
        plan: [{ validator: validatePlan, trigger: 'blur' }],
        batch: [{ required: true, message: '请选择填报批次', trigger: 'blur'}],
      },

      Input_form: {
        PlanArr: [
          {
            title: "冲击",
            placeholder: "请输入冲击数",
            value: '',
          },
          {
            title: "稳妥",
            placeholder: "请输入稳妥数",
            value: '',
          },
          {
            title: "保底",
            placeholder: "请输入保底数",
            value: '',
          }
        ],
      },
      checkRules: [{ validator: validateInput, trigger: 'none'}],
      vipIsOrNot: false,
      vipDialog: false
    };
  },
  methods: {
    initData() {
      //if (getToken())
      this.userInfo = JSON.parse(localStorage.getItem('userGaoKaoInfo'));
      // console.log(this.userInfo);
      this.selectBatch();
      this.selectRegion();
      this.selectSchoolType();
      //this.selectMajorType();
    },
    getArrayRegion() {
      let j;
      let info = this.form.region;
      if (this.form.region) {
        this.form.region = [];
        for (j = 0; j < info.length; j++) {
          let array = info[j][1];
          this.form.region.push(array);
        }
      }
    },
    getClassification() {
      let i;
      let info1 = this.form.school;
      const newInfo = [];
      if (this.form.school) {
        for (i = 0; i < info1.length; i++) {
          let array1 = info1[i][1];
          newInfo.push(array1);
        }
      }
      let feature, character, genre = [];
      feature = newInfo.filter(function (value) {
        return value >= 609 && value <= 612
      });
      this.form.schoolTeSe = feature;
      character = newInfo.filter(function (value) {
        return value >= 616 && value <= 617
      });
      this.form.schoolXingZhi = character;
      genre = newInfo.filter(function (value) {
        return value >= 619 && value <= 631
      });
      this.form.schoolType = genre;
    },
    onSubmit() {
      this.$refs['form'].validate((isValid) => {
        if (isValid) {
          getInfo().then(res => {
            this.vipIsOrNot = res.data.vipIsOrNot
            if (!this.vipIsOrNot) {
              this.vipDialog = true
              return
            }
            //console.log(parseInt(this.plan[2]));
            this.getClassification();
            this.getArrayRegion();
            let totalForm = {
              chongRate: window.isNaN(parseInt(this.plan[0])) ? 0 : parseInt(this.plan[0]),
              baoRate: window.isNaN(parseInt(this.plan[1])) ? 0 : parseInt(this.plan[1]),
              wenRate: window.isNaN(parseInt(this.plan[2])) ? 0 : parseInt(this.plan[2]),
              region: this.form.region,
              batch: [this.form.batch],
              schoolTeSe: this.form.schoolTeSe,
              schoolXingZhi: this.form.schoolXingZhi,
              schoolType: this.form.schoolType,
              score: this.userInfo.score,
              subject: this.userInfo.subject,
              userId: this.userInfo.id,
              majorName: '',
              page: 1,
              limit: 96,
              total: 96,
              type: 0,
              universityName: ''
            };
            autoGenerateForm(totalForm).then(res => {
              //console.log(res);
              if (res.code === STATUS_CODE.SUCCESS) {
                this.$message({
                  type: 'success',
                  message: '智能推荐生成志愿表成功'
                });
                changeCurrentForm({newFormId: res.data.id}).then(res => {
                  if (res.code === STATUS_CODE.SUCCESS) {
                    this.$router.push('/preference');
                  }
                });
              }
            });
          })
        } else {
          return false;
        }
      })
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
          this.locationOptions.push.apply(this.locationOptions, response.data);
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
    Click1() {
      this.activeTab = 1;
      this.$forceUpdate();
      this.plan[0]=40;
      this.plan[1]=30;
      this.plan[2]=26;
    },
    Click2() {
      this.activeTab = 2;
      this.$forceUpdate();
      this.plan[0]=25;
      this.plan[1]=46;
      this.plan[2]=25;
    },
    Click3() {
      this.activeTab = 3;
      this.$forceUpdate();
      this.plan[0]=20;
      this.plan[1]=30;
      this.plan[2]=46;
    },
    Click4() {
      this.$forceUpdate();
      this.dialogFormVisible = true;
    },
    submitForm(formName) {
      this.dialogFormVisible = false;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$message({
            type: 'success',
            message: '自定义志愿成功'
          });
          this.activeTab = 4;
          for (let i = 0; i < 3; i++) {
            this.plan[i] = this.Input_form.PlanArr[i].value;
          }
        } else {
          this.$refs[formName].resetFields();
          this.$message({
            type: 'error',
            message: '自定义失败'
          })
          return false;
        }
      });
    },
    resetForm(formName) {
      this.dialogFormVisible = false;
      this.$refs[formName].resetFields();
    },
    closeDialog(param) {
      this.vipDialog = param
    }
  }
  ,
  computed: {
    userInfoStr() {
      const subjects = ['', '物理', '化学', '生物', '历史', '地理', '政治']
      if (this.userInfo.score) {
        let subs = this.userInfo.subject
        return '高考分数: ' + this.userInfo.score
          + ' / 排名: ' + this.userInfo.provinceRank
          + ' / 选科: ' + subjects[subs[0]] + ' ' + subjects[subs[1]] + ' ' + subjects[subs[2]]
      }
    }
  },
  mounted() {
    this.initData()
  }
}
{/* <script type='text/javascript'> */}
    (function(a, b, c, d, e, j, s) {
        a[d] = a[d] || function() {
            (a[d].a = a[d].a || []).push(arguments)
        };
        j = b.createElement(c),
            s = b.getElementsByTagName(c)[0];
        j.async = true;
        j.charset = 'UTF-8';
        j.src = 'https://static.meiqia.com/widget/loader.js';
        s.parentNode.insertBefore(j, s);
    })(window, document, 'script', '_MEIQIA');
    _MEIQIA('entId', 'a14c5c4c2fc2aae7257e952284f21cb0');
// </script>
