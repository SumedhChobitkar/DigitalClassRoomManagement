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
                          "/api/user/login",
                                         "/api/user/registerUser",
                                         "/api/user/getAll",
                                         "/api/user/getById/{id}",
                                         "/api/user/forgot-password",
                                         "/api/user/verify-otp",
                                         "/api/user/reset-password",
                                "/api/user/logout/{id}",
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

                                "/api/teacher/addTeacher",
                                "/api/teacher/getAll",
                                "/api/teacher/getById",
                                "/api/teacher/update",
                                "/api/teacher/delete",
                                "/api/teacher/add",
                                "/api/teacher/getAllTeachers",
                                "/api/teacher/getTeacherById/{id}",
                                "/api/teacher/updateTeacherById/{id}",
                                "/api/teacher/deleteTeacherById/{id}",
                                //Teacher profile dashboard
                                "/api/profile/dashboard/{id}/add-profile-picture",
                                "/api/profile/dashboard/{id}/update-profile-picture",
                                "/api/profile/dashboard/{id}/get-profile-picture",
                                "/api/profile/dashboard/{id}/remove-profile-picture",


                                // Student Related
                                "/api/students/saveStudent",


                                // Subject Related
                                "/api/subject/**",


                                "/api/teacher/delete",

                                //ContactUs
                                "/api/contact/**",



                                //Assignment
                                        "/api/assignments/create",
                                        "/api/assignments/updateAssignmentById/{id}",
                                        "/api/assignments/getAllAssignments",
                                        "/api/assignments/getAssignmentById/{id}",
                                        "/api/assignments/getAllAssignmentsByTeacherId/{teacherId}",
                                        "/api/assignments/getAssignmentsByIdAndTeacherId/{assignmentId}/{teacherId}",
                                        "/api/assignments/deleteAssignmentByIdAndTeacherId/{assignmentId}/{teacherId}",
                                        "/api/assignments/getAssignmentsFileByAssignmentId/{assignmentId}",

                                //TeacherAssignmentSubmission
                                "api/teacher/assignments/create",
                                "api/teacher/assignments/updateAssignmentById/{id}",
                                "api/teacher/assignments/getAllAssignments",
                                "api/teacher/assignments/getAssignmentById/{id}",
                                "api/teacher/assignments/deleteAssignmentById/{id",

                                // Homework
                                "/api/homeworks/saveHomework",
                                "/api/homeworks/getHomeworkById/{id}",
                                "/api/homeworks/getAllHomework",
                                "/api/homeworks/updateHomeworkById/{id}",
                                "/api/homeworks/deleteHomeworkById/{id}",

                                // AuditLog
                                "/api/auditlogs/saveAuditlog",
                                "/api/auditlogs/getAuditlogById/{id}",
                                "/api/auditlogs/getAllAuditlogs",
                                "/api/auditlogs/updateAuditlogById/{id}",
                                "/api/auditlogs/deleteAuditlogById/{id}",


                                //LibraryMember
                                  "/api/Librarymembers/saveLibraryMember",
                                "/api/Librarymembers/getByIdLibraryMember/{id}",
                                  "/api/Librarymembers/getAllLibraryMembers",
                                "/api/Librarymembers/updateLibraryMemberById/{id}",
                                "/api/Librarymembers/deleteLibraryMemberyById/{id}",

                                //Student
                                "/api/students/**",

                                //parent
                                "/api/parents/**",


                                //Result
                                "/api/results/SaverResult",
                                "/api/results/GetResult/{id}",
                                "/api/results/getAllResult",
                                "/api/results/UpdateResult/{id}",
                                "api/results/DeleteById/{id}",
                                "/api/results/top",


                                //StudentExam
                                "/api/exam/submit",
                                "/api/exam/getAllExam",
                                "/api/exam/result",


                                //TeacherExam
                                "/api/TeacherExam/{examId}/questions",
                                "/api/TeacherExam/submissions",


                                //Admin Exam
                                "/api/Create-exam/CreateExam",
                                "/api/Create-exam/getAll",


                                //FeedBack
                                "/api/feedback/FeedbackCreate",
                                "api/feedback/FeedBack_get_student",
                                "api/feedback/FeedBack_get_parent",
                                "/api/feedback/{id}",
                                "api/feedback/getAll",
                                "api/feedback/{id}/review",
                                "api/feedback/{id}"



                                ).permitAll()


                        //Section Related
                        .requestMatchers(HttpMethod.POST, "/api/sections/AddSection").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/sections/getAllSections").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/sections/getSectionById/{id}").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/api/sections/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/sections/by-teacher/{teacherId}").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/sections/delete/{id}").hasRole("ADMIN")
                        //Admin
                        .requestMatchers("/api/admin/update/status/{id}","/api/admin/get/unapproved/statusrequest","/api/admin/get/approved/statusrequest","api/admin/create").permitAll()

                        //SuperAdmin
                        .requestMatchers("/api/superAdmin/get/unapproved/statusrequest","/api/superAdmin/get/approved/statusrequest","/api/superAdmin/update/status/{id}").permitAll()

                        // Teacher endpoints
                        .requestMatchers(HttpMethod.POST, "/api/teacher/addTeacher").hasRole("ADMIN")

                        //Timetable Related
                        .requestMatchers(HttpMethod.POST, "/api/timetable/createTimetable").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/timetable/get/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/timetable/getAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/timetable/update/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/timetable/delete/{id}").hasRole("ADMIN")

                        //TeacherTimetable Related
                        .requestMatchers(HttpMethod.GET, "/api/teacherTimetable/{teacherId}/timetable").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET,"/api/teacherTimetable/{teacherId}/sections").hasRole("TEACHER")

                        //StudentTimetable Related
                        .requestMatchers(HttpMethod.GET,"/api/studentTimetable/{sectionId}/timetable").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET,"/api/studentTimetable/{sectionId}/section").hasRole("STUDENT")





                        // Teacher endpoints
                        .requestMatchers(HttpMethod.POST, "/api/teacher/add").hasRole("ADMIN")
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

                        // HOMEWORK MANAGEMENT
                        .requestMatchers(HttpMethod.POST, "/api/homeworks/saveHomework").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/api/homeworks/updateHomeworkById/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/homeworks/deleteHomeworkById/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/homeworks/getHomeworkById/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/homeworks/getAllHomework").hasAnyRole("ADMIN", "TEACHER", "STUDENT")

                        // AuditLog endpoints
                        .requestMatchers(HttpMethod.POST, "/api/auditlogs/saveAuditlog").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/auditlogs/updateAuditlogById/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/auditlogs/deleteAuditlogById/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/auditlogs/getAuditlogById/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/auditlogs/getAllAuditlogs").hasAnyRole("ADMIN", "TEACHER")

                        // LibraryMember endpoints
                        .requestMatchers(HttpMethod.POST, "/api/Librarymembers/saveLibraryMember").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/Librarymembers/updateLibraryMemberById/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/Librarymembers/deleteLibraryMemberById/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/Librarymembers/getByIdLibraryMember/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/Librarymembers/getAllLibraryMembers").hasAnyRole("ADMIN", "TEACHER")

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


