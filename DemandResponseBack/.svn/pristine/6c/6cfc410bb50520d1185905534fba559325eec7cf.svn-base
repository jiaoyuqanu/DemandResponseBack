package com.xqxy.dr.modular.notice.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.annotion.NeedSetValueField;
import com.xqxy.dr.modular.notice.entity.NoticeFile;
import com.xqxy.dr.modular.notice.mapper.NoticeFileMapper;
import com.xqxy.dr.modular.notice.param.NoticeFileParam;
import com.xqxy.dr.modular.notice.service.NoticeFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 公示附件 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-04-27
 */
@Service
public class NoticeFileServiceImpl extends ServiceImpl<NoticeFileMapper, NoticeFile> implements NoticeFileService {

    @Resource
    NoticeFileMapper noticeFileMapper;

    @Override
    @NeedSetValueField
    public List<NoticeFile> list(NoticeFileParam noticeFileParam) {
        LambdaQueryWrapper<NoticeFile> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(noticeFileParam)) {
            // 根据用户编号查询
            if (ObjectUtil.isNotEmpty(noticeFileParam.getNoticeId())) {
                queryWrapper.eq(NoticeFile::getNoticeId, noticeFileParam.getNoticeId());
            }

        }

        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(NoticeFile::getCreateTime);
        return this.list(queryWrapper);
    }


    @NeedSetValueField
    public List<NoticeFile> listByNoticeId(Long noticeId) {
//        NoticeFileParam noticeFileParam = new NoticeFileParam();
//        noticeFileParam.setNoticeId(noticeId);
//        return this.list(noticeFileParam);
        List<NoticeFile> noticeFiles = noticeFileMapper.listByNoticeId(noticeId);
        return noticeFiles;
    }

    @Override
    public void delete(Long id) {
       this.removeById(id);
    }
}
