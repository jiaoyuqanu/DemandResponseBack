package com.xqxy.sys.modular.dict.result;

import com.xqxy.core.pojo.base.node.BaseTreeNode;
import lombok.Data;

import java.util.List;

/**
 * 系统字典树
 *
 * @author xuyuxiang
 * @date 2020/3/11 12:08
 */
@Data
public class DictTreeNode implements BaseTreeNode {

    /**
     * id
     */
    private Long id;

    /**
     * 父id
     */
    private Long pid;

    /**
     * 编码-对应字典值的编码
     */
    private String code;

    /**
     * 名称-对应字典值的value
     */
    private String name;

    /**
     * 名称-对应字典值的value
     */
    private String value;

    /**
     * 名称-对应字典值的value
     */
    private String parentId;

    /**
     * 子节点集合
     */
    private List<DictTreeNode> children;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Long getPid() {
        return this.pid;
    }

    @Override
    public String getParentId() {
        return this.parentId;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
