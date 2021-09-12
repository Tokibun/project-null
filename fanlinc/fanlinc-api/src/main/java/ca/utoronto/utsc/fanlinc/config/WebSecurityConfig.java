package ca.utoronto.utsc.fanlinc.config;

import ca.utoronto.utsc.fanlinc.service.FanlincUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private FanlincUserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(FanlincUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // For PUT/POST
                .authorizeRequests()
                .antMatchers("/graphql").permitAll() // Enforce auth once we get good.
                .antMatchers("/accountonly").authenticated()
                .antMatchers("/anononly").anonymous()
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("http://localhost:3000").failureUrl("http://localhost:3000/loginFailure").permitAll()
                .and()
                .logout().logoutUrl("/logout").deleteCookies("SESSION").invalidateHttpSession(true).clearAuthentication(true).logoutSuccessUrl("http://localhost:3000/logoutSuccess").permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
