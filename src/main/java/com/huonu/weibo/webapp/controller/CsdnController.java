package com.huonu.weibo.webapp.controller;

import com.huonu.weibo.webapp.service.ISysService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "csdn相关")
public class CsdnController {
    @Autowired
    private ISysService sysService;

    @ApiOperation("刷csdn访问量")
    @GetMapping("/csdn/handle")
    public Boolean handle(@ApiParam("是否循环") @RequestParam(defaultValue = "true") boolean isCycle) {
        sysService.handleCsdn(isCycle);
        return true;
    }
}
