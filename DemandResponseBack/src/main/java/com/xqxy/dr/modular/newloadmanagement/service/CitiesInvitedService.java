package com.xqxy.dr.modular.newloadmanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xqxy.dr.modular.event.param.ConsInvitationParam;

import java.util.List;
import java.util.Map;

public interface CitiesInvitedService  {


    Page queryCitiesInvited(ConsInvitationParam consInvitationParam);


    List queryDrConsInvitation(ConsInvitationParam consInvitationParam);
}
