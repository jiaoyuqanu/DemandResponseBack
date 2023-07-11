package com.xqxy.core.client;

import com.alibaba.fastjson.JSONObject;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.project.result.Region;
import com.xqxy.sys.modular.cust.param.BusConfigParam;
import com.xqxy.sys.modular.cust.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="znyd-system-service")
@Component
public interface SystemClient {
    /**
     *  param是否配置审批流程
     * @param param
     * @return
     */
    @PostMapping(value="/busConfig/selectInfo_v2/{param}")
    public Result selectInfo(@RequestBody BusConfigParam param);

    @GetMapping(value="/group/getOrg/{id}")
    public JSONObject getOrgName(@PathVariable String id);

    @GetMapping(value="/region/queryAll")
    public List<Region> queryAll();

    @GetMapping(value="/group/getAllOrgs")
    public Result getAllOrgs();

    @GetMapping(value="/group/queryAllOrg")
    public JSONObject queryAllOrg();

    /**
     * 根据待办标识撤销客户签约
     *
     * @param busId 待办标识
     * @return com.xqxy.core.pojo.response.ResponseData
     * @date 12/7/2021 20:12
     * @author Caoj
     */
    @GetMapping(value = "/busTask/deleteByBusId/{busId}")
    ResponseData<String> deleteBus(@PathVariable String busId);

    @GetMapping(value = "/group/getNextOrgs/{id}")
    ResponseData<String> getNextOrgs(@PathVariable String id);

    /**
     * 根据组织机构编码获取所有子集组织机构编码
     *
     * @param orgId 组织机构编码
     * @return com.xqxy.core.pojo.response.ResponseData<java.lang.String>
     * @date 2022/2/18 10:16
     * @author Caoj
     */
    @GetMapping(value = "/group/getAllNextOrgId/{orgId}")
    ResponseData<List<String>> getAllNextOrgId(@PathVariable String orgId);

    /**
     * 根据组织id查询下级组织机构列表
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/group/parent/{id}")
    JSONObject parent(@PathVariable String id);

    @GetMapping(value = "/user")
    JSONObject getUserInfo(@RequestParam String uniqueId);

    @PostMapping("/user")
    JSONObject addUser(@RequestBody JSONObject jsonObject);
}
