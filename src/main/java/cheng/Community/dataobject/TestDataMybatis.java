package cheng.Community.dataobject;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary  //优先
public class TestDataMybatis implements TestData {
    @Override
    public String select(){
        return "I am MybatisCheng ——20200823";
    }
}
