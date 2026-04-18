package com.yhh.weblogadmin.config;

import com.yhh.weblogjwt.config.JwtAuthenticationSecurityConfig;
import com.yhh.weblogjwt.filter.TokenAuthenticationFilter;
import com.yhh.weblogjwt.handler.RestAccessDeniedHandler;
import com.yhh.weblogjwt.handler.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationSecurityConfig jwtAuthenticationSecurityConfig;

    @Autowired
    private RestAuthenticationEntryPoint authEntryPoint;

    @Autowired
    private RestAccessDeniedHandler deniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin().disable()
                .apply(jwtAuthenticationSecurityConfig)
                .and()
                .authorizeHttpRequests()
                .mvcMatchers("/login", "/register",
                        "/uploads/**", "/images/**",
                        "/doc.html", "/swagger-resources/**", "/webjars/**", "/v2/api-docs", "/favicon.ico")
                .permitAll()
                .mvcMatchers("/admin/**", "/blog/**", "/article/**", "/category/**", "/tag/**", "/archive/**", "/statistics/**")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic().authenticationEntryPoint(authEntryPoint)
                .and()
                .exceptionHandling().accessDeniedHandler(deniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }
}
