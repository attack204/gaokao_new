<template>
  <div>
    <el-upload
      :multiple="true"
      action="#"
      :http-request="uploadFiles"
      :data="useOss?dataObj:null"
      list-type="picture-card"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-preview="handlePreview"
      :limit="maxCount"
      :on-exceed="handleExceed">
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>
<script>
import { upload } from '@/api/oss'
import { OSS_UTILS } from '../../utils/oss'
import { Message } from 'element-ui'

const imageConversion = require('image-conversion')

export default {
  name: 'multiUpload',
  props: {
    // 图片属性数组
    value: Array,
    // 最大上传图片数量
    maxCount: {
      type: Number,
      default: 6
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
      },
      dialogVisible: false,
      dialogImageUrl: null,
      useOss: true, // 使用oss->true;使用MinIO->false
      ossUploadUrl: OSS_UTILS.OSS_UPLOAD_URL
    }
  },
  computed: {
    fileList() {
      const fileList = []
      if (this.value) {
        for (let i = 0; i < this.value.length; i++) {
          fileList.push({ url: this.value[i] })
        }
      }
      return fileList
    }
  },
  methods: {
    emitInput(fileList) {
      const value = []
      for (let i = 0; i < fileList.length; i++) {
        value.push(fileList[i].url)
      }
      this.value = value
      this.$emit('input', value)
    },
    handleRemove(file, fileList) {
      this.emitInput(fileList)
    },
    handlePreview(file) {
      this.dialogVisible = true
      this.dialogImageUrl = file.url
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
    uploadFiles(e) {
      const file = e.file
      const uploadMsg = Message('上传中', {
        duration: 0,
        showClose: true
      })
      if (file.fileSizeM > 1) {
        imageConversion.compressAccurately(file, file.fileSizeM * 100).then(res => {
          const compressedFile = new File([res], file.name, { type: file.type, lastModified: Date.now() })
          compressedFile.uid = file.uid
          this.uploadActual(compressedFile, uploadMsg)
        })
      } else {
        this.uploadActual(file, uploadMsg)
      }
    },
    uploadActual(file, toast) {
      const formData = new FormData()
      formData.append('image', file)
      upload(formData).then(response => {
        const url = response.data
        if (url) {
          this.fileList.push({ name: file.name, url: url })
          this.$message.success('上传成功')
          this.emitInput(this.fileList)
        }
      }).catch(err => {
        console.log(err)
        this.$message.error('上传失败: ' + err)
      }).finally(() => {
        toast.close()
      })
    },
    handleExceed(files, fileList) {
      this.$message({
        message: '最多只能上传' + this.maxCount + '张图片',
        type: 'warning',
        duration: 1000
      })
    }
  }
}
</script>
<style>

</style>
