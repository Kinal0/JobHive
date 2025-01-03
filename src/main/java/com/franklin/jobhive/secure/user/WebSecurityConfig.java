package com.franklin.jobhive.secure.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {


    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    private final String[] publicUrl = {"/error", "/user/**", "/login", "/resources/**", "/assets/**", "/css/**", "/styles/**", "/images/**"};


    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth -> {
                            auth.requestMatchers(publicUrl).permitAll();
                            auth.requestMatchers("/skill/**").hasAuthority("RECRUITER");
                            auth.requestMatchers("/company/**").hasAuthority("RECRUITER");
                            auth.requestMatchers("/job/**").hasAnyAuthority("RECRUITER", "JOBSEEKER");
                            auth.requestMatchers("/application/**").hasAnyAuthority("RECRUITER", "JOBSEEKER");
                            auth.anyRequest().authenticated();
                        }
                );

        http.formLogin( form->form.loginPage("/login").permitAll()
                                   .successHandler(loginSuccessHandler))
                                   .logout( logout-> {
                                                        logout.logoutUrl("/logout");
                                                        logout.logoutSuccessUrl("/");
                                   })
                                   .cors(Customizer.withDefaults())
                                   .csrf(csrf->csrf.disable())
                                   .exceptionHandling( exception -> exception.accessDeniedPage("/error"));

        return http.build();

    }
}
