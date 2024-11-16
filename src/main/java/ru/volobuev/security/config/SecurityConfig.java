package ru.volobuev.security.config;

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
import ru.volobuev.security.repository.RoleRepository;
import ru.volobuev.security.repository.UserRepository;
import ru.volobuev.security.security.CustomAuthenticationProvider;
import ru.volobuev.security.service.UserService;
import ru.volobuev.security.service.UserServiceImpl;
import ru.volobuev.security.utils.CustomLoginSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new CustomLoginSuccessHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/registration").not().authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/", "/login").permitAll()
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
    public UserService userService(
            UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return new UserServiceImpl(userRepository, roleRepository, passwordEncoder);
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(
            UserServiceImpl userServiceImpl, PasswordEncoder bCryptPasswordEncoder) {
        return new CustomAuthenticationProvider(userServiceImpl, bCryptPasswordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, CustomAuthenticationProvider authProvider) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }
}