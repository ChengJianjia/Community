package cheng.Community.service;

import cheng.Community.dataobject.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
// @Scope("prototype") //每次访问一个新的实例
public class TestService {

    @Autowired
    private TestData testData;


    public TestService(){
        System.out.println("实例化TestService");
    }

    @PostConstruct //实例化之后调用
    public void init(){
        System.out.println("初始化TestService");
    }

    @PreDestroy //销毁之前调用
    public void destroy() {
        System.out.println("销毁TestService");
    }

    public String find() {
        return testData.select(); //依赖TestData
    }
}
