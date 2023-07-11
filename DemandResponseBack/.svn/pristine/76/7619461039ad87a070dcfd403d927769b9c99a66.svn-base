package com.xqxy.sys.modular.cust.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xqxy.core.pojo.base.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户认证材料信息表
 *
 * @author shi
 * @date 2021/10/8 10:25
 */
@Data
@TableName("dr_cust_certify_file")
public class CustCertifyFile extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 4948472632271997940L;

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;
  private Long custId;
  private Long fileId;
  private String thirdFileId;
  private String fileType;
  private String fileName;
}
