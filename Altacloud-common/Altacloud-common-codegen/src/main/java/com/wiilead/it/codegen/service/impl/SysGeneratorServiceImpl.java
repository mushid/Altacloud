package com.wiilead.it.codegen.service.impl;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wiilead.it.codegen.entity.GenConfig;
import com.wiilead.it.codegen.mapper.SysGeneratorMapper;
import com.wiilead.it.codegen.service.SysGeneratorService;
import com.wiilead.it.codegen.utils.GenUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName: SysGeneratorServiceImpl
 * @Description: TODO
 * @Author mushishi
 * @Date 2019/4/19-14:33
 */
@Service
@AllArgsConstructor
public class SysGeneratorServiceImpl implements SysGeneratorService {


    private final SysGeneratorMapper sysGeneratorMapper;


    /**
     * 分页查询表
     *
     * @param tableName 查询条件
     * @return
     */
    @Override
    public IPage<List<Map<String, Object>>> queryPage(Page page, String tableName) {
        return sysGeneratorMapper.queryList(page, tableName);
    }

    /**
     * 生成代码
     *
     * @param genConfig 生成配置
     * @return
     */
    @Override
    public byte[] generatorCode(GenConfig genConfig) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        //查询表信息
        Map<String, String> table = queryTable(genConfig.getTableName());
        //查询列信息
        List<Map<String, String>> columns = queryColumns(genConfig.getTableName());
        //生成代码
        GenUtils.generatorCode(genConfig, table, columns, zip);
        IoUtil.close(zip);
        return outputStream.toByteArray();
    }

    private Map<String, String> queryTable(String tableName) {
        return sysGeneratorMapper.queryTable(tableName);
    }

    private List<Map<String, String>> queryColumns(String tableName) {
        return sysGeneratorMapper.queryColumns(tableName);
    }
}
