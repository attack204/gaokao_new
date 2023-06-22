import {isvalidUsername} from '@/utils/validate'
import LangSelect from '@/components/LangSelect'
import {getInfo, login} from "../../api/login";
import {STATUS_CODE} from "../../api/statusCode";
import {setToken, setUserInfo} from "../../utils/auth";
import {buildRouter} from "../../permission";

export default {
  name: 'login',
  components: {LangSelect},
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!isvalidUsername(value)) {
        callback(new Error('Please enter the correct user name'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 5) {
        callback(new Error('The password can not be less than 5 digits'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{required: true, trigger: 'blur', validator: validateUsername}],
        password: [{required: true, trigger: 'blur', validator: validatePassword}]
      },
      loading: false,
      pwdType: 'password'
    }
  },
  methods: {
    showPwd() {
      if (this.pwdType === 'password') {
        this.pwdType = ''
      } else {
        this.pwdType = 'password'
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          login(this.loginForm.username, this.loginForm.password).then((res) => {
            this.loading = false
            console.log(this.loading)
            if (res.code === STATUS_CODE.SUCCESS) {
              console.log('登陆成功')
              setToken(res.data)
              getInfo().then((res) => {
                if (res.code === STATUS_CODE.SUCCESS) {
                  setUserInfo(res.data)
                  this.$router.push({ path: '/' })
                } else {
                  this.$message({
                    message: res.msg,
                    type: 'error'
                  })
                }
              });

            } else {
              this.$message({
                message: res.msg,
                type: 'error'
              })
            }
          }).catch((_) => {
            this.loading = false;
          });
        } else {
          return false
        }
      })
    }
  }
}
