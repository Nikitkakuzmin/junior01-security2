package kz.nikitka.spring.security.springsecurity.config;

import kz.nikitka.spring.security.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());

        http.exceptionHandling().accessDeniedPage("/forbidden");

        http.formLogin()
                .loginPage("/signin")  // page of login form
                .loginProcessingUrl("/auth") // <form action = "/auth">
                .defaultSuccessUrl("/profile") // redirect page if ok
                .failureUrl("/signin?error") // if incorrect email or password
                .usernameParameter("user_email") // <input type = 'email' name = 'user_email'>
                .passwordParameter("user_password"); // <input type = 'password' name = 'user_password'>

        http.logout()
                .logoutUrl("/exit") //<form action = "/exit" method = "post">
                .logoutSuccessUrl("/signin");

        return http.build();
    }
}
