package krd.skysoft.TestingApp;

import krd.skysoft.TestingApp.services.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class TestingAppApplication implements CommandLineRunner {


//	private final DataService dataService;

	@Value("${my.varible}")
	private String myVarible;

	@Override
	public void run(String... args) throws Exception {
//		System.out.println("Injected from "+dataService.getData()+" Service");
		System.out.println("MyVarible loaded from :- application-"+myVarible+" file");
	}

	public static void main(String[] args) {
		SpringApplication.run(TestingAppApplication.class, args);
	}

}
