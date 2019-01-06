package av.av8242n.reactivespringdata;

import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactiveSpringDataApplicationTests {

	@Autowired
	ApplicationContext context;

	@Test
	public void contextLoads() {
		System.out.println(context.getBeanDefinitionCount());
	}

}

