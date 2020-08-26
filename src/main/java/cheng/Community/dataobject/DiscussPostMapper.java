package cheng.Community.dataobject;


import cheng.Community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper

//——————1.30 开发社区首页——————
public interface DiscussPostMapper {

    // offset:起始行行号, limit：每页多少数据
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId); //查询帖子行数

}
