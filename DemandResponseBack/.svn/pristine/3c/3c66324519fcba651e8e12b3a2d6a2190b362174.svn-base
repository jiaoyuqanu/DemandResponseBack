package com.xqxy.dr.modular.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.notice.entity.NoticeFile;
import com.xqxy.dr.modular.notice.param.NoticeFileParam;

import java.util.List;

/**
 * <p>
 * 公示附件 服务类
 * </p>
 *
 * @author xiao jun
 * @since 2021-04-27
 */
public interface NoticeFileService extends IService<NoticeFile> {

    /**
     * 公示附件列表
     *
     * @param noticeFileParam 查询参数
     * @return 公示附件列表
     * @author xiao jun
     * @date 2021-03-11 15:49
     */
    List<NoticeFile> list(NoticeFileParam noticeFileParam);

    /**
     * 公示附件列表
     *
     * @param noticeId 查询参数
     * @return 公示附件列表
     * @author xiao jun
     * @date 2021-03-11 15:49
     */
    List<NoticeFile> listByNoticeId(Long noticeId);

    /**
     * 删除公示附件
     *
     * @param id 查询参数
     * @return 公示附件列表
     * @author xiao jun
     * @date 2021-03-11 15:49
     */
    void delete(Long id);

}
