package cheng.Community;

import cheng.Community.dataobject.DiscussPostMapper;
import cheng.Community.dataobject.TestData;
import cheng.Community.dataobject.UserMapper;
import cheng.Community.entity.DiscussPost;
import cheng.Community.entity.User;
import cheng.Community.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)

public class MapperTests {

    // ——————1.23 MyBatis入门——————
    @Autowired
    private UserMapper usermapper;


    @Test
    public void testSelectUser(){
        User user = usermapper.selectById(101);
        System.out.println(user);

        user = usermapper.selectByName("liubei");
        System.out.println(user);

        user = usermapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123123123");
        user.setSalt("test");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        int rows = usermapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user);
    }

    @Test
    public void testUpdateUser(){
        int rows = usermapper.updateStatus(150,1);
        System.out.println(rows);
        rows = usermapper.updateHeader(150,"http://www.aaa.com/png");
        System.out.println(rows);
        rows = usermapper.updatePassword(150,"benben");
        System.out.println(rows);
    }


    //——————1.30 开发社区首页——————
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectPosts(){
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149,0,10);
        for(DiscussPost post : list) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }

}
