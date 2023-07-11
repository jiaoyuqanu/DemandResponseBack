
package com.xqxy.core.factory;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xqxy.core.pojo.base.node.BaseTreeNode;
import com.xqxy.sys.modular.dict.entity.DictData;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认递归工具类，用于遍历有父子关系的节点，例如菜单树，字典树等等
 *
 * @author xuyuxiang
 * @date 2020/4/5 14:17
 */
@Data
public class TreeBuildFactory2<T extends DictData> {

    /**
     * 顶级节点的父节点id(默认0)
     */
    private String rootParentId = "1";

    /**
     * 树节点构造
     *
     * @author xuyuxiang
     * @date 2020/4/5 14:09
     */
    public List<T> doTreeBuild(List<T> nodes) {

        //具体构建的过程
        List<T> buildComplete = this.executeBuilding(nodes);

        //构建之后的处理工作
        return this.afterBuild(buildComplete);
    }

    /**
     * 查询子节点集合
     *
     * @author xuyuxiang
     * @date 2020/4/5 14:10
     */
    public void buildChildNodes2(List<T> totalNodes, T DictData, List<T> childNodeLists) {
        if (ObjectUtil.hasEmpty(totalNodes, DictData)) {
            return;
        }
        List<T> nodeSubLists = this.getSubChildLevelOne2(totalNodes, DictData);
        if (ObjectUtil.isNotEmpty(nodeSubLists)) {
            nodeSubLists.forEach(t -> this.buildChildNodes2(totalNodes, t, CollectionUtil.newArrayList()));
        }
        childNodeLists.addAll(nodeSubLists);
        DictData.setChildren(childNodeLists);
    }

    /**
     * 获取子一级节点的集合
     *
     * @author xuyuxiang
     * @date 2020/4/5 14:12
     */
    public List<T> getSubChildLevelOne2(List<T> list, T DictData) {
        List<T> nodeList = CollectionUtil.newArrayList();
        if (ObjectUtil.isNotEmpty(list)) {
            list.forEach(t -> {
                if (t.getParentId().equals(DictData.getCode())) {
                    nodeList.add(t);
                }
            });
        }
        return nodeList;
    }

    /**
     * 执行构造
     *
     * @author xuyuxiang
     * @date 2020/4/5 14:13
     */
    private List<T> executeBuilding(List<T> nodes) {
        nodes.forEach(t -> this.buildChildNodes2(nodes, t, CollectionUtil.newArrayList()));
        return nodes;
    }

    /**
     * 构造之后
     *
     * @author xuyuxiang
     * @date 2020/4/5 14:13
     */
    private List<T> afterBuild(List<T> nodes) {
        //去掉所有的二级节点
        ArrayList<T> results = CollectionUtil.newArrayList();
        nodes.forEach(t -> {
            if (rootParentId.equals(t.getLevel())) {
                results.add(t);
            }
        });
        return results;
    }
}