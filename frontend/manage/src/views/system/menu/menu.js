import treeTable from "@/components/TreeTable";
import { getList, delPerm, updatePerm } from "@/api/system/menu";

export default {
  name: "treeTableDemo",
  components: { treeTable },
  data() {
    return {
      showTree: false,
      defaultProps: {
        id: "code",
        label: "name",
        children: "children"
      },
      loading: true,
      listLoading: true,
      data: [],
      permNode: {},

      //新建
      addFormVisible: false,
      addLoading: false,
      addForm: this.buildPerm(),
      addFormRules: {
        id: [
          { required: true, message: "请输入权限id", trigger: "blur" },
          { min: 1, max: 20, message: "长度在 1 到 20 个字符", trigger: "blur" }
        ],
        pid: [
          { required: true, message: "请输入父权限id", trigger: "blur" },
          { min: 1, max: 20, message: "长度在 1 到 20 个字符，若仍报错，请重新输入", trigger: "blur" }
        ],
        name: [
          { required: true, message: "请输入权限名称", trigger: "blur" },
          { min: 1, max: 32, message: "长度在 1 到 32 个字符", trigger: "blur" }
        ],
        code: [
          { required: true, message: "请输入权限表达式", trigger: "blur" },
          { min: 1, max: 20, message: "长度在 1 到 20 个字符", trigger: "blur" }
        ]
      },

      //这个变量是这样子的，由于"新增权限"和"编辑"用的是同一个对话框，所以用此来记录一下状态
      //true表示新增，false表示编辑
      currentDialog: false
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.fetchData();
    },
    fetchData() {
      getList().then(response => {
        this.data = response.data;
        this.permNode = this.data[0]; //获取根节点
        console.log(this.permNode);
      });
    },
    /**
     * 点击左侧节点时，右侧列表获取被点击的节点信息
     * @param data为自动传入的当前点击的节点属性
     */
    handleNodeClick(data) {
      this.permNode = data;
    },
    /**
     * 初始化提示框中的信息
     * @returns
     */
    buildPerm() {
      return {
        code: "",
        id: "",
        name: "",
        pid: 0
      };
    },
    /**
     * 提示框中的信息
     * @param insert为true表示为新增子权限，否则为编辑
     */
    save(insert) {
      this.addFormVisible = true;
      this.currentDialog = insert;
      console.log("新增子权限");
      this.addForm = this.buildPerm();
      console.log(this.addForm);
      if (insert) {
        console.log(this.permNode.pid);
        this.addForm.pid = this.permNode.id;
      } else {
        console.log(this.permNode);
        this.addForm = Object.assign(this.addForm, this.permNode);
      }
    },
    /**
     * 提交数据输入框中的数据
     */
    addSubmit() {
      this.$refs.addForm.validate(valid => {
        if (valid) {
          this.addLoading = true;
          updatePerm(this.addForm, this.addForm.id, this.currentDialog)
            .then(res => {
              if (res) {
                this.$refs.addForm.resetFields();
              }
              this.addLoading = false;
              this.addFormVisible = false;
              this.fetchData();
            })
            .catch(error => {
              this.addLoading = false;
            });
        }
      });
    },
    /**
     * 删除框业务逻辑
     */
    del() {
      this.$confirm("确认删除吗?", "提示", {
        type: "warning"
      }).then(() => {
        delPerm(this.permNode.id)
          .then(res => {
            this.fetchData();
            if (res) {
              this.$message({
                message: "删除成功",
                type: "success"
              });
            }
          })
          .catch(error => {
            this.$message({
              message: "删除失败" + error,
              type: "error"
            });
          });
      });
    }
  }
};
