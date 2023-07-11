package com.xqxy.dr.modular.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.notice.entity.Notice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 公示表 Mapper 接口
 * </p>
 *
 * @author xiao jun
 * @since 2021-04-27
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    @Select("select count(1) from dr_notice where (type = #{code} and public_time is not null) and date_format(public_time,'%Y') = #{overYear}")
    Integer countByTypeAndYear(@Param("code")Integer code,@Param("overYear")  String overYear);

    @Select("select * from dr_notice where (type = #{code} and public_time is not null) and date_format(public_time,'%Y') = #{year} order by public_time asc limit #{index},1")
    Notice getNoticeByTime(@Param("code")Integer code,@Param("year") String year,@Param("index") int index);
}
