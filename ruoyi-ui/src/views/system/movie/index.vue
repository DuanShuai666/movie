<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="中文标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入中文标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="原标题" prop="originalTitle">
        <el-input
          v-model="queryParams.originalTitle"
          placeholder="请输入原标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上映日期" prop="releaseDate">
        <el-date-picker clearable
          v-model="queryParams.releaseDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择上映日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="上映年份" prop="year">
        <el-input
          v-model="queryParams.year"
          placeholder="请输入上映年份"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['system:movie:add']"
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
          v-hasPermi="['system:movie:edit']"
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
          v-hasPermi="['system:movie:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:movie:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <div class="table-scroll-top" ref="tableScrollTop" @scroll="syncScroll('top')">
      <div class="table-scroll-top-inner" ref="tableScrollTopInner"></div>
    </div>
    <div class="table-scroll" ref="tableScrollBottom" @scroll="syncScroll('bottom')">
      <el-table v-loading="loading" :data="movieList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="TMDB电影ID" align="center" prop="id" />
        <el-table-column label="海报" align="center" width="120">
          <template slot-scope="scope">
            <div class="poster-wrapper">
              <img
                v-if="scope.row.posterPath"
                :src="resolvePosterPath(scope.row.posterPath)"
                class="poster-img"
                alt="海报"
              />
              <span v-else class="poster-empty">无</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="中文标题" align="center" prop="title" />
        <el-table-column label="原标题" align="center" prop="originalTitle" />
        <el-table-column label="简介" align="center" width="340">
          <template slot-scope="scope">
            <el-tooltip
              class="item"
              effect="light"
              placement="top-start"
              popper-class="movie-overview-tooltip"
              :content="truncateOverview(scope.row.overview, 120)"
              v-if="scope.row.overview"
            >
              <div class="overview-cell">{{ scope.row.overview }}</div>
            </el-tooltip>
            <div class="overview-cell" v-else>—</div>
          </template>
        </el-table-column>
        <el-table-column label="上映日期" align="center" prop="releaseDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.releaseDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上映年份" align="center" prop="year" />
      <el-table-column label="片长" align="center" prop="runtime" />
      <el-table-column label="评分" align="center" prop="voteAverage" />
      <el-table-column label="评分人数" align="center" prop="voteCount" />
      <el-table-column label="热度值" align="center" prop="popularity" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:movie:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:movie:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改电影基本信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="中文标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入中文标题" />
        </el-form-item>
        <el-form-item label="原标题" prop="originalTitle">
          <el-input v-model="form.originalTitle" placeholder="请输入原标题" />
        </el-form-item>
        <el-form-item label="简介" prop="overview">
          <el-input v-model="form.overview" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="上映日期" prop="releaseDate">
          <el-date-picker clearable
            v-model="form.releaseDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择上映日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="上映年份" prop="year">
          <el-input v-model="form.year" placeholder="请输入上映年份" />
        </el-form-item>
        <el-form-item label="片长" prop="runtime">
          <el-input v-model="form.runtime" placeholder="请输入片长" />
        </el-form-item>
        <el-form-item label="评分" prop="voteAverage">
          <el-input v-model="form.voteAverage" placeholder="请输入评分" />
        </el-form-item>
        <el-form-item label="评分人数" prop="voteCount">
          <el-input v-model="form.voteCount" placeholder="请输入评分人数" />
        </el-form-item>
        <el-form-item label="热度值" prop="popularity">
          <el-input v-model="form.popularity" placeholder="请输入热度值" />
        </el-form-item>
        <el-form-item label="海报相对路径" prop="posterPath">
          <el-input v-model="form.posterPath" placeholder="请输入海报相对路径" />
        </el-form-item>
        <el-form-item label="背景图相对路径" prop="backdropPath">
          <el-input v-model="form.backdropPath" placeholder="请输入背景图相对路径" />
        </el-form-item>
        <el-form-item label="IMDB ID" prop="imdbId">
          <el-input v-model="form.imdbId" placeholder="请输入IMDB ID" />
        </el-form-item>
        <el-form-item label="创建时间" prop="createdAt">
          <el-date-picker clearable
            v-model="form.createdAt"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="更新时间" prop="updatedAt">
          <el-date-picker clearable
            v-model="form.updatedAt"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择更新时间">
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
import { listMovie, getMovie, delMovie, addMovie, updateMovie } from "@/api/system/movie"

export default {
  name: "Movie",
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
      // 电影基本信息表格数据
      movieList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        originalTitle: null,
        overview: null,
        releaseDate: null,
        year: null,
        runtime: null,
        voteAverage: null,
        voteCount: null,
        popularity: null,
        posterPath: null,
        backdropPath: null,
        imdbId: null,
        status: null,
        createdAt: null,
        updatedAt: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "中文标题不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
        updatedAt: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  mounted() {
    window.addEventListener('resize', this.updateTopScrollWidth)
    this.$nextTick(this.updateTopScrollWidth)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateTopScrollWidth)
  },
  methods: {
    /** 查询电影基本信息列表 */
    getList() {
      this.loading = true
      listMovie(this.queryParams).then(response => {
        this.movieList = response.rows
        this.total = response.total
        this.loading = false
        this.$nextTick(this.updateTopScrollWidth)
      })
    },
    updateTopScrollWidth() {
      const bottom = this.$refs.tableScrollBottom
      const topInner = this.$refs.tableScrollTopInner
      if (bottom && topInner) {
        topInner.style.width = `${bottom.scrollWidth}px`
      }
    },
    syncScroll(from) {
      const top = this.$refs.tableScrollTop
      const bottom = this.$refs.tableScrollBottom
      if (!top || !bottom) {
        return
      }
      if (from === 'top') {
        bottom.scrollLeft = top.scrollLeft
      } else {
        top.scrollLeft = bottom.scrollLeft
      }
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
        title: null,
        originalTitle: null,
        overview: null,
        releaseDate: null,
        year: null,
        runtime: null,
        voteAverage: null,
        voteCount: null,
        popularity: null,
        posterPath: null,
        backdropPath: null,
        imdbId: null,
        status: null,
        createdAt: null,
        updatedAt: null
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
      this.title = "添加电影基本信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMovie(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改电影基本信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMovie(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMovie(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除电影基本信息编号为"' + ids + '"的数据项？').then(function() {
        return delMovie(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/movie/export', {
        ...this.queryParams
      }, `movie_${new Date().getTime()}.xlsx`)
    },
    truncateOverview(text, maxLength) {
      if (!text) {
        return ""
      }
      return text.length > maxLength ? text.slice(0, maxLength).trim() + "..." : text
    },
    resolvePosterPath(path) {
      if (!path) {
        return ""
      }
      if (path.startsWith('http://') || path.startsWith('https://') || path.startsWith('//')) {
        return path
      }
      // TMDB 海报路径通常为 /xxx.jpg，前端直接根据 TMDB image URL 生成
      const tmdbImageBase = 'https://image.tmdb.org/t/p/w185'
      return path.startsWith('/') ? `${tmdbImageBase}${path}` : `${tmdbImageBase}/${path}`
    }
  }
}
</script>

<style scoped>
.table-scroll-top {
  width: 100%;
  overflow-x: auto;
  overflow-y: hidden;
  height: 20px;
  margin-bottom: 4px;
}
.table-scroll-top-inner {
  height: 1px;
}
.table-scroll {
  width: 100%;
  overflow-x: auto;
  margin-bottom: 16px;
}
.overview-cell {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  line-clamp: 2;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  word-break: break-word;
  line-height: 1.6em;
  max-height: 3.2em;
}
.movie-overview-tooltip {
  max-width: 280px;
  white-space: normal !important;
  overflow-wrap: break-word;
}
.movie-overview-tooltip .popper__content,
.movie-overview-tooltip .el-tooltip__popper {
  max-width: 280px;
  background: #ffffff !important;
  color: #111827 !important;
  border: 1px solid rgba(15,23,42,0.08) !important;
  box-shadow: 0 4px 12px rgba(15,23,42,0.08) !important;
}
.movie-overview-tooltip .popper__arrow,
.movie-overview-tooltip .el-tooltip__arrow {
  border-top-color: #ffffff !important;
  border-bottom-color: #ffffff !important;
}
.poster-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 120px;
}
.poster-img {
  width: 80px;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.08);
}
.poster-empty {
  display: inline-block;
  color: #999;
  font-size: 12px;
}
</style>
