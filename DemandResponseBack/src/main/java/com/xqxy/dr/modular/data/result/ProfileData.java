package com.xqxy.dr.modular.data.result;

import lombok.Data;

import java.util.List;

@Data
public class ProfileData {

    private Integer totalNum;
    private Integer pageSize;
    private List<ProfileObject> rows;
    private Integer pageNum;

}
