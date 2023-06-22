import { updatePwd } from '@/api/login'
import { logOut } from '@/utils/auth';
export default {
  data() {
    var validateRepassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      form: {
        oldPassword: '',
        password: '',
        rePassword: ''
      },
      activeName: 'updatePwd',
      user: {},
      rules: {
        oldPassword: [
          { required: true, message: '原密码不能为空', trigger: ['blur', 'change'] }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: ['blur', 'change'] },
          { min: 5, max: 100, message: '密码长度不能小于5', trigger: 'blur' }
        ],
        rePassword: [
          { required: true, message: '密码不能为空', trigger: ['blur', 'change'] },
          { min: 5, max: 100, message: '密码长度不能小于5', trigger: ['blur', 'change'] },
          { validator: validateRepassword, trigger: ['blur', 'change'], required: true }
        ]
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.user = this.$store.state.user.profile
    },
    handleClick(tab, event) {
      this.$router.push({ path: '/account/' + tab.name })
    },
    updatePwd() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          updatePwd({
            oldPassword: this.form.oldPassword,
            password: this.form.password
          }).then(response => {
            this.$message({
              message: '密码修改成功，请重新登录',
              type: 'success',
              duration: 700
            })
            setTimeout(() => {
              logOut()
              this.$router.push({ path: '/login' })
              window.location.reload()
            }, 800)
          }).catch(err => {
            this.$message({
              message: err,
              type: 'error'
            })
          })
        } else {
          return false
        }
      })
    }
  }
}
