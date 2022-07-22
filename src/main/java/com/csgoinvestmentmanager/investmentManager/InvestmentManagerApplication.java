package com.csgoinvestmentmanager.investmentManager;

import com.csgoinvestmentmanager.investmentManager.Threads.CollectionThread;
import com.csgoinvestmentmanager.investmentManager.config.SwaggerConfiguration;
import com.csgoinvestmentmanager.investmentManager.model.AppUser;
import com.csgoinvestmentmanager.investmentManager.model.Role;
import com.csgoinvestmentmanager.investmentManager.service.intefaces.AppUserService;
import com.csgoinvestmentmanager.investmentManager.timedTaks.PriceUpdateTask;
import io.swagger.models.Swagger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class InvestmentManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestmentManagerApplication.class, args);

	}


	@Bean
	CommandLineRunner run(AppUserService userService,CollectionThread collectionThread) {
		return args -> {


			Thread thread = new Thread(collectionThread);
			thread.start();
/*
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_MANAGER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			userService.saveUser(new AppUser(null, "John Travolta", "1234", "1234", new ArrayList<>(),true));
			userService.saveUser(new AppUser(null, "Will Smith", "will", "1234", new ArrayList<>(),true));
			userService.saveUser(new AppUser(null, "Jim Carry", "jim", "1234", new ArrayList<>(),true));
			userService.saveUser(new AppUser(null, "Arnold Schwarzenegger", "123", "123", new ArrayList<>(),true));

			userService.addRoleToUser("1234", "ROLE_USER");
			userService.addRoleToUser("will", "ROLE_MANAGER");
			userService.addRoleToUser("jim", "ROLE_ADMIN");
			userService.addRoleToUser("123", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("123", "ROLE_ADMIN");
			userService.addRoleToUser("123", "ROLE_USER");

 */

		};


	}





	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200","https://angular-investmentapp.herokuapp.com/"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}



}
