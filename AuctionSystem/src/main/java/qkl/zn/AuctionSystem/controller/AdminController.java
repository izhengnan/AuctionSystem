package qkl.zn.AuctionSystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qkl.zn.AuctionSystem.pojo.dto.UserDTO;
import qkl.zn.AuctionSystem.pojo.entity.User;
import qkl.zn.AuctionSystem.result.Result;
import qkl.zn.AuctionSystem.service.UserService;
import qkl.zn.AuctionSystem.utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * 管理员登录
     * @param userDTO 用户信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result adminLogin(@RequestBody UserDTO userDTO) {
        log.info("管理员登录:{}", userDTO);
        
        // 设置角色为管理员(0)
        userDTO.setRole(0);
        User user = userService.adminLogin(userDTO);
        
        // 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRole()); // 添加角色信息到token中
        String token = JwtUtils.generateJwt(claims);
        
        // 创建返回数据对象，包含用户ID和令牌
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", user.getId());
        responseData.put("role", user.getRole());
        responseData.put("token", token);
        log.info("管理员登录成功，返回数据:{}", responseData);
        return Result.success(responseData);
    }
}