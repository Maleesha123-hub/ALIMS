package com.alims.londontech.config;

import com.alims.londontech.service.JWT.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/airline/login",
                        "/airline/**",
                        "/airline/authenticate/**",
                        "/airline/userAccount/**",
                        "/airline/systemAdminDashboard/**",
                        "/airline/dataSync/**",

                        "/airline/arrangeSchedule/**",
                        "/airline/user/**",
                        "/airline/userRole/**",
                        "/airline/pilot/**",
                        "/airline/flightCrew/**",
                        "/airline/airplane/**",
                        "/airline/country/**",

                        "/systemAdminLoginDashboard/css/**",
                        "/systemAdminLoginDashboard/fonts/**",
                        "/systemAdminLoginDashboard/img/**",
                        "/systemAdminLoginDashboard/js/**",
                        "/systemAdminLoginDashboard/plugins/**",

                        "/systemAdminDashboard/css/**",
                        "/systemAdminDashboard/fonts/**",
                        "/systemAdminDashboard/images/**",
                        "/systemAdminDashboard/js/**",
                        "/systemAdminDashboard/libs/**"

                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }
}
