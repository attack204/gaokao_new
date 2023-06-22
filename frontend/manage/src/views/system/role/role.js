import {
  remove,
  getList,
  save,
  savePermissons,
  permTreeListByRoleId
} from "@/api/system/role";
import { getList as getPermList } from "@/api/system/menu";

export default {
  data() {
    return {
      //对话框
      formVisible: false,
      formTitle: "添加角色",
      isAdd: true,
      //选中角色的信息
      selRow: {},
      //添加与修改使用的表单
      form: {
        permIds: [],
        name: "",
        id: 0
      },
      //输入限制
      rules: {
        name: [
          { required: true, message: "请输入角色名称", trigger: "blur" },
          { min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur" }
        ]
      },
      //获取展示列表的请求参数：关键字，第几页，每页条数
      listQuery: {
        keyword: undefined,
        page: undefined,
        size: undefined
      },
      //当前页总角色数量，角色列表，加载中标志
      total: 0,
      list: null,
      listLoading: true,

      //权限配置相关
      permissonVisible: false,
      //权限列表（暂时）
      permList: [],
      permTree: {
        show: true,
        defaultProps: {
          id: "id",
          label: "name",
          children: "children"
        }
      }
    };
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: "success",
        draft: "gray",
        deleted: "danger"
      };
      return statusMap[status];
    }
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.fetchData();
    },
    fetchData() {
      this.listLoading = true;
      //获取展示列表
      getList(this.listQuery).then(response => {
        this.data = response.data;
        this.list = response.data.content;
        this.listLoading = false;
        this.total = response.data.totalPages;
      });
      //权限树，暂时先这样
      getPermList().then(response => {
        this.$data.permList = response.data;
      });
    },
    search() {
      this.fetchData();
    },
    reset() {
      this.listQuery.keyword = "";
      this.fetchData();
    },

    fetchPage(page) {
      this.listQuery.page = page;
      this.fetchData();
    },


    /**
     * fuck element-ui
     */
    checkParent(data, TreeData) {
      let toDoDeal = [];
      function permListFind(node, val) {
        for(let item of node) {
          if(item.id == val) return item;
          if(item.children != null) {
            let nxtStep = permListFind(item.children, val);
            if(nxtStep != null) return nxtStep;
          }
        }
        return null;
      }
      function permListSet(node, status) {
        if(node == null) return ;
        toDoDeal.push(node.id);
        if(node.children != null) {
          for(let item of node.children) {
            permListSet(item, status);
          }
        }
      }
      let status = 0;
      for(let i of TreeData.checkedKeys) {
        if(i == data.id) {
          status = 1;
          break;
        }
      }
      this.$refs.permTree.setChecked(data.id, status);


      //选中子节点，如果父节点未被选中，则选中父节点
     /* if (status == true) {
        let cur = permListFind(this.permList, data.pid)
        while(cur != null) {
          this.$refs.permTree.setChecked(cur.id, 1);
          cur = permListFind(this.permList, cur.pid);
        }
      }*/


      if(data.children != null) {
        let cur = permListFind(this.permList, data.id);
        toDoDeal.length = 0;
        permListSet(cur, status);
        console.log(toDoDeal);
        for(let item of toDoDeal) {
          this.$refs.permTree.setChecked(item, status);
        }
      }



    },
    handleCurrentChange(currentRow, oldCurrentRow) {
      this.selRow = currentRow;
    },
    resetForm() {
      this.form = {
        permIds: [],
        name: "",
        id: 0
      };
    },
    onDialogOpened() {
      this.$refs.permTree.setCheckedKeys(this.form.permIds);
    },
    add() {
      this.resetForm();
      this.formTitle = "添加角色";
      this.formVisible = true;
      this.isAdd = true;
    },
    edit(row) {
      this.resetForm();
      this.isAdd = false;
      this.form.id = row.id;
      this.form.name = row.name;
      this.form.permIds = row.permIds;
      this.formTitle = "修改角色";
      this.formVisible = true;
    },
    //保存操作，新建与修改共用
    saveChange() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.permIds = this.$refs.permTree.getCheckedKeys();
          save(this.form, this.isAdd).then(response => {
            this.$message({
              message: "提交成功",
              type: "success"
            });
            this.fetchData();
            this.formVisible = false;
          });
        } else {
          return false;
        }
      });
    },
    //删除角色
    remove(row) {
      const id = row.id;
      this.$confirm("确定删除该记录?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          remove(id).then(response => {
            this.$message({
              message: "提交成功",
              type: "success"
            });
            this.fetchData();
          });
        })
        .catch(() => {});
    }
  }
};
