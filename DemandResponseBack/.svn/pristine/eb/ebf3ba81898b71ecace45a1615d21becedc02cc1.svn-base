
package com.xqxy.dr.modular.powerplant.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 通用基础参数，相关实体参数校验可继承此类
 *
 * @author chen zhi jun
 * @date 2020/3/10 16:02
 */
@ApiModel(description = "通用基础 参数")
@Data
public class Param implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分页查询的参数，当前页数
     */
    @TableField(exist = false)
    private long current = 1;

    /**
     * 分页查询的参数，当前页面每页显示的数量
     */
    @TableField(exist = false)
    private long size = 10;

    /**
     * 从form中获取page参数，用于分页查询参数
     *
     * @return
     */
    public Page getPage() {
        return new Page(this.getCurrent(), this.getSize());
    }
}
