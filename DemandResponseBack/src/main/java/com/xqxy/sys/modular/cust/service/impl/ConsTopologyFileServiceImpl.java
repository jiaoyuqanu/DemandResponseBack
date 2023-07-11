package com.xqxy.sys.modular.cust.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.sys.modular.cust.entity.ConsTopologyFile;
import com.xqxy.sys.modular.cust.mapper.ConsTopologyFileMapper;
import com.xqxy.sys.modular.cust.service.ConsTopologyFileService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 用户拓扑图表 服务实现类
 * </p>
 *
 * @author liqirui
 * @since 2022-05-05
 */
@Service
public class ConsTopologyFileServiceImpl extends ServiceImpl<ConsTopologyFileMapper, ConsTopologyFile> implements ConsTopologyFileService {


    /**
     * 批量新增 用户拓扑图
     * @author lqr
     * @return
     */
    @Override
    public ResponseData insertBatch(List<ConsTopologyFile> consTopoologyFiles) {

        //删除 用户 对应的库里数据
        LambdaQueryWrapper<ConsTopologyFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ConsTopologyFile::getConsId,consTopoologyFiles.get(0).getConsId());
        this.remove(lambdaQueryWrapper);
        lambdaQueryWrapper.clear();

        for (ConsTopologyFile consTopoologyFile : consTopoologyFiles) {
            Long fileId = consTopoologyFile.getFileId();
            lambdaQueryWrapper.eq(ConsTopologyFile::getFileId,fileId);
            List<ConsTopologyFile> list = this.list(lambdaQueryWrapper);
            if(!CollectionUtils.isEmpty(list)){
                return ResponseData.fail("500","数据的 文件id 重复 ","");
            }
        }
        this.saveBatch(consTopoologyFiles);

        return ResponseData.success();
    }



    /**
     * 根据主键删除 用户拓扑图
     * @author lqr
     * @return
     */
    @Override
    public void deleteByFileId(Long id) {

        LambdaQueryWrapper<ConsTopologyFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConsTopologyFile::getFileId,id);
        this.remove(queryWrapper);
    }
}
