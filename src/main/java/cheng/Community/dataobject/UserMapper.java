package cheng.Community.dataobject;

import cheng.Community.entity.User;
import org.apache.ibatis.annotations.Mapper;

// ——————1.23 MyBatis入门——————

@Mapper
// 对User表的操作 resource/mapper/user-mapper.xml
public interface UserMapper {

    User selectById(int id);
    User selectByName(String username);
    User selectByEmail(String email);

    int insertUser(User user);
    int updateStatus(int id, int status);
    int updateHeader(int id, String headerUrl);
    int updatePassword(int id, String password);


}
