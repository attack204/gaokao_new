import { updatePwd } from '@/api/login'
import { getInfo, updateUserInfo } from '@/api/login'
import { SUBJECT_TYPE } from '../../consts/Subject'
import { getRank } from '@/api/recommand'
export default {
  data() {
    return {
      activeName: 'profile',
      dialogVisible: false,
      user: {
        id: 0,
        username: '',
        vipIsOrNot: false,
        nickname: '',
        score: 0,
        provinceRank: 0,
        subject: '',
        phone: '',
        email: ''
      },
      edit: false,
      subjectList: [],
      subject: ''
    }
  },
  mounted: function() {
    this.init()
  },
  methods: {
    init() {
      getInfo().then(res => {
        this.user.id = res.data.id
        this.user.username = res.data.username
        this.user.phone = res.data.phone
        this.user.vipIsOrNot = res.data.vipIsOrNot
        this.user.nickname = res.data.nickname
        this.user.score = res.data.score
        this.user.provinceRank = res.data.provinceRank
        res.data.subject.forEach((value) => {
          this.user.subject = this.user.subject + SUBJECT_TYPE[value - 1]
          this.subjectList.push(value.toString())
        })
        this.user.email = res.data.email
      })
    },
    handleClick(tab, event) {
      this.$router.push({path: '/account/' + tab.name})
    },
    onClickEdit() {
      this.dialogVisible = true
      if (!this.edit) {
        // 进入修改页面
        this.edit = !this.edit
      } else {
        // 保存修改结果
        if (this.user.username === '') {
          this.$message({
            message: '用户名不能为空',
            type: 'error'
          })
          this.$refs.username.focus()
          return
        }
        // if (this.user.phone === '' || this.user.phone.length !== 11) {
        //   this.$message({
        //     message: '手机号码输入有误',
        //     type: 'error'
        //   })
        //   this.$refs.phone.focus()
        //   return
        // }
        if (!this.user.vipIsOrNot && this.subjectList.length < 3) {
          this.$message({
            message: '选课不够三门',
            type: 'error'
          })
          return
        }
        if (this.user.score < 100 || this.user.score > 750) {
          this.$message({
            message: '请输入正确分数！',
            type: 'error'
          })
          return
        }
        console.log(this.subjectList)
        this.edit = !this.edit
        if (!this.user.vipIsOrNot) {
          this.subject = '['
          for (let i = 0; i < 3; i++) {
            this.subject = this.subject + this.subjectList[i]
            if (i < 2) {
              this.subject = this.subject + ','
            }
          }
          this.subject = this.subject + ']'
        }
        const params = {
          id: this.user.id,
          nickname: this.user.nickname,
          phone: this.user.phone,
          username: this.user.username,
          provinceRank: this.user.provinceRank,
          score: this.user.score,
          subject: this.subject
        }
        updateUserInfo(params).then(res => {
          if (res.code === 200) {
            this.$message({
              message: '修个个人信息成功！',
              type: 'success',
              duration: 700
            })
            window.location.reload()
          } else {
            this.$message({
              message: '修改失败，请稍后再试',
              type: 'error'
            })
          }
        })
      }
    }, // edit结束
    getRank(score) {
      if (score > 100 && score < 750) {
        getRank(score).then(res => {
          if (res === 200) {
            this.user.provinceRank = res.data
            this.$message({
              message: '预测排名获取成功！',
              type: 'success',
              duration: 700
            })
          }
        })
      }
    }
  }
}
