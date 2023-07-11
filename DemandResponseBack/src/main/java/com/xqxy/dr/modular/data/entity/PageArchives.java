package com.xqxy.dr.modular.data.entity;

import com.xqxy.dr.modular.data.result.ProfileObject;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * page class of archives entity
 * </p>
 *
 * @author Caoj
 * @date 2021-11-05 9:20
 */
@Data
public class PageArchives {
    private Integer pageNum;
    private Integer pages;
    private Integer total;
    private Integer pageSize;
    private List<ProfileObject> list;
}
