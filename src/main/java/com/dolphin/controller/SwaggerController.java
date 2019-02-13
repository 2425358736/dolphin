package com.dolphin.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * SwaggerController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/13
 */
@RestController
@RequestMapping("/swagger")
@CrossOrigin
@Api(description = "swagger测试api")
public class SwaggerController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("index")
    @ApiOperation(value = "swagger测试index", notes = "参数如下：{\"userName\": \"刘志强\",\"password\": \"123\"}")
    public User index(@RequestBody  User user){
        return user;
    }

    @PostMapping("index2")
    @ApiOperation(value = "swagger测试index2")
    public User index(@ApiParam(name="id",value="用户id",required=true) @RequestParam Long id){
        logger.info("id参数：" + id);
        User user = new User();
        user.setUserName("刘志强");
        user.setPassword("123456");
        return user;
    }

    @PostMapping("/index3")
    @ApiOperation(value = "swagger测试index3")
    @ApiImplicitParam(name="id",value="用户id",dataType="Long", paramType = "query")
    public User index3(Long id){
        User user = index(id);
        return user;
    }

    @PostMapping("/index4")
    @ApiOperation(value = "swagger测试index4")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",value="用户名",dataType="String", paramType = "query",defaultValue="刘志强"),
            @ApiImplicitParam(name="password",value="密码",dataType="String", paramType = "query",defaultValue="123456")})
    public User index4(String userName, String password){
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        return user;
    }
}
@ApiModel
class User{
    @ApiModelProperty(value = "用户名", name = "userName")
    private String userName;
    @ApiModelProperty(value = "密码", name = "password")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}