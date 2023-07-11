package com.xqxy.core.util;

import cn.hutool.core.util.ObjectUtil;
import com.xqxy.core.client.SystemClient;
import com.xqxy.core.client.SystemClientService;
import com.xqxy.core.pojo.login.CurrenUserInfo;
import com.xqxy.core.pojo.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 查询组织机构编码
 * </p>
 *
 * @author Caoj
 * @date 2022-02-18 14:45
 */
@Component
public class OrganizationUtil implements ApplicationRunner {

    @Resource
    private SystemClientService systemClientService;

    public static OrganizationUtil organizationUtil;

    @PostConstruct
    public void init() {
        organizationUtil = this;
        organizationUtil.systemClientService = this.systemClientService;
    }

    public static List<String> getAllOrgByOrgNo() {
        CurrenUserInfo currentUserInfo = SecurityUtils.getCurrentUserInfoUTF8();
        List<String> orgIds = new ArrayList<>();
        if (ObjectUtil.isNotNull(currentUserInfo) && ObjectUtil.isNotNull(currentUserInfo.getOrgId())) {
            String orgId = currentUserInfo.getOrgId();
            ResponseData<List<String>> allNextOrgId = organizationUtil.systemClientService.getAllNextOrgId(orgId);
            if (ObjectUtil.isNotNull(allNextOrgId.getData())) {
                orgIds = allNextOrgId.getData();
            }
        }
        if (ObjectUtil.isEmpty(orgIds)) {
            orgIds.add("-1");
        }
        return orgIds;
    }

    public static List<String> getAllOrgByOrgNoPamarm(String orgId) {
        List<String> orgIds = new ArrayList<>();
        if (ObjectUtil.isNotNull(orgId)) {
            ResponseData<List<String>> allNextOrgId = organizationUtil.systemClientService.getAllNextOrgId(orgId);
            if (ObjectUtil.isNotNull(allNextOrgId.getData())) {
                orgIds = allNextOrgId.getData();
            }
        }
        if (ObjectUtil.isEmpty(orgIds)) {
            orgIds.add("-1");
        }
        return orgIds;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
