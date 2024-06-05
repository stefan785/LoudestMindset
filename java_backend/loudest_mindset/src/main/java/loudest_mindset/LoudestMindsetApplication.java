package loudest_mindset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoudestMindsetApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoudestMindsetApplication.class, args);
                
                System.out.print("""
                                   
                                   =======================================================
                                                
                                                       LOUDEST MINDSET
                                                
                                 
                                    If it does not run as expected check:
                                   
                                    - connection and all necessary rights on the database 
                                 
                                    - all read/write privileges in this folder 
                                   
                                   =======================================================
                                   """);
	}

}
