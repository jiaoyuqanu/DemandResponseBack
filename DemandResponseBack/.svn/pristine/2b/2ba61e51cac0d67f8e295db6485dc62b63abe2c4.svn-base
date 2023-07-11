package com.xqxy.dr.modular.newloadmanagement.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author Rabbit
 * @Date 2022/6/10 20:43
 */
@ApiModel(description = "分页格式")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedFormat<T> {
    @ApiModelProperty(value = "总页数")
    private Integer totalPage;
    @ApiModelProperty(value = "总记录数")
    private Integer totalCount;
    @ApiModelProperty(value = "当前页数")
    private Integer currPage;
    @ApiModelProperty(value = "每页记录数")
    private Integer pageSize;
    @ApiModelProperty(value = "数据")
    private T list;

    public PagedFormat(long total, long current, long size, T userListVos) {
        this.totalPage = (int) (total / size + (total % size == 0 ? 0 : 1));
        this.totalCount = (int) total;
        this.currPage = (int) current;
        this.pageSize = (int) size;
        this.list = userListVos;
    }
}
