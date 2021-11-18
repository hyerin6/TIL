package net.skhu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import net.skhu.service.MyAuthenticationProvider;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired MyAuthenticationProvider myAuthenticationProvider;

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers("/res/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests() // 검사를 위에서부터 아래로하기 때문에 순서가 중요함
                .antMatchers("/admin/**").access("ROLE_ADMIN") // 로그인 한 사람만 검사
                .antMatchers("/professor/**").access("ROLE_PROFESSOR") // 로그인 한 사람만 검사
                .antMatchers("/guest/**").permitAll() // 로그인 했던 안했던 다 검사
                .antMatchers("/").permitAll() // 로그인 했던 안했던 다 검사
                .antMatchers("/**").authenticated(); // 로그인 한 사람만 검사

        http.csrf().disable();

        http.formLogin()
                .loginPage("/guest/login")
                .loginProcessingUrl("/guest/login_processing")
                .failureUrl("/guest/login?error")
                .defaultSuccessUrl("/user/index", true)
                .usernameParameter("loginId")
                .passwordParameter("passwd");

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout_processing"))
                .logoutSuccessUrl("/guest/login")
                .invalidateHttpSession(true);

        http.authenticationProvider(myAuthenticationProvider);
    }
}
