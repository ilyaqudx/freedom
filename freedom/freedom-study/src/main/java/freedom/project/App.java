package freedom.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration//配置控制
@EnableAutoConfiguration//自动配置
@ComponentScan//组件扫描  
public class App {

	public static void main(String[] args) 
	{
		SpringApplication.run(App.class, args);  
	}
}
