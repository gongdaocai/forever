package com.xcly.forever.controller;

import com.xcly.forever.common.result.Result;
import com.xcly.forever.service.QinagGouService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class QiangGouController {

    @Autowired
    private QinagGouService qinagGouService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "start")
    public Result start(@RequestParam(value = "count") Integer count, @RequestParam(value = "mid") String mid) {
        try {
//            LOGGER.info("=====>>>>>抢购接口请求");
            return Result.createSuccess("success", qinagGouService.start(mid, count));
        } catch (Exception e) {
            LOGGER.error("<<====抢购失败 reason:{}", e);
            return Result.createError();
        }
    }

    @PostMapping(value = "addQiangGou")
    public String addQiangGou() {
        try {
            return qinagGouService.addQiangGou();
        } catch (Exception e) {
            LOGGER.error("<<====添加抢购失败 reason:{}", e);
            return "添加抢购异常  ";
        }
    }
}
