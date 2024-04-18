package pl.sda.carrental.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.sda.carrental.configuration.configurationProperties.ApplicationSettings;

import java.util.function.Consumer;

@Configuration
@EnableWebSecurity
@ComponentScan()
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    ApplicationSettings applicationSettings;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizeAuthorization() {
        // I'm sorry for what I've done, I know it's bad, but I have no time left
        if (applicationSettings.isOverrideSecurity())
            return authorize -> authorize.anyRequest().permitAll();
        else
            return authorize ->
                authorize
                        .requestMatchers("/register/**").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/access-denied")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/dev/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users/**")).hasAnyAuthority("ADMIN", "EMPLOYEE")
                         .requestMatchers(new AntPathRequestMatcher("/divisions/**")).hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .anyRequest().authenticated();
    }
    @Bean
    public SecurityFilterChain filterSecurity(HttpSecurity http) throws Exception {
        http
                .csrf(c -> c.disable())
                .cors(c -> c.disable())
                .exceptionHandling(e -> e.accessDeniedPage("/error/unauthorized"))
                .authorizeHttpRequests(customizeAuthorization())
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .permitAll())
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll())
                .logout(logout -> logout.permitAll())
                .httpBasic(Customizer.withDefaults())
                .headers(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        return http.build();
    }
}