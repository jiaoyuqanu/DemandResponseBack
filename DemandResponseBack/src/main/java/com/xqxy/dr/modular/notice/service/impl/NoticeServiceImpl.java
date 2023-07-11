package com.xqxy.dr.modular.notice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.annotion.NeedSetValueField;
import com.xqxy.dr.modular.notice.entity.Notice;
import com.xqxy.dr.modular.notice.entity.NoticeFile;
import com.xqxy.dr.modular.notice.mapper.NoticeMapper;
import com.xqxy.dr.modular.notice.param.NoticeParam;
import com.xqxy.dr.modular.notice.service.NoticeFileService;
import com.xqxy.dr.modular.notice.service.NoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 公示表 服务实现类
 * </p>
 *
 * @author xiao jun
 * @since 2021-04-27
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Resource
    private NoticeFileService noticeFileService;

    @Resource
    private NoticeMapper noticeMapper;


    @Override
    @NeedSetValueField
    public Page<Notice> page(NoticeParam noticeParam) {
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        LocalDateTime startTime=null;
        LocalDateTime endTime=null;
        if(ObjectUtil.isNotEmpty(noticeParam.getStartTime()))
        {
            startTime=noticeParam.getStartTime().atStartOfDay();
            queryWrapper.ge(Notice::getPublicTime, startTime);
        }
        if(ObjectUtil.isNotEmpty(noticeParam.getEndTime()))
        {
            endTime=noticeParam.getEndTime().atTime(23,59,59);
            queryWrapper.le(Notice::getPublicTime, endTime);
        }

        if (ObjectUtil.isNotNull(noticeParam)) {
            // 根据公式标题
            if (ObjectUtil.isNotEmpty(noticeParam.getTitle())) {
                queryWrapper.like(Notice::getTitle, noticeParam.getTitle());
            }
            // 根据公式类型
            if (ObjectUtil.isNotEmpty(noticeParam.getType())) {
                queryWrapper.eq(Notice::getType, noticeParam.getType());
            }
            if (ObjectUtil.isNotEmpty(noticeParam.getStatus())) {
                queryWrapper.eq(Notice::getStatus, noticeParam.getStatus());
            }
            // 多公告类型查询
            if (ObjectUtil.isNotEmpty(noticeParam.getTypes())) {
                queryWrapper.in(Notice::getType, noticeParam.getTypes());
            }

        }
        queryWrapper.ne(Notice::getStatus, 3);
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByDesc(Notice::getPublicTime);
        return this.page(noticeParam.getPage(), queryWrapper);
    }

    @Override
    public void add(NoticeParam noticeParam) {
        LocalDateTime publicTime=null;
        if(noticeParam.getStatus().toString().equals("1"))
        {
            publicTime=LocalDateTime.now();
        }
        LocalDate publicDate=null;
        LocalDate deadlineDate=null;
        if(ObjectUtil.isNotEmpty(noticeParam.getStartTime()))
        {
            publicDate=noticeParam.getStartTime();
        }
        if(ObjectUtil.isNotEmpty(noticeParam.getEndTime()))
        {
            deadlineDate=noticeParam.getEndTime();
        }
        Notice notice = new Notice();
        if (ObjectUtil.isNotNull(noticeParam.getId())) {
            notice = this.queryNotice(noticeParam);
            BeanUtil.copyProperties(noticeParam, notice);
            notice.setPublicTime(publicTime);
            notice.setPublicDate(publicDate);
            notice.setDeadlineDate(deadlineDate);
            this.updateById(notice);
            if(!noticeParam.getFileIds().equals("")){
                String[] fileIds=noticeParam.getFileIds().split(",");
                for (String fileId : fileIds) {
                    NoticeFile noticeFile=new NoticeFile();
                    noticeFile.setFileId(Long.valueOf(fileId));
                    noticeFile.setNoticeId(notice.getId());
                    noticeFileService.save(noticeFile);
                }
            }

            return;
        }
        BeanUtil.copyProperties(noticeParam, notice);
        notice.setPublicTime(publicTime);
        notice.setPublicDate(publicDate);
        notice.setDeadlineDate(deadlineDate);
        this.save(notice);
        Long noticeId=notice.getId();
        if(!noticeParam.getFileIds().equals("")) {
            String[] fileIds = noticeParam.getFileIds().split(",");
            for (String fileId : fileIds) {
                NoticeFile noticeFile = new NoticeFile();
                noticeFile.setFileId(Long.valueOf(fileId));
                noticeFile.setNoticeId(noticeId);
                noticeFileService.save(noticeFile);
            }
        }
    }

    @Override
    public void edit(NoticeParam noticeParam) {
        Notice notice = this.queryNotice(noticeParam);
        //对比是否存在状态修改
        if (notice.getStatus() != noticeParam.getStatus()) {
            if (noticeParam.getStatus() == 1) {
                noticeParam.setPublicTime(LocalDateTime.now());
            } else if (noticeParam.getStatus() == 2) {
                noticeParam.setCancelTime(LocalDateTime.now());
            }
        }
        BeanUtil.copyProperties(noticeParam, notice);
        this.updateById(notice);
    }

    @Override
    @NeedSetValueField
    public Notice detail(NoticeParam noticeParam) {
        return this.queryNotice(noticeParam);
    }

    @Override
    public Integer countByTypeAndYear(Integer code, String overYear) {
        return noticeMapper.countByTypeAndYear(code,overYear);
    }

    @Override
    public Notice getNoticeByTime(Integer code, String year, Integer times) {
        return noticeMapper.getNoticeByTime(code,year,times -1);
    }

    @Override
    public List<Notice> getByBusinessRela(String formatMonth,Integer noticeType) {
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notice::getBusinessRela,formatMonth);
        queryWrapper.eq(Notice::getType,noticeType);
        return this.list(queryWrapper);
    }

    /**
     * 获取用户档案
     *
     * @author xiao jun
     * @date 2020/3/26 9:56
     */
    private Notice queryNotice(NoticeParam noticeParam) {
        Notice notice = this.getById(noticeParam.getId());
        return notice;
    }
}
