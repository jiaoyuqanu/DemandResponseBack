package com.xqxy.dr.modular.gwapp.vo;

import com.xqxy.dr.modular.gwapp.entity.GwappCust;
import com.xqxy.sys.modular.cust.entity.CustCertifyFile;
import lombok.Data;

import java.util.List;

@Data
public class GwappCustInfoVo extends GwappCust {

    private List<CustCertifyFile> fileList;

}
