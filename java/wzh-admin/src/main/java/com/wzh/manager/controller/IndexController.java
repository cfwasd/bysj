package com.wzh.manager.controller;

import com.wzh.common.core.controller.BaseController;
import com.wzh.common.core.domain.AjaxResult;
import com.wzh.manager.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/index")
@RestController
public class IndexController extends BaseController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/info")
    public AjaxResult info(){
        return indexService.getTotal();
    }
}
