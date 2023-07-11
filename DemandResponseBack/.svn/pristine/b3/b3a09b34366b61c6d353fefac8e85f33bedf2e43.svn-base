package com.xqxy.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;

/**
 * Author: zgy
 * Date: 2021/11/25 23:18
 * Content:
 */
public class NdsDialect implements IDialect {

    public NdsDialect() {
    }

    @Override
    public DialectModel buildPaginationSql(String originalSql, long offset, long limit) {
        String sql = originalSql + " LIMIT " + Long.toString(offset) + "," + Long.toString(limit);
        return (new DialectModel(sql, offset, limit));
    }
}
