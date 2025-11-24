package com.DigitalClassRoomManagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                          // User
                          "/api/user/login",
                                         "/api/user/registerUser",
                                         "/api/user/getAll",
                                         "/api/user/getById/{id}",
                                         "/api/user/forgot-password",
                                         "/api/user/verify-otp",
                                         "/api/user/reset-password",
                                "/api/user/logout/{id}",
                                //Exam
                                "/api/exam/saveExam",
                                "/api/exam/UpdateByExamId/{examId}",
                                "/api/exam/GetByExamId/{id}",
                                "/api/getByTeacher/{teacherId}",
                                "/api/exam/GetAllExam",
                                "/api/exam/DeleteByExamId/{id}",
                                // Teacher

                                "/api/teacher/getAll",

                                "/api/teacher/getById",

                                "/api/teacher/update",

                                "/api/teacher/delete"
                        ).permitAll()


                        //Admin
                        .requestMatchers("/api/admin/update/status/{id}","/api/admin/get/unapproved/statusrequest","/api/admin/get/approved/statusrequest","api/admin/create").permitAll()

                        //SuperAdmin
                        .requestMatchers("/api/superAdmin/get/unapproved/statusrequest","/api/superAdmin/get/approved/statusrequest","/api/superAdmin/update/status/{id}").permitAll()

                        // Teacher endpoints
                        .requestMatchers(HttpMethod.POST, "/api/teacher/addTeacher").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/teacher/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/teacher/delete/**").hasRole("ADMIN")
                        .requestMatchers("/api/teacher/update/status/{id}").permitAll()
                        .requestMatchers("/api/teacher/get/unapproved/statusrequest").permitAll()
                        .requestMatchers("/api/teacher/get/approved/statusrequest").permitAll()

                        //Approved and Unapproved status
                        .requestMatchers("/api/teacher/get/unapproved/statusrequest","/api/teacher/update/status/{id}","/api/teacher/get/approved/statusrequest").permitAll()

                        // Read teacher -> ADMIN or TEACHER
                        .requestMatchers(HttpMethod.GET, "/api/teacher/getAll").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/teacher/getById/**").hasAnyRole("ADMIN", "TEACHER")

                        // Assign class -> ADMIN only
                        .requestMatchers(HttpMethod.POST, "/api/teacher/*/assign/*").hasRole("ADMIN")

                        // Unassign must use DELETE
                        .requestMatchers(HttpMethod.DELETE, "/api/teacher/*/unassign/*").hasRole("ADMIN")

                        // Get classes of teacher
                        .requestMatchers(HttpMethod.GET, "/api/teacher/*/classes").hasAnyRole("ADMIN", "TEACHER")

                        // SchoolClass endpoints
                        .requestMatchers(HttpMethod.POST, "/api/classes/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/classes/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/classes/delete/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/classes/getAll").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/classes/getById/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")

                        // Get teachers of class
                        .requestMatchers(HttpMethod.GET, "/api/classes/getTeachersOfClass/*/teachers").hasAnyRole("ADMIN", "TEACHER")

                        // All other requests require authentication


                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
