package com.baikalsr.queueserver.config;

import com.baikalsr.queueserver.service.AuthProvider;
import com.baikalsr.queueserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProvider authProvider;
    @Autowired
    private MyBasicAuthenticationEntryPoint basicAuthenticationEntryPoint;

    @Bean
    UserService userService() {
        UserService userService = new UserService();
        return userService;
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
    {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .authorizeRequests()
                .antMatchers("/resources/**", "/login**", "/registration", "/kiosk"
                        , "/setCommentKiosk", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.mp3", "/**/*.woff2", "/**/*.woff", "/**/*.ttf", "/kioskt/*",
                        "/tablo", "/rest/tablo/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/").failureUrl("/login?error").permitAll()
                .and().logout().logoutSuccessUrl("/login").permitAll()
                .and().csrf().ignoringAntMatchers("/rest/**").and().httpBasic().authenticationEntryPoint(basicAuthenticationEntryPoint);
    }
}
