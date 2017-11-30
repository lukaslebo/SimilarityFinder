package ch.propulsion.similarityfinder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				super.addCorsMappings(registry);
				registry.addMapping("/**")
					.allowedMethods("PUT", "DELETE", "GET", "POST");
//				registry.addMapping("/**").allowedOrigins("http://localhost:3000");
			}
		};
	}
	
	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	
	    	httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
			.authorizeRequests().antMatchers("/h2_console/**").permitAll();
    	
        httpSecurity
        
	        // we don't need CSRF because our token is invulnerable
	        .csrf().disable()
	        
	        // don't create session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            
            .mvcMatcher("/**")
	            .authorizeRequests()
					.mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					.and()
            
            .mvcMatcher("/api/user/**")
				.authorizeRequests()
					.mvcMatchers(HttpMethod.GET, "/api/user/**").permitAll()
					.mvcMatchers(HttpMethod.POST, "/api/user/**").permitAll()
					.mvcMatchers(HttpMethod.PUT, "/api/user/**").permitAll()
					.and()
			
			.mvcMatcher("/api/document/**")
				.authorizeRequests()
					.mvcMatchers(HttpMethod.GET, "/api/document/**").permitAll()
					.mvcMatchers(HttpMethod.POST, "/api/document/**").permitAll()
					.and()
			
				.authorizeRequests()
					.antMatchers(
		                    HttpMethod.GET,
		                    "/",
		                    "/*.html",
		                    "/favicon.ico",
		                    "/**/*.html",
		                    "/**/*.css",
		                    "/**/*.js"
		                ).permitAll()
		                .antMatchers("/auth/**").permitAll()
		                .anyRequest().authenticated()
		                .and()
			
		        .mvcMatcher("/**")
					.authorizeRequests()
						.mvcMatchers("/**").denyAll();
		                

		        // disable page caching
		        httpSecurity.headers().cacheControl();
		        
		        httpSecurity.headers().frameOptions().disable();
	
	}
}
