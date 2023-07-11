package com.xqxy.sys.modular.cust.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.sys.modular.cust.entity.CustCertifyFile;

import java.util.List;

/**
 * 系统用户service接口
 *
 * @author shi
 * @date 2021/10/8 16:49
 */

public interface CustCertifyFileService extends IService<CustCertifyFile> {

    /**
     * 根据客户标识查找文件
     * @param CustId
     * @return
     */
    List<CustCertifyFile> getByCustId(Long CustId);

    void deleteBy(Long custId,String fileType);
}
