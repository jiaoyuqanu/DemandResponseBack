package com.xqxy.dr.modular.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xqxy.dr.modular.notice.entity.NoticeFile;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 公示附件 Mapper 接口
 * </p>
 *
 * @author xiao jun
 * @since 2021-04-27
 */
public interface NoticeFileMapper extends BaseMapper<NoticeFile> {

    @Select("SELECT a.id,a.notice_id,a.file_id,b.file_origin_name FROM dr_notice_file a, dr_sys_file_info b WHERE a.file_id = b.file_id and notice_id = ${noticeId}")
    List<NoticeFile> listByNoticeId(Long noticeId);

}
