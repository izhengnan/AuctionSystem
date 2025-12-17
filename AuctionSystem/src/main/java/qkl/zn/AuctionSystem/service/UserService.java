package qkl.zn.AuctionSystem.service;

import qkl.zn.AuctionSystem.pojo.dto.UserDTO;
import qkl.zn.AuctionSystem.pojo.entity.User;

public interface UserService {
    void userRegister(UserDTO userDTO);

    User userLogin(UserDTO userDTO);
    
    User adminLogin(UserDTO userDTO);
}
