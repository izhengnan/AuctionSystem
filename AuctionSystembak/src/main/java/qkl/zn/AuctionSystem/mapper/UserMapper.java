package qkl.zn.AuctionSystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import qkl.zn.AuctionSystem.pojo.entity.User;

@Mapper
public interface UserMapper {

    void userRegister(User user);

    User userLogin(String username);
}
