package com.xqxy.sys.modular.utils;

import com.alibaba.fastjson.JSONObject;
import com.xqxy.dr.modular.device.entity.SysOrgs;

public class JSONObjectToEntityUtils {

    public static SysOrgs JSONObjectToSysOrg(JSONObject fromObject) {
        SysOrgs sysOrgs = new SysOrgs();

        String id = fromObject.getString("id");
        String parentId = fromObject.getString("parentId");
        String name = fromObject.getString("name");
        String simpleName = fromObject.getString("simpleName");
        String regionId = fromObject.getString("regionId");
        String description = fromObject.getString("description");
        String orgTitle = fromObject.getString("orgTitle");
        String factId = fromObject.getString("factId");
        String picUrl = fromObject.getString("picUrl");
        String linkName = fromObject.getString("linkName");
        String linkPhone = fromObject.getString("linkPhone");
        String addresDesc = fromObject.getString("addresDesc");
        String longitude = fromObject.getString("longitude");
        String latitude = fromObject.getString("latitude");
        String manRegId = fromObject.getString("manRegId");
        String taxNo = fromObject.getString("taxNo");
        String bankNo = fromObject.getString("bankNo");

        sysOrgs.setId(id);
        sysOrgs.setParentId(parentId);
        sysOrgs.setName(name);
        sysOrgs.setSimpleName(simpleName);
        sysOrgs.setRegionId(regionId);
        sysOrgs.setDescription(description);
        sysOrgs.setOrgTitle(orgTitle);
        sysOrgs.setFactId(factId);
        sysOrgs.setPicUrl(picUrl);
        sysOrgs.setLinkName(linkName);
        sysOrgs.setLinkName(linkPhone);
        sysOrgs.setAddresDesc(addresDesc);
        sysOrgs.setLongitude(longitude);
        sysOrgs.setLatitude(latitude);
        sysOrgs.setManRegId(manRegId);
        sysOrgs.setTaxNo(taxNo);
        sysOrgs.setBankNo(bankNo);
        return sysOrgs;
    }

}
