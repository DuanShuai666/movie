<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="中文名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入中文名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="原名" prop="originalName">
        <el-input
          v-model="queryParams.originalName"
          placeholder="请输入原名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生日" prop="birthday">
        <el-date-picker clearable
          v-model="queryParams.birthday"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择生日">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:person:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:person:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:person:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:person:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="personList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="TMDB人物ID" align="center" prop="id" />
      <el-table-column label="中文名" align="center" prop="name" />
      <el-table-column label="原名" align="center" prop="originalName" />
      <el-table-column label="性别" align="center" prop="gender" />
      <el-table-column label="头像" align="center" width="120">
        <template slot-scope="scope">
          <div class="profile-wrapper">
            <img
              v-if="scope.row.profilePath"
              :src="resolveProfilePath(scope.row.profilePath)"
              class="profile-img"
              alt="头像"
            />
            <span v-else class="profile-empty">无</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="简介" align="center" prop="biography" />
      <el-table-column label="生日" align="center" prop="birthday" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.birthday, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:person:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:person:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改演职人员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="中文名" prop="name">
          <el-input v-model="form.name" placeholder="请输入中文名" />
        </el-form-item>
        <el-form-item label="原名" prop="originalName">
          <el-input v-model="form.originalName" placeholder="请输入原名" />
        </el-form-item>
        <el-form-item label="性别：0未设置，1女，2男" prop="gender">
          <el-input v-model="form.gender" placeholder="请输入性别：0未设置，1女，2男" />
        </el-form-item>
        <el-form-item label="头像相对路径" prop="profilePath">
          <el-input v-model="form.profilePath" placeholder="请输入头像相对路径" />
        </el-form-item>
        <el-form-item label="简介" prop="biography">
          <el-input v-model="form.biography" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="生日" prop="birthday">
          <el-date-picker clearable
            v-model="form.birthday"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择生日">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="创建时间" prop="createdAt">
          <el-date-picker clearable
            v-model="form.createdAt"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPerson, getPerson, delPerson, addPerson, updatePerson } from "@/api/system/person"

export default {
  name: "Person",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 演职人员表格数据
      personList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        originalName: null,
        birthday: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "中文名不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询演职人员列表 */
    getList() {
      this.loading = true
      listPerson(this.queryParams).then(response => {
        this.personList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        originalName: null,
        gender: null,
        profilePath: null,
        biography: null,
        birthday: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加演职人员"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getPerson(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改演职人员"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePerson(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPerson(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除演职人员编号为"' + ids + '"的数据项？').then(function() {
        return delPerson(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/person/export', {
        ...this.queryParams
      }, `person_${new Date().getTime()}.xlsx`)
    },
    resolveProfilePath(path) {
      if (!path) {
        return ""
      }
      if (path.startsWith('http://') || path.startsWith('https://') || path.startsWith('//')) {
        return path
      }
      const tmdbImageBase = 'https://image.tmdb.org/t/p/w185'
      return path.startsWith('/') ? `${tmdbImageBase}${path}` : `${tmdbImageBase}/${path}`
    }
  }
}
</script>

<style scoped>
.profile-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 120px;
}
.profile-img {
  width: 80px;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.08);
}
.profile-empty {
  color: #999;
  font-size: 12px;
}
</style>
