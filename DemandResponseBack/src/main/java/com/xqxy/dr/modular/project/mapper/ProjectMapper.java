package com.xqxy.dr.modular.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.dr.modular.project.entity.Project;

/**
 * <p>
 * 需求响应项目 Mapper 接口
 * </p>
 *
 * @author PengChuqing
 * @since 2021-10-11
 */
public interface ProjectMapper extends BaseMapper<Project> {


    /**
     * @param consId
     * @return java.util.List<com.xqxy.dr.modular.project.entity.ProjectDetail>
     * @description 根据用户标识查询项目详情
     * @author Caoj
     * @since 10/13/2021 15:56
     */
    Page<Project> listProjectByConsId(Page<?> page, String consId);

    /**
     * 获取最近项目id
     *
     * @return id
     */
    String recentProjects();
}
