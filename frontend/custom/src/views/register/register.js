import {isvalidUsername} from '@/utils/validate'
// import {getInfo, login} from "../../api/login";
import {STATUS_CODE} from "../../api/statusCode";
// import {setToken, setUserInfo} from "../../utils/auth";
// import {buildRouter} from "../../permission";
import { register, sendVeryCode } from "@/api/register.js";
import { UserInfoDialog } from './components';
import store from '@/store';

export default {
  name: 'login',
  data() {
    const validateConfirmPwd = (rule, value, callback) => {
      //return callback(new Error('请输入确认密码'));
      if (value === '') {
        callback(new Error('请输入确认密码'));
      } else if (value != this.regForm.password) {
        callback(new Error('两次密码不一致'));
      } else {
        callback();
      }
    }
    const validatePhone = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('手机号不能为空'));
      } else {
        let regexp = /^[1][3,4,5,7,8][0-9]{9}$/;
        if (!regexp.test(value)) {
          callback(new Error('请输入正确的手机号'));
        } else {
          callback();
        }
      }
    }
    return {
      regForm: {
        phone: '',
        password: '',
        confirmpwd: '',
        veryCode: '',
        nickname: '',
        score: 0,
        subject: [],
        provinceRank: 0
      },
      regRules: {
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 1, max: 14, message: '长度不允许超过14个字符', trigger: 'blur'}
        ],
        phone: [{ required: true, trigger: 'blur', validator: validatePhone }],
        //username: [{required: true, trigger: 'blur', validator: validateUsername}],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 8, message: '密码长度不能少于8位', trigger: 'blur'},
          { max: 14, message: '密码长度不能少于14位', trigger: 'blur'}
        ],
        confirmpwd: [{ required: true, trigger: 'blur', validator: validateConfirmPwd }],
        veryCode: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min: 4, max: 4, message: '验证码应该是4位', trigger: 'blur' }
        ]
      },
      loading: false,
      pwdType: 'password'
    }
  },
  components: {
    UserInfoDialog
  },
  methods: {
    initData() {
      let userGaokaoInfo = JSON.parse(localStorage.getItem('userGaoKaoInfo'));
      //console.log(JSON.parse(localStorage.getItem('userGaoKaoInfo')));
      this.regForm.score = userGaokaoInfo.score;
      this.regForm.provinceRank = userGaokaoInfo.provinceRank;
      this.regForm.subject = userGaokaoInfo.subject;
    },
    showPwd() {
      if (this.pwdType === 'password') {
        this.pwdType = ''
      } else {
        this.pwdType = 'password'
      }
    },
    showInfoDialog() {
      store.commit('SHOW_DIALOG', true);
    },
    getVeryCode() {
      sendVeryCode().then((res) => {
        if (res.code === STATUS_CODE.SUCCESS) {
          alert("验证码是" + res.msg);
        } else {
          //出现错误可能是没启动redis服务(顺带一提验证码现在可以乱写);
        }
      })
    },
    handleRegister() {
      this.$refs['regForm'].validate((valid) => {
        if (valid) {
          register(this.regForm).then((res) => {
            console.log(res);
            if (res.code === STATUS_CODE.SUCCESS) {
              this.$message({
                type: 'success',
                message: '注册成功'
              });
              this.$router.push('/login');
            }
          })
        } else {
          return false;
        }
      })
    }
  },
  computed: {
    userInfoStr() {
      const subjects = ['', '物理', '化学', '生物', '历史', '地理', '政治']
      if (this.regForm.score) {
        let subs = this.regForm.subject
        return '高考分数: ' + this.regForm.score
          + ' / 排名: ' + this.regForm.provinceRank
          + ' / 选科: ' + subjects[subs[0]] + ' ' + subjects[subs[1]] + ' ' + subjects[subs[2]]
      }
    }
  },
  mounted() {
    this.initData();
  },
}
