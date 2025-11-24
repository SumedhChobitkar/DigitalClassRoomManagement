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
                        // Swagger URLs (new custom + default paths)
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/swagger-config/**",
                                // allow the custom path too:-
                          // User
                          "/api/digitalClassroom/login", 
                                         "/api/digitalClassroom/registerUser",
                                         "/api/digitalClassroom/getAll",
                                         "/api/digitalClassroom/getById/{id}",
                                         "/api/digitalClassroom/forgot-password",
                                         "/api/digitalClassroom/verify-otp",
                                         "/api/digitalClassroom/reset-password",
                                //Exam
                                "/api/exam/saveExam",
                                "/api/exam/UpdateByExamId/{examId}",
                                "/api/exam/GetByExamId/{id}",
                                "/api/getByTeacher/{teacherId}",
                                "/api/exam/GetAllExam",
                                "/api/exam/DeleteByExamId/{id}",
                                // Teacher

                                "/api/teacher/add",
                                "/api/teacher/getAll",
                                "/api/teacher/getById",
                                "/api/teacher/update",
                                "/api/teacher/delete",

                                // Student Related
                                "/api/students/saveStudent",


                                // Subject Related
                                "/api/subject/**",


                                "/api/teacher/delete",


                                //Assignment
                                        "/api/assignments/create",
                                        "/api/assignments/updateAssignmentById/{id}",
                                        "/api/assignments/getAllAssignments",
                                        "/api/assignments/getAssignmentById/{id}",
                                        "/api/assignments/getAllAssignmentsByTeacherId/{teacherId}",
                                        "/api/assignments/getAssignmentsByIdAndTeacherId/{assignmentId}/{teacherId}",
                                        "/api/assignments/deleteAssignmentByIdAndTeacherId/{assignmentId}/{teacherId}",
                                        "/api/assignments/getAssignmentsFileByAssignmentId/{assignmentId}"


                        ).permitAll()

           

                                // Teacher endpoints
                        .requestMatchers(HttpMethod.POST, "/api/teacher/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/teacher/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/teacher/delete/**").hasRole("ADMIN")

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
