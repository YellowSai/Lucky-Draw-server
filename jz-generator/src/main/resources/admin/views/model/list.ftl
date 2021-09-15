<template>
  <div class="app-container">
    <el-row>
      <el-input v-model="pagination.keywords" placeholder="请输入关键字" style="width: 200px" />
      <el-button type="primary" @click="reload${model.model}List">搜索</el-button>
      <#if config.operateAdd>
      <el-button type="success" @click="add">添加${model.comment}</el-button>
      </#if>
      <#if config.operateExport>
      <el-button type="primary" @click="exportExcel">导出EXCEL</el-button>
      </#if>
      <#if config.operateExport>
      <el-button type="primary" @click="importExcel">批量导入</el-button>
      </#if>
    </el-row>
    <el-table
      v-loading="loading"
      :data="${model.camelCaseField}List"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
    <#list model.fieldList as field>
      <el-table-column label="${field.comment}" prop="${field.camelCaseField}" />
    </#list>

      <el-table-column fixed="right" label="操作" width="100">
        <template slot-scope="scope">
          <#if config.operateEdit>
          <el-button type="text" size="small" @click="edit(scope.$index)">编辑</el-button>
          </#if>
          <#if config.operateDelete>
          <el-button type="text" size="small" @click="del(scope.$index)">删除</el-button>
          </#if>
        </template>
      </el-table-column>
    </el-table>
    <el-row>
      <el-pagination
        background
        layout="total,sizes, prev, pager, next,jumper"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        @size-change="onSizeChange"
        @current-change="onPageChange"
      />
    </el-row>
  </div>
</template>

<script>
import { ${model.camelCaseField}Del, ${model.camelCaseField}List } from '@/api/${model.model}Api'
import { getToken } from '@/utils/auth'

export default {
  data() {
    return {
      ${model.camelCaseField}List: [],
      loading: false,
      pagination: {
        keywords: '',
        current: 1,
        size: 10,
        total: 0
      },
      headers: { 'authToken': getToken() }
    }
  },
  mounted() {
    this.reserveQuery()
    this.load${model.model}List()
  },
  methods: {
    reserveQuery() {
      const query = this.$route.query
      query.current = /\d+/.test(query.current) ? parseInt(query.current) : 1
      query.total = /\d+/.test(query.total) ? parseInt(query.total) : 0
      query.size = /\d+/.test(query.size) ? parseInt(query.size) : 10
      this.pagination = Object.assign(this.pagination, query)
    },
    reload${model.model}List() {
      this.pagination.current = 1
      this.load${model.model}List()
    },
    load${model.model}List() {
      this.loading = true
      ${model.camelCaseField}List(this.pagination).then(res => {
        this.loading = false
        const data = res.data
        this.${model.camelCaseField}List = data.records
        this.pagination.total = data.total
      }).catch(err => {
        this.loading = false
        this.$message.error('Error: ' + err.message)
      })
    },
    onSizeChange(size) {
      this.pagination.size = size
      this.load${model.model}List()
    },
    onPageChange(page) {
      this.pagination.current = page
      this.load${model.model}List()
    },
    add() {
      this.$router.push({ path: '/${model.camelCaseField}/add/', query: this.pagination })
    },
    edit(index) {
      const data = this.list[index]
      this.$router.push({ path: '/${model.camelCaseField}/edit/' + data.id, query: this.pagination })
    },
    del(index) {
      this.$confirm('确定删除该${model.comment}吗').then(() => {
        const data = this.list[index]
        ${model.camelCaseField}Del(data.id).then(res => {
          this.list.splice(index, 1)
          this.$message.success('删除成功')
        }).catch(err => {
          this.$message.error('Error: ' + err.message)
        })
      }).catch(() => {
        this.$message.info('取消删除')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
