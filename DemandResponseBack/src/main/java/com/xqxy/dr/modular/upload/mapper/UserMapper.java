package com.xqxy.dr.modular.upload.mapper;


import com.xqxy.dr.modular.upload.entity.Drcons;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 需求响应用户 Mapper 接口  & 用户年度响应能力
 * </p>
 *
 */

@Mapper
public interface UserMapper {
    List<Drcons> getUser();


    List<Drcons> getAbility();
}
