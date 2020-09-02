package cheng.Community.service;


import cheng.Community.dataobject.LoginTicketMapper;
import cheng.Community.dataobject.UserMapper;
import cheng.Community.entity.LoginTicket;
import cheng.Community.entity.User;
import cheng.Community.util.CommunityConstant;
import cheng.Community.util.CommunityUtil;
import cheng.Community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@Service

public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    //——————2.7 开发注册功能——————
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public Map<String,Object> register(User user){

        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!!!");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空!!!");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!!!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!!!");
            return map;
        }

        // 账号是否存在？
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在!!!");
            return map;
        }
        // 邮箱是否存在？
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册!!!");
            return map;
        }

        // 注册用户，存储用户信息
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5)); //生成salt随机字符串
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt())); //密码加上salt
        user.setType(0);
        user.setStatus(0);
        user.setActiationCode(CommunityUtil.generateUUID()); //激活码
        user.setHeaderUrl( String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000))); //牛客网随机头像
        user.setCreateTime(new Date()); //创建时间
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/cheng/activation/(userid)/(激活码)
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActiationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content); // subject:主题   content:内容

        return map;
    }

    // 处理激活
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT; //重复激活
        } else if (user.getActiationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS; //激活成功
        } else {
            return ACTIVATION_FAILURE; //激活失败
        }
    }


    //——————2.23 开发登录和退出功能—————
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    //设置成map返回多种情况的登陆结果
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }
    // 退出登录
    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }


    // ————2.26 课后作业，完成忘记密码功能——————
    public Map<String,Object> fogetSendKaptcha(String email,String kaptcha){

        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("邮箱不能为空!!!");
        }

        // 邮箱是否存在？
        User user = userMapper.selectByEmail(email);
        if (user == null) {
            map.put("emailMsg", "该邮箱未注册!!!");
            return map;
        }

        // 发送验证码邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        context.setVariable("kaptchaforget", kaptcha);
        String content = templateEngine.process("/mail/forget", context);
        mailClient.sendMail(user.getEmail(),"验证码",content);

        return map;
    }

    public Map<String,Object> change(String email,String password,HttpSession session){

        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("邮箱不能为空!!!");
        }

        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("密码不能为空!!!");
        }

        // 邮箱是否存在？
        User u = userMapper.selectByEmail(email);
        if (u == null) {
            map.put("emailMsg", "该邮箱未注册!!!");
            return map;
        }

        userMapper.updatePassword(u.getId(),password);

        return map;
    }

    // ————2.27 显示登陆信息——————
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    // ————2.33 账号设置——————
    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
    }





}
