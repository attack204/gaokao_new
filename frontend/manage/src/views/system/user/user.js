import { mapActions } from "vuex";
import { getAllRoles, allocRoles } from "@/api/system/user";
import { getList, addUser, updateUser, delUser } from "@/api/system/user";

import { isPlatformUser } from "../../../utils/auth";
import { getRoleDropDown } from "../../../api/system/role";
import { STATUS_CODE } from "../../../api/statusCode";

export default {
  data() {
    return {
      rolesList: [],
      listLoading: false,
      dataList: [],
      page: {
        keyword: "",
        total: 0,
        size: 8,
        page: 1
      },
      corpOptions: [],
      roleOptions: [],

      // 新建
      addLoading: false,
      addFormVisible: false,
      addForm: this.buildUser(),
      addFormRules: {
        corp: [{ required: true, message: "请输入所属公司", trigger: "blur" }],
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 5, max: 11, message: "长度在 5 到 11 个字符", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { min: 11, max: 11, message: "手机号长度不合法", trigger: "blur" }
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      },

      // 编辑
      updateLoading: false,
      updateFormVisible: false,
      updateForm: this.editUser(),
      updateFormRules: {
        corp: [{ required: true, message: "请输入所属公司", trigger: "blur" }],
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 5, max: 11, message: "长度在 5 到 11 个字符", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { min: 11, max: 11, message: "手机号长度不合法", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    buildUser() {
      return {
        corp: "",
        username: "",
        phone: "",
        password: "12345678",
        roles: [] /*字符名称 */,
        roleIds: [] /**对应的id */
      };
    },

    editUser() {
      return {
        corp: "",
        username: "",
        phone: "",
        roles: [],
        roleIds: []
      };
    },

    isPlatformUser() {
      return isPlatformUser();
    },

    save() {
      this.addFormVisible = true;
      this.addForm = this.buildUser();
    },
    updateSave(row) {
      this.updateFormVisible = true;
      this.updateForm = this.editUser();
      if (row) {
        this.updateForm = Object.assign(this.updateForm, row);
      }
    },
    updateSubmit() {
      this.$refs.updateForm.validate(valid => {
        if (valid) {
          this.updateLoading = true;
          updateUser(this.updateForm, this.updateForm.id)
            .then(res => {
              this.updateLoading = false;
              if (res) {

                this.updateFormVisible = false;
                this.listByPage(this.page.page);
                this.$refs.updateForm.resetFields();
              }
            })
            .catch(err => {
              this.updateLoading = false;
            });
        }

      });
    },

    //新增用户
    addUserSubmit() {
      this.$refs.addForm.validate(valid => {
        if (valid) {
          this.addLoading = true;
          addUser(this.addForm).then(res => {
            this.addLoading = false;
            if (res) {
              this.addFormVisible = false;
              this.listByPage(this.page.page);
              this.$refs.addForm.resetFields();
            }
          }).catch((error)=>{
            this.addLoading = false;
          })

        }
      });
    },

    handleDelete(index, row) {
      this.$confirm("确认删除用户： " + row.username + " 吗?", "提示", {
        type: "warning"
      }).then(() => {
        delUser(row.id).then(res => {
          if (res) {
            this.listByPage(this.page.page);
            this.$message({
              message: "删除成功",
              type: "success"
            });
          }
        });
      });
    },

    handleCurrentChange(page) {
      this.listByPage(page);
    },
    async listByPage(page) {
      if (page !== undefined) {
        this.page.page = page;
      } else {
        this.page.page = 1;
      }
      this.listLoading = true;
      getList(this.page.keyword, this.page.page, this.page.size).then(res => {
        this.listLoading = false;
        if (res) {
          this.page.total = res.data.totalPages;
          this.dataList = res.data.content;
        }
      });
    },
    fetchRole() {
      getRoleDropDown().then(res => {
        if (res.code === STATUS_CODE.SUCCESS) {
          this.roleOptions = res.data;
        }
      });
    }
  },
  mounted() {
    this.listByPage();
    this.fetchRole();
  }
};
