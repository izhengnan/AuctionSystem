package qkl.zn.AuctionSystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qkl.zn.AuctionSystem.pojo.dto.UserDTO;
import qkl.zn.AuctionSystem.pojo.entity.User;
import qkl.zn.AuctionSystem.result.Result;
import qkl.zn.AuctionSystem.service.UserService;
import qkl.zn.AuctionSystem.utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result userRegister(UserDTO userDTO) {
        log.info("注册:{}", userDTO);
        userService.userRegister(userDTO);
        return Result.success();
    }

    @PostMapping("/login")
    public Result userLogin(UserDTO userDTO) {
        log.info("登录:{}", userDTO);
        User user = userService.userLogin(userDTO);
        
        // 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        String token = JwtUtils.generateJwt(claims);
        
        // 创建返回数据对象，包含用户ID令牌
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", user.getId());
        responseData.put("token", token);
        
        return Result.success(responseData);
    }
}