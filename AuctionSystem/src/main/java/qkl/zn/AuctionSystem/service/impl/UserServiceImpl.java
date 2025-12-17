package qkl.zn.AuctionSystem.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qkl.zn.AuctionSystem.mapper.UserMapper;
import qkl.zn.AuctionSystem.pojo.dto.UserDTO;
import qkl.zn.AuctionSystem.pojo.entity.User;
import qkl.zn.AuctionSystem.service.UserService;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void userRegister(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        user.setRole(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.userRegister(user);
    }

    @Override
    public User userLogin(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        User user = userMapper.userLogin(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        return user;
    }
    
    @Override
    public User adminLogin(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        User user = userMapper.userLogin(username);
        if (user == null) {
            throw new RuntimeException("管理员不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        if (user.getRole() != 0) {
            throw new RuntimeException("该用户不是管理员");
        }
        return user;
    }
}
