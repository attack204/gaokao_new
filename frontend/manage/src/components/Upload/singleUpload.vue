<template>
  <div>
    <el-upload
      action="#"
      :http-request="uploadFile"
      :data="useOss?dataObj:null"
      list-type="picture"
      :multiple="false" :show-file-list="showFileList"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview">
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="fileList[0].url" alt="">
    </el-dialog>
  </div>
</template>
<script>
  import { upload } from '@/api/oss'
  import { OSS_UTILS } from '../../utils/oss'
  import { Message } from 'element-ui'

  const imageConversion = require('image-conversion')

  export default {
    name: 'singleUpload',
    props: {
      value: String
    },
    computed: {
      imageUrl() {
        return this.value
      },
      imageName() {
        if (this.value != null && this.value !== '') {
          return this.value.substr(this.value.lastIndexOf('/') + 1)
        } else {
          return null
        }
      },
      fileList() {
        return [{
          name: this.imageName,
          url: this.imageUrl
        }]
      },
      showFileList: {
        get: function() {
          return this.value !== null && this.value !== '' && this.value !== undefined
        },
        set: function(newValue) {
        }
      }
    },
    data() {
      return {
        dataObj: {
          policy: '',
          signature: '',
          key: '',
          ossaccessKeyId: '',
          dir: '',
          host: ''
          // callback:'',
        },
        dialogVisible: false,
        useOss: true, // 使用oss->true;
        ossUploadUrl: OSS_UTILS.OSS_UPLOAD_URL
      }
  },
    methods: {
      emitInput(val) {
        this.$emit('input', val)
      },
      handleRemove(file, fileList) {
        this.emitInput('')
      },
      handlePreview(file) {
        this.dialogVisible = true
      },
      beforeUpload(file) {
        const fileSizeM = file.size / 1024 / 1024
        file.fileSizeM = fileSizeM
        const isLt10M = fileSizeM < 10
        if (!isLt10M) {
          this.$message.error('单张图片大小不能超过 10MB!')
          return false
        }
        if (!/\.(png|jpg|jpeg)$/.test(file.name.toLowerCase())) {
          this.$message.error('上传格式不正确，请上传png或者jpg或者jpeg格式')
          return false
        }
        return true
      },
      uploadFile(e) {
        const file = e.file
        if (file.fileSizeM > 1) {
          const zipMsg = Message('压缩中', {
            duration: 0,
            showClose: true
          })
          imageConversion.compressAccurately(file, file.fileSizeM * 100).then(res => {
            const compressedFile = new File([res], file.name, { type: file.type, lastModified: Date.now() })
            compressedFile.uid = file.uid
            this.uploadActual(compressedFile)
          }).finally(() => {
            zipMsg.close()
          })
        } else {
          this.uploadActual(file)
        }
      },
      uploadActual(file) {
        const uploadMsg = Message('上传中', {
          duration: 0,
          showClose: true
        })
        const formData = new FormData()
        formData.append('image', file)
        upload(formData).then(response => {
          this.showFileList = true
          this.fileList.pop()
          const url = response.data
          this.fileList.push({ name: file.name, url: url })
          this.emitInput(this.fileList[0].url)
          this.$message.success('上传成功')
        }).catch(err => {
          console.log(err)
          this.$message.error('上传失败：' + err)
        }).finally(() => {
          uploadMsg.close()
        })
      },
      // 使用http-request覆盖action，此方法用不到
      handleUploadSuccess(res, file) {
        this.showFileList = true
        this.fileList.pop()
        const url = this.dataObj.host + '/' + this.dataObj.dir + '/' + file.name
        this.fileList.push({ name: file.name, url: url })
        this.emitInput(this.fileList[0].url)
      }
    }
  }
</script>
<style>

</style>


