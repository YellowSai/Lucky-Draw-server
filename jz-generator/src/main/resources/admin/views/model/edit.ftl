<template>
  <div class="app-container">
    <el-form ref="${model.camelCaseField}Form" :inline="true" :model="${model.camelCaseField}" :rules="${model.camelCaseField}FormRules" label-width="150px">
    <#list model.fieldList as field>
      <el-row>
        <el-form-item label="${field.comment}" prop="${field.camelCaseField}">
          <el-input v-model="${model.camelCaseField}.${field.camelCaseField}" style="width: 300px" />
        </el-form-item>
      </el-row>
    </#list>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
        <el-button @click="onCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { ${model.camelCaseField}Add, ${model.camelCaseField}Info, ${model.camelCaseField}Update } from '@/api/${model.camelCaseField}Api'

export default {
  data() {
    return {
      ${model.camelCaseField}: {
      <#list model.fieldList as field>
        <#if !field.primaryKey>
        ${field.camelCaseField}: ''<#if (field_index < model.fieldList?size - 1)>,</#if>
        </#if>
      </#list>
      },
      dataStatusList: [
        { value: 2, label: '正常' },
        { value: 3, label: '删除' }
      ],
      ${model.camelCaseField}FormRules: {
      <#list model.fieldList as field>
        <#if !field.primaryKey>
        ${field.camelCaseField}: [{ required: true, trigger: 'blur', message: '请输入${field.comment}' }]<#if (field_index < model.fieldList?size - 1)>,</#if>
        </#if>
      </#list>
      }
    }
  },
  mounted() {
    const params = this.$route.params
    this.${model.camelCaseField}.id = params.id
    this.load${model.model}Info()
  },
  methods: {
    load${model.model}Info() {
      if (this.${model.camelCaseField}.id > 0) {
        ${model.camelCaseField}Info(this.${model.camelCaseField}.id).then(res => {
          this.${model.camelCaseField} = res.data
        }).catch(err => {
          this.$message.error('Error: ' + err.message)
        })
      }
    },
    onSubmit() {
      this.$refs.${model.camelCaseField}Form.validate((valid) => {
        if (valid) {
          const ${model.camelCaseField}Action = this.${model.camelCaseField}.id > 0 ? ${model.camelCaseField}Update : ${model.camelCaseField}Add
          ${model.camelCaseField}Action(this.${model.camelCaseField}).then(res => {
            this.$message.success('保存成功')
            this.$router.push({ path: '/${model.camelCaseField}/list', query: this.$route.query })
          }).catch(err => {
            this.$message.error('Error: ' + err.message)
          })
        } else {
          return false
        }
      })
    },
    onCancel() {
      this.$router.push({ path: '/${model.camelCaseField}/list', query: this.$route.query })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>

