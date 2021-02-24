package com.github.casside.pac4jx.clients.app.config;

import com.github.casside.pac4jx.clients.pac4j.springframework.security.web.DelegatingCallbackFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.pac4j.core.client.finder.ClientFinder;
import org.pac4j.core.config.Config;
import org.pac4j.core.engine.SecurityLogic;
import org.pac4j.springframework.security.web.CallbackFilter;
import org.pac4j.springframework.security.web.LogoutFilter;
import org.pac4j.springframework.security.web.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 注意每一个 http security 必须配置 matcher，否则会拦截所有请求，只有默认的form登录，才需要拦截所有请求
 */
@EnableWebSecurity
public class SecurityConfig {

    @Order(800)
    @Configuration
    public class BasicSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .inMemoryAuthentication()
                .withUser("admin") // 添加用户admin
                .password("{noop}admin")  // 不设置密码加密
                .roles("ADMIN", "USER")// 添加角色为admin，user

                .and()
                .withUser("user") // 添加用户user
                .password("{noop}user")
                .roles("USER")

                .and()
                .withUser("tmp") // 添加用户tmp
                .password("{noop}tmp")
                .roles(); // 没有角色
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

//            http
//                .antMatcher("/form/**")
//                .authorizeRequests().anyRequest().authenticated()
//                .and()
//                .exceptionHandling().authenticationEntryPoint(new Pac4jEntryPoint(config, "FormClient"))
//                .and()
//                .addFilterBefore(filter, BasicAuthenticationFilter.class)
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

            http
                .authorizeRequests()//开启登录配置
                .antMatchers("/**").authenticated()//表示剩余的其他接口，登录之后就能访问
//                .antMatchers("/admin/**").hasRole("admin")//表示访问 /hello 这个接口，需要具备 admin 这个角色
//                .anyRequest().authenticated()//表示剩余的其他接口，登录之后就能访问
                .and()
                .formLogin()
                //定义登录页面，未登录时，访问一个需要登录之后才能访问的接口，会自动跳转到该页面
                // 可以使用spring自带登录页
//                .loginPage("/login")
                //登录处理接口
                .loginProcessingUrl("/doLogin")
                .permitAll()//和表单登录相关的接口统统都直接通过
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication)
                        throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        out.write("logout success");
                        out.flush();
                    }
                })
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
        }
    }

//    @Configuration
//    @Order(20)
//    public static class FormWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//
//        @Autowired
//        private Config config;
//
//        protected void configure(final HttpSecurity http) throws Exception {
//
//            final SecurityFilter filter = new SecurityFilter(config, "DirectBasicAuthClient,AnonymousClient");
//
//            http
//                .antMatcher("/form/**")
//                .authorizeRequests().anyRequest().authenticated()
//                .and()
//                .exceptionHandling().authenticationEntryPoint(new Pac4jEntryPoint(config, "FormClient"))
//                .and()
//                .addFilterBefore(filter, BasicAuthenticationFilter.class)
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
//        }
//    }

    @RequiredArgsConstructor
    @Configuration
    @Order(30)
    public static class BmskWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private final Config config;

        @Autowired
        SecurityLogic securityLogic;

        protected void configure(final HttpSecurity http) throws Exception {

            final SecurityFilter filter = new SecurityFilter(config, "oidc-01");
            filter.setSecurityLogic(securityLogic);

            http
                .antMatcher("/third/oidc/**")
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            ;
        }
    }

    @RequiredArgsConstructor
    @Configuration
    @Order(40)
    public static class WechatWorkWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private final Config config;

        @Autowired
        SecurityLogic securityLogic;

        protected void configure(final HttpSecurity http) throws Exception {

            final SecurityFilter filter = new SecurityFilter(config, "test-wework-01");
            filter.setSecurityLogic(securityLogic);

            http
                .antMatcher("/third/wechat_work/**")
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            ;
        }
    }

    @Configuration
    @Order(110)
    @RequiredArgsConstructor
    public static class LogoutWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private final Config config;

        protected void configure(final HttpSecurity http) throws Exception {

            // 统一登出URL
            final LogoutFilter centralLogoutFilter = new LogoutFilter(config, "/?defaultUrlAfterCentralLogout");
            centralLogoutFilter.setDestroySession(true);
            centralLogoutFilter.setLocalLogout(false);
            centralLogoutFilter.setCentralLogout(true);
            centralLogoutFilter.setLogoutUrlPattern(".*");

            http
                .antMatcher("/pac4jCentralLogout")
                .addFilterAfter(centralLogoutFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
        }
    }

    @Configuration
    @Order(100)
    @RequiredArgsConstructor
    public static class DefaultWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private final Config config;

        private final List<ClientFinder> clientFinders;

        protected void configure(final HttpSecurity http) throws Exception {

            CallbackFilter callbackFilter = new CallbackFilter(config);
            callbackFilter.setMultiProfile(true);

            // OIDC 回跳URL
            final DelegatingCallbackFilter delegatingCallbackFilter = new DelegatingCallbackFilter(callbackFilter, clientFinders, config);

            http
                .antMatcher("/callback")
                .addFilterBefore(delegatingCallbackFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .logout()
                .logoutSuccessUrl("/");
        }
    }

}
