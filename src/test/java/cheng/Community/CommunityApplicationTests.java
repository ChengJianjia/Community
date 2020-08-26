package cheng.Community;

import cheng.Community.dataobject.TestData;
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


@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

	@Test
	public void testAppliactionContext(){
		System.out.println(applicationContext);

		TestData testdata = applicationContext.getBean(TestData.class);
		System.out.println(testdata.select());

		testdata = applicationContext.getBean("Hibernate",TestData.class);
		System.out.println(testdata.select());
	}

	@Test
	public void testBeanManagement(){
		TestService testservice  = applicationContext.getBean(TestService.class);
		System.out.println(testservice);

		testservice  = applicationContext.getBean(TestService.class);
		System.out.println(testservice); //getBean是单例，只实例化一次
	}

	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Autowired
	private TestData testData;
	@Autowired
	private TestService testService;
	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Test  //测试依赖注入
	public void testDI(){
		System.out.println(testData);
		System.out.println(testService);
		System.out.println(simpleDateFormat);
	}


}
