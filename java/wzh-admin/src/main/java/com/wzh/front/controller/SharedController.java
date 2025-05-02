package com.wzh.front.controller;

import com.wzh.common.core.controller.BaseController;
import com.wzh.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wzh
 * 前台分享功能
 * @date 2020/1/14 - 14:01
 */
@RestController
@RequestMapping("/shared")
public class SharedController extends BaseController {

    @GetMapping("/code/{code}")
    public AjaxResult getCode(@PathVariable("code") String code){
        return success();
    }
}
