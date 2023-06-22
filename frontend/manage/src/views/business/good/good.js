import { auxiliaryComment as err } from "babel-core/lib/transformation/file/options/removed";
import {
  addGood, auditPassGood, auditUnPassGood, closeGood, fetchRemoteData, getGood, goodGetCategory, goodGetTag, offGood, onGood, outOfGood, unCloseGood, updateDatabase, updateGood
} from "../../../api/business/good";
import MultiUpload from "../../../components/Upload/multiUpload";
import SingleUpload from "../../../components/Upload/singleUpload";
import {STATUS_CODE} from "../../../api/statusCode";

export default {
  name: "Good",
  components: {
    SingleUpload,
    MultiUpload,
  },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      //选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      //总条数
      total: 0,
      // 显示搜索条件
      showSearch: true,
      //禁用
      edit: true,
      //商品创建编辑进度
      goodsCreateStep: 1,
      //列信息
      columns: [
        {key: 0, label: `高校名称`, visible: true},
        {key: 1, label: `所在省份`, visible: true},
        {key: 2, label: `所在市`, visible: true},
        {key: 3, label: `所在区/县`, visible: true},
        {key: 4, label: `详细地址`, visible: true},
        {key: 5, label: `官方网址`, visible: true},
        {key: 6, label: `官方电话`, visible: true},
        {key: 7, label: `官方邮箱`, visible: true},
        {key: 8, label: `学校档次`, visible: true},
        {key: 9, label: `人气值`, visible: true},
        
      ],
      // 显隐数据
      visibleValue: [],
      // toolbar弹出层标题
      toolbarTitle: "显示/隐藏",
      // 是否显示toolbar弹出层
      open: false,
      //服务列表
      goodList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      dialogFormVisible: false,
      // 弹出层标题
      auditTitle: "",
      // 是否显示弹出层
      notPassFormVisible: false,
      // 弹出层标题
      passTitle: "",
      // 是否显示弹出层
      passFormVisible: false,
      //审核通过按钮是否展示
      isAuditButtonShow: 'none',
      //提交按钮是否展示
      isSubmitButtonShow: '',
      //商品服务状态类型
      statusOptions: [
        {
          value: 10,
          label: '全部'
        },
        {
          value: 1,
          label: '在售中'
        },
        {
          value: 0,
          label: '审核中'
        },
        {
          value: -1,
          label: '已驳回'
        },
        {
          value: -2,
          label: '已下架'
        },
        {
          value: -3,
          label: '暂停服务'
        },
        {
          value: -4,
          label: '已封禁'
        }
      ],
      //选中的商品属性
      selectProductAttr: [],
      //分类字典
      categoryOptions: [],
      //分组数组
      dynamicTags: [],
      page: 1,
      size: 20,
      //请求服务列表的参数
      listQuery: {
        page: 1,
        size: 6,
        total: 0,
        name: '',
        status: 10
      },
      notPassForm: {auditOpinion: undefined},
      passForm: {
        tags: undefined,
        tagIds: [],
      },
      baseForm: {
        categoryId: undefined,
        name: undefined,
        thumbnail: undefined,
        carousels: [],
        summary: undefined,
        tagIds: undefined,
        auditOpinion: undefined,
        serviceCountPerPeriod: 10
      },
      detailForm: {
        toShopIsOrNot: true,
        door2doorIsOrNot: true,
        description: undefined
      },
      skuForm: {
        skus: [],
        //商品属性相关{productAttributeId: 0, value: ''}
        valueList: [],
      },
      // 表单校验
      notPassFormRules: {
        auditOpinion: [
          {required: true, message: "审核意见不能为空", trigger: "blur"},
        ]
      },
      passFormRules: {
        auditOpinion: [
          {required: true, message: "审核意见不能为空", trigger: "blur"},
        ]
      },
      baseFormRules: {
        name: [
          {required: true, message: "服务名称不能为空", trigger: "blur"},
        ],
        categoryId: [
          {required: true, message: "所属分类不能为空", trigger: "blur"}
        ],
        summary: [
          {required: true, message: "简要描述不能为空", trigger: "blur"}
        ],
        thumbnail: [
          {required: true, message: "请上传图片", trigger: "blur"}
        ],
        carousels: [
          {required: true, message: "请上传图片", trigger: "blur"}
        ],
        serviceCountPerPeriod: [
          {required: true, message: '两小时接单数不能为空', trigger: 'blur'}
        ]
      },
      detailFormRules: {},
      skuFormRules: {},

      //表单参数
      form: {
        id: undefined,
        categoryId: undefined,
        name: undefined,
        thumbnail: undefined,
        carousels: [],
        summary: undefined,
        door2doorIsOrNot: true,
        toShopIsOrNot: true,
        description: undefined,
        skus: [],
        valueList: [],
        auditOpinion: undefined,
        tags: undefined,
        tagIds: undefined,
        originalPrice: undefined,
        salePrice: undefined
      },
      //文件列表
      fileList: [],

    }
  },
  created() {
    this.fetchData();
    this.getCategoryList();
    this.getTagList();
    this.initToolbarColumn();
  },
  methods: {
    //请求服务列表数据
    fetchData(page) {
      if (page !== undefined) {
        this.listQuery.page = page;
      } else {
        this.listQuery.page = 1;
      }
      this.loading = true;
      getGood(this.listQuery).then(response => {
        if (response) {
          this.goodList = response.data.content;
          this.goodList = this.buildTagNames(this.goodList);
          this.total = response.data.totalElements;
          this.listQuery.total = response.data.totalPages;
        }
        this.loading = false;
      })
    },
    //获取sku最低价格
    getMinPrice(skus) {
      const array = []
      skus.forEach(e => {
        array.push(e.salePrice)
      })
      const minPrice = Math.min.apply(Math, array);
      return minPrice * 100;
    },
    // 右侧列表元素变化
    dataChange(data) {
      for (var item in this.columns) {
        const key = this.columns[item].key;
        this.columns[item].visible = !data.includes(key);
      }
    },
    // 打开显隐列dialog
    showColumn() {
      this.open = true;
    },
    // 分组id和分组名称填入form
    buildTagNames(goodList) {
      goodList.forEach(function (good) {
        const tags = good.tags;
        const tagNames = tags.map(function (obj, index) {
          return obj.name;
        }).join(',');
        good.tagNames = tagNames;
        if (tags.length === 0) {
          good.tagIds = [];
        } else {
          const tagIdStr = tags.map(function (obj, index) {
            return obj.id;
          }).join(',');
          good.tagIds = tagIdStr.split(',').map(Number);
        }
      })
      return goodList;
    },
    //获取分类信息列表
    getCategoryList() {
      goodGetCategory().then(response => {
        this.categoryOptions = response.data;
      });
    },
    //获取分组信息列表
    getTagList() {
      goodGetTag().then(response => {
        this.dynamicTags = response.data;
      });
    },
    // 显隐列初始默认隐藏列
    initToolbarColumn() {
      for (let item in this.columns) {
        if (this.columns[item].visible === false) {
          this.visibleValue.push(parseInt(item));
        }
      }
    },
    //表单重置
    formReset(refName) {
      if (this.$refs[refName]) {
        this.$refs[refName].resetFields();
      }
    },
    //表单重置
    reset() {
      this.baseForm = {
        categoryId: undefined,
        name: undefined,
        thumbnail: undefined,
        carousels: [],
        summary: undefined,
        tagIds: undefined,
        auditOpinion: undefined,
        serviceCountPerPeriod: 10
      };
      this.formReset("baseForm");
      this.detailForm = {
        toShopIsOrNot: true,
        door2doorIsOrNot: true,
        description: undefined
      };
      this.formReset("detailForm");
      this.skuForm = {
        skus: [],
        valueList: []
      };
      this.formReset("skuForm");
      this.goodsCreateStep = 1;
    },
    //上一步
    preStep() {
      if (this.goodsCreateStep > 0) {
        this.goodsCreateStep--;
      }
    },
    //下一步或提交
    nextStep(formName) {
      if (formName === 'skuForm') {
        this.submitForm();
      } else {
        this.$refs[formName].validate((valid) => {
          if (valid && this.goodsCreateStep < 3) {
            this.goodsCreateStep++;
          }
        })
      }
    },
    //审核
    audit(passOrNot) {
      if (passOrNot === 'pass') {
        this.passFormVisible = true;
        this.passTitle = "审核通过框";
        this.passForm.tagIds = this.baseForm.tagIds;
      } else {
        this.notPassFormVisible = true;
        this.auditTitle = "驳回信息框";
        this.notPassForm.auditOpinion = this.baseForm.auditOpinion;
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.listQuery.page = 1;
      this.fetchData();
    },
    /** 清空按钮操作 */
    resetQuery() {
      this.listQuery.name = '';
      this.listQuery.status = '';
      this.handleQuery();
    },
    /** 取消按钮操作 */
    cancel() {
      this.dialogFormVisible = false;
      this.reset();
    },
    auditCancel() {
      this.notPassFormVisible = false;
      this.notPassForm = {auditOpinion: undefined};
      this.formReset("notPassForm");
    },
    passCancel() {
      this.passFormVisible = false;
      this.passForm = {tags: undefined, tagIds: []};
      this.formReset("passForm");
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.form.id = '';
      this.title = "添加服务";
      this.edit = false;
      this.dialogFormVisible = true;
      this.uploadDialogVisible = false;
    },
    /** 删除按钮操作 */
    handleDel() {
      this.reset();
      this.form.id = '';
      this.title = "删除服务";
      this.edit = false;
      this.dialogFormVisible = true;
      this.uploadDialogVisible = false;
    },
    /** 修改按钮操作 */
    handleEdit() {
      this.reset();
      this.form.id = '';
      this.title = "修改服务";
      this.edit = false;
      this.dialogFormVisible = true;
      this.uploadDialogVisible = false;
    },
    beforeHandleCommand(flag, command) {
      return {
         'flag': flag,
         'command': command
      }
    },
    changeItem(val) {
      const formItem = val.command
      switch (val.flag) {
        case 'guide':
          this.viewItem(formItem)
          break
        case 'university':
          this.viewItem(formItem)
          break
        case 'major':
          this.viewItemw(formItem)
          break
        default:
          break
      }
    },

    /** 爬取按钮操作 */
    handleFetch() {
      fetchRemoteData().then((res) => {
        console.log(res);
        if(res.code === STATUS_CODE.SUCCESS)
        {
          this.$message({
            type: 'success',
            message: '爬取成功'
          });
          location.href = res.data;
          //this.$router.push('/')
        }
      })
    },

    /** 更新按钮操作 */
    /*handleUpdate() {
      updateDatabase().then((res) => {
        console.log(res);
        if(res.code === STATUS_CODE.SUCCESS)
        {
          this.$message({
            type: 'success',
            message: '更新成功'
          });
          //this.$router.push('/')
        }
      })
    },*/
    
    handleUpload() {
      this.$refs.upload.submit();
      updateDatabase().then((res) => {
        console.log(res);
        if(res.code === STATUS_CODE.SUCCESS)
        {
          this.$message({
            type: 'success',
            message: '更新成功'
          });
          //this.$router.push('/')
        }
      })
    },


    beforeUpload(file) {
      if(file.type == '' || file.type == null || file.type== undefined) {
        const fileExt = file.name.replace(/.+\./,"").toLowerCase();
        if(fileExt == "xls" || fileExt== "xlsx") {
          return true;
        }else{
          this.$message.error("上传的文件必须是Excel格式！");
          return false;
        }
      }else{
        return true;
      }
    },

    upModel() {
      this.uploading = false;
      this.file = [];
      this.dialogFormVisible = false;
      this.uploadDialogVisible = true;
    },

    postFile() {
      const fileObj = this.file;
      var fileData = new FormData();
      fileData.append('file', fileObj);
      let headers = {
        "Content-Type": "multipart/form-data"
      };
      this.uploading = true;
      this.$axios({
        method: 'post',
        url: '/xhr/vl/university/update',
        headers: headers,
        data: fileData
      }).then((res) => {
        console.log(res);
        if(res.code === STATUS_CODE.SUCCESS) {
          this.$message.success("读取成功");
          this.uploadDialogVisible = false;
        }else{
          this.$message.error("错误！请检查上传文件内容");
        }
      });
    },
    closeFile() {
      this.uploadDialogVisible = false;
    },
    
    handleExceed() {
      this.$message.warning('当前限制选择3个文件，请删除掉多余文件后继续上传');
    },
    uploadFile(item) {
      this.file=item.file;
    },
    beforeRemove(file) {
      return this.$confirm('确定移除 ${ file.name }?');
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },

    /** 修改按钮操作 */
   /* handleUpdate(row) {
      this.reset();
      this.title = "修改服务";
      this.edit = false;
      this.form.id = row.id;
      this.form.shopId = row.shopId;
      this.goodsCreateStep = 1;
      this.baseForm.categoryId = row.categoryId;
      this.baseForm.name = row.name;
      this.baseForm.serviceCountPerPeriod = row.serviceCountPerPeriod
      this.baseForm.thumbnail = row.thumbnail;
      this.baseForm.carousels = row.carousels;
      this.baseForm.summary = row.summary;
      this.detailForm.door2doorIsOrNot = row.door2doorIsOrNot;
      this.detailForm.toShopIsOrNot = row.toShopIsOrNot;
      this.detailForm.description = row.description;
      this.skuForm.skus = row.skus;
      this.skuForm.valueList = row.valueList;
      this.dialogFormVisible = true;
      this.isSubmitButtonShow = '';
      this.isAuditButtonShow = 'none';
    },
    */

    /** 提交按钮*/
    submitForm: function () {
      this.form.categoryId = this.baseForm.categoryId;
      this.form.name = this.baseForm.name;
      this.form.serviceCountPerPeriod = this.baseForm.serviceCountPerPeriod
      this.form.thumbnail = this.baseForm.thumbnail;
      this.form.carousels = this.baseForm.carousels;
      this.form.summary = this.baseForm.summary;
      this.form.door2doorIsOrNot = this.detailForm.door2doorIsOrNot;
      this.form.toShopIsOrNot = this.detailForm.toShopIsOrNot;
      this.form.description = this.detailForm.description;
      this.form.skus = this.skuForm.skus;
      this.form.originalPrice = this.getMinPrice(this.form.skus);
      this.form.salePrice = this.form.originalPrice;
      this.form.valueList = this.skuForm.valueList;
      console.log('submit');
      console.log(this.skuForm.valueList);
      if (this.form.door2doorIsOrNot === false && this.form.toShopIsOrNot === false) {
        this.$message({
          type: 'error',
          message: '上门服务和到店服务请至少开通一项'
        })
      } else {
        if (!this.form.originalPrice) {
          this.$message({
            type: 'error',
            message: '请添加商品销售规格(sku)的数据'
          })
        } else {
          if (this.form.id) {
            updateGood(this.form.id, this.form).then(response => {
              if (response.code === 200) {
                this.$message({
                  type: 'success',
                  message: '更新信息成功，请耐心等待审核'
                })
              }
              this.dialogFormVisible = false;
              this.fetchData();
            });
          } else {
            addGood(this.form).then(response => {
              if (response.code === 200) {
                this.$message({
                  type: 'success',
                  message: '添加信息成功，请耐心等待审核'
                })
                this.dialogFormVisible = false;
                this.fetchData();
              } else {
                this.$message({
                  type: 'error',
                  message: err.message
                })
              }
            });
          }
        }
      }
    },
    /** 审核服务按钮操作 */
    handleAudit(row) {
      this.title = "审核服务";
      this.edit = true;
      this.form.id = row.id;
      this.form.shopId = row.shopId;
      this.goodsCreateStep = 1;
      this.baseForm.categoryId = row.categoryId;
      this.baseForm.name = row.name;
      this.baseForm.thumbnail = row.thumbnail;
      this.baseForm.carousels = row.carousels;
      this.baseForm.tagIds = row.tagIds;
      this.baseForm.auditOpinion = row.auditOpinion;
      this.baseForm.summary = row.summary;
      this.detailForm.door2doorIsOrNot = row.door2doorIsOrNot;
      this.detailForm.toShopIsOrNot = row.toShopIsOrNot;
      this.detailForm.description = row.description;
      this.skuForm.skus = row.skus;
      this.skuForm.valueList = row.valueList;
      this.dialogFormVisible = true;
      this.isAuditButtonShow = '';
      this.isSubmitButtonShow = 'none';
    },
    /** 批量上架按钮操作*/
    handleOnSell(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认上架' + this.ids.length + '个商品?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return onGood(ids);
      }).then(() => {
        this.fetchData();
        this.$message({
          type: 'success',
          message: '服务上架成功'
        });
      });
    },
    /** 批量下架按钮操作*/
    handleOffSell(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认下架' + this.ids.length + '个商品?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return offGood(ids);
      }).then(() => {
        this.fetchData();
        this.$message({
          type: 'success',
          message: '服务下架成功'
        });
      });
    },
    /** 批量解封按钮操作*/
    handleUnClose(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认解封' + this.ids.length + '个商品?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return unCloseGood(ids);
      }).then(() => {
        this.fetchData();
        this.$message({
          type: 'success',
          message: '服务解封成功'
        });
      });
    },
    /** 批量封禁按钮操作*/
    handleClose(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认封禁' + this.ids.length + '个商品?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return closeGood(ids);
      }).then(() => {
        this.fetchData();
        this.$message({
          type: 'success',
          message: '服务封禁成功'
        });
      });
    },
    /** 暂停服务按钮操作*/
    handleOutOff(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认暂停服务' + this.ids.length + '个商品?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return outOfGood(ids);
      }).then(() => {
        this.fetchData();
        this.$message({
          type: 'success',
          message: '暂停服务成功'
        });
      });
    },
    /** 通过按钮*/
    handlePass() {
      this.passForm = {tags: undefined, tagIds: []};
      this.formReset("passForm");
      const ids = this.ids;
      this.passTitle = "审核通过页面";
      this.form.id = ids[0];
      this.passFormVisible = true;
    },
    /** 审核通过相关信息提交操作*/
    passSubmit: function () {
      this.$refs['passForm'].validate(valid => {
        if (valid) {
          auditPassGood(this.form.id, this.passForm).then(response => {
            if (response.code === 200) {
              this.$message({
                type: 'success',
                message: '审核配置信息发送成功'
              })
            }
            this.passFormVisible = false;
            this.dialogFormVisible = false;
            this.fetchData();
          });
        }
      });
    },
    /** 驳回按钮*/
    handleReject() {
      this.notPassForm = {auditOpinion: undefined};
      this.formReset("notPassForm");
      const ids = this.ids;
      this.auditTitle = "审核驳回页面";
      this.form.id = ids[0];
      this.notPassFormVisible = true;
    },
    /** 驳回信息提交操作*/
    auditSubmit: function () {
      this.$refs['notPassForm'].validate(valid => {
        if (valid) {
          this.form.auditOpinion = this.notPassForm.auditOpinion;
          auditUnPassGood(this.form.id, this.form.auditOpinion).then(response => {
            if (response.code === 200) {
              this.$message({
                type: 'success',
                message: '驳回信息发送成功'
              })
            }
            this.notPassFormVisible = false;
            this.dialogFormVisible = false;
            this.fetchData();
          });
        }
      });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 分页需要的4个方法
    fetchNext() {
      this.listQuery.page = this.listQuery.page + 1
      this.fetchData()
    },
    fetchPrev() {
      this.listQuery.page = this.listQuery.page - 1
      this.fetchData()
    },
    fetchPage(page) {
      this.fetchData(page)
    },
    changeSize(size) {
      this.listQuery.size = size
      this.fetchData()
    },
  }
}
