package cheng.Community.dataobject;

import org.springframework.stereotype.Repository;

@Repository("Hibernate") //访问数据库的注解
public class TestDataHibernate implements TestData{
    @Override
    public String select(){
        return "HibernateCheng";
    }
}
