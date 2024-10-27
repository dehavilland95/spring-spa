package ru.volobuev.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.volobuev.security.security.CustomAuthenticationProvider;
import ru.volobuev.security.utils.CustomLoginSuccessHandler;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.volobuev.security.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    @Autowired
    UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private CustomAuthenticationProvider authProvider;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new CustomLoginSuccessHandler();
    }


//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
           .authorizeHttpRequests((requests) -> requests
                   .requestMatchers("/registration").not().authenticated()
                   .requestMatchers("/admin").hasRole("ADMIN")
                   .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
           .requestMatchers("/", "/login", "/error", "/resources/**", "/logout", "/style.css").permitAll()
           .anyRequest().authenticated())
               .formLogin(formLogin -> formLogin.loginPage("/login")
                       .loginProcessingUrl("/login")
                       .successHandler(myAuthenticationSuccessHandler())
                       .permitAll())
               .logout(logout ->
                       logout.logoutUrl("/logout")
                       .logoutSuccessUrl("/login"));
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }
}