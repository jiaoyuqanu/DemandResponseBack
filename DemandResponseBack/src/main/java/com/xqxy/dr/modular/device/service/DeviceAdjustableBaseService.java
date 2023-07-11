package com.xqxy.dr.modular.device.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xqxy.core.pojo.response.ResponseData;
import com.xqxy.dr.modular.device.VO.DeviceAdjustableBaseVO;
import com.xqxy.dr.modular.device.entity.DeviceAdjustableBase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.device.entity.TDevBranch;
import com.xqxy.dr.modular.device.param.DeviceAdjustableBaseParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 设备可调节基础信息 服务类
 * </p>
 *
 * @author hu xingxing
 * @since 2021-11-08
 */
public interface DeviceAdjustableBaseService extends IService<DeviceAdjustableBase> {

    /**
     * 设备生成
     * @param deviceAdjustableBaseParam
     * @return
     * @author hu xingxing
     * @date 2021-11-08 10:00
     */
    ResponseData generateDevice(DeviceAdjustableBaseParam deviceAdjustableBaseParam);

    /**
     * 设备生成
     * @param
     * @return
     * @author shi
     * @date 2021-12-02 10:00
     */
    List<DeviceAdjustableBase> getByConsId(String consId);

    /**
     * 设备ID是否重复
     * @param
     * @return
     * @author shi
     * @date 2021-12-02 10:00
     */
    boolean isDeviceIdRepeat(String deviceId);

    /**
     * 删除设备基础档案
     * @param
     * @return
     * @author shi
     * @date 2021-12-02 10:00
     */
    int delDevice(Long id);

    /**
     * 电力专责用户 列表
     * @param deviceAdjustableBaseParam
     * @return
     * @author hu xingxing
     * @date 2021-11-08 10:20
     */
    Page<DeviceAdjustableBaseVO> page(DeviceAdjustableBaseParam deviceAdjustableBaseParam);


    /**
     * 设备下拉框查询
     * @param deviceAdjustableBase
     * @return
     * @author hu xingxing
     * @date 2021-11-08 10:20
     */
    List<DeviceAdjustableBase>  listDeviceBase(DeviceAdjustableBase deviceAdjustableBase);


    /**
     *设备基础档案管理
     * @param deviceAdjustableBaseParam
     * @return
     * @author lqr
     * @date 2021-11-08 10:20
     */
    ResponseData pageDeviceBase(DeviceAdjustableBaseParam deviceAdjustableBaseParam);


    /**
     *设备基础档案修改(电力用户)
     * @param deviceAdjustableBase
     * @return
     * @author czj
     * @date 2022-1-5 15:20
     */
    void update(DeviceAdjustableBase deviceAdjustableBase);


    /**
     *设备基础档案删除(电力用户)
     * @param deviceAdjustableBase
     * @return
     * @author czj
     * @date 2022-1-5 15:20
     */
    void delete (DeviceAdjustableBase deviceAdjustableBase);

    /**
     *设备基础档案新增(电力用户)
     * @param deviceAdjustableBase
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    ResponseData add(DeviceAdjustableBase deviceAdjustableBase);


    /**
     *设备基础档案详情(电力用户)
     * @param id
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    DeviceAdjustableBaseVO detailDeviceBase(Long id);


    /**
     *批量提交 导入 (电力用户)
     * @param
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    ResponseData importDeviceModel(MultipartFile file);


    /**
     *批量删除 (电力用户)
     * @param
     * @return
     * @author lqr
     * @date 2022-1-5 15:20
     */
    void deleteBatchDevice(List<Long> ids);

    /**
     *  传入cons_id 查询 支路名称和两侧单元标识
     */
    JSONArray selectBranchByConsId(String consId);
}
