package com.chinasoft.goldidea.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * for hc
 * @author Mango
 *
 */
@Slf4j
@RestController
@Api("swaggerDemoController相关的api")
public class IndexController {

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "It works";
    }

    @ApiOperation(value = "根据code返回code用来测试",notes = "返回code")
    @ApiImplicitParam(name="code", value="传入的code",paramType = "param",required = true,dataType = "String")
    @GetMapping("/getcode")
    public String getCode(@RequestParam String code){
        log.info("The code is ",code);
        return code;
    }
}
