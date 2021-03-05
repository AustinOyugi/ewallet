package com.doorstep.springproject.config;


import com.doorstep.springproject.security.CustomAuthenticationFailureHandler;
import com.doorstep.springproject.security.JwtAuthenticationEntryPoint;
import com.doorstep.springproject.security.JwtAuthenticationFilter;
import com.doorstep.springproject.services.auth.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

      private static final String[] AUTH_LIST_SWAGGER = {
        // -- swagger ui
        "**/swagger-resources/**", "/swagger-ui.html/**", "/v2/api-docs", "/webjars/**"
      };

      private final CustomUserDetailsService customUserDetailsService;

      private final JwtAuthenticationEntryPoint unauthorizedHandler;

      @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
      public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
      }

      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
      }

      @Bean(BeanIds.AUTHENTICATION_MANAGER)
      @Override
      public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
      }

      @Bean
      public AuthenticationFailureHandler authenticationFailureHandler(){
          return new CustomAuthenticationFailureHandler();
      }

      @Bean
      PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
      }

      @Override
      public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_LIST_SWAGGER);
        web.ignoring().mvcMatchers(AUTH_LIST_SWAGGER);
      }

      @Override
      protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .csrf()
            .disable()
            .exceptionHandling()
            .authenticationEntryPoint(unauthorizedHandler)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                    "/",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js")
            .permitAll()
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers("/h2-console/**")
                .permitAll()
            .anyRequest()
            .authenticated();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
      }
}
