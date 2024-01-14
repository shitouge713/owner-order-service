package com.owner.order.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.owner.order.dao.pojo.UserInfo;
import com.owner.order.model.request.DeleteEsDataReq;
import com.owner.order.service.IEsOrderGlobalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class EsOrderController {
    @Autowired
    private IEsOrderGlobalService esOrderGlobalService;


    @PostMapping("/es/syncGlobalOrder/applyCode")
    public Boolean syncGlobalOrderRecord(@RequestBody @Valid UserInfo vo) {
        return esOrderGlobalService.syncToEsByLinkData(vo);
    }

    @GetMapping("/es/syncGlobalOrder/deleteById")
    public void deleteById(@Valid DeleteEsDataReq req) {
        esOrderGlobalService.deleteById(req.getId());
    }


}
