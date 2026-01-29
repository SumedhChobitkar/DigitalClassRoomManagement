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
                                "/payment.html",
                                "/",
                                "/static/**",
                                "/public/**",
                                "/resources/**",
                                "/favicon.ico",
                                        "/chat/**",       // 🔥 websocket endpoint
                                        "/ws/**",
                                "/api/chat/**",
                                        "/ws-chat/**",
                                "/student.html",
                                "/parent.html",
                                "/teacher.html",
                                        "/api/notifications/**",
                                // for chat
                              //  "/api/chat/student/open",

                                // OAuth endpoints (make these publicly accessible)
                                //OAuth
                               "/oauth/**",
                               "/google/**",
                               "/oauth/callback",
                               "/oauth2/**",
                               "/oauth2/authorize",
                               "/oauth2/callback",

                                //Meet
                                "/meet/**",

                                // Home & error
                                "/",
                                "/error",

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
                                "/api/exam/TeacherSaveExam",
                                "/api/exam/schedule/{examId}",
                                "/api/exam/UpdateByExamId/{examId}",
                                "/api/exam/by-question/{questionId}",
                                "/api/exam/GetByExamId/{id}",
                                "/api/exam/getByTeacher/{teacherId}",
                                "/api/exam/getAllExam",
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
                                "/api/teacher/update/status/{id}",
                                //Teacher profile dashboard
                                "/api/profile/dashboard/*/add-profile-picture",
                                "/api/profile/dashboard/*/update-profile-picture",
                                "/api/profile/dashboard/*/get-profile-picture",
                                "/api/profile/dashboard/*/remove-profile-picture",

                                // Student Related
                              //  "/api/students/saveStudent",

                                // Subject Related
                                "/api/subject/**",
                                "/api/teacher/exam/GetAllQuestion",


                                "/api/results/top",
                                "/api/results/Create-result",
                                "/api/results/{id}",
                                "/api/results/GetAll",

                                "/api/teacher/delete",


                                //Assignment
                                "/api/assignments/create",
                                "/api/assignments/updateAssignmentById/{id}",
                                "/api/assignments/getAllAssignments",
                                "/api/assignments/getAssignmentsById/{id}",
                                "/api/assignments/getAllAssignmentsByTeacherId/{teacherId}",
                                "/api/assignments/getAssignmentsByIdAndTeacherId/{assignmentId}/{teacherId}",
                                "/api/assignments/deleteAssignmentByIdAndTeacherId/{assignmentId}/{teacherId}",
                                "/api/assignments/getAssignmentsFileByAssignmentId/{assignmentId}",

                                //TeacherAssignmentSubmission
                                "/api/teacher/assignments/create",
                                "/api/teacher/assignments/updateAssignmentById/{id}",
                                "/api/teacher/assignments/getAllAssignments",
                                "/api/teacher/assignments/getAssignmentById/{id}",
                                "/api/teacher/assignments/deleteAssignmentById/{id}",
                                        "/api/teacher/assignments/getAssignmentFileByTeacherAssignmentId/{Id}",

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
//                                "/api/results/Create-result",
//                                "/api/results/GetResult/{id}",
//                                "/api/results/getAllResult",
//                                "/api/results/UpdateResult/{id}",
//                                "api/results/DeleteById/{id}",
//                                "/api/results/top",


                                //StudentExam
                                "/api/exam/submit",
                                "/api/exam/getAllExam",
                                "/api/exam/result",


                                //TeacherExam
//                               "/api/teacher/exam/{examId}/questions",
//                                "/api/teacher/exam/submissions",
//                                "/api/teacher/exam/GetAllQuestion",
//                                "/api/teacher/exam/teacher/{teacherId}/questions",



                                //Admin Exam
                                "/api/Create-exam/CreateExam",
                                "/api/Create-exam/getAll",






                                "/api/teacher/delete",

                                //ContactUs
                                "/api/contact/**",




                                //ReportCard

                                "/api/reportCards/create",
                                "/api/reportcards/getReportCardById/{id}",
                                "/api/reportcards/getAllReportCards",
                                "/api/reportcards/getReportCardByStudentId/{studentId}",
                                "/api/reportcards/updateReportCardById/{id}",
                                "/api/reportcards/deleteReportCardById/{id}",
//
//                                // AuditLog
//                                "/api/auditlogs/saveAuditlog",
//                                "/api/auditlogs/getAuditlogById/{id}",
//                                "/api/auditlogs/getAllAuditlogs",
//                                "/api/auditlogs/updateAuditlogById/{id}",
//                                "/api/auditlogs/deleteAuditlogById/{id}"


//                                //LibraryMember
//                                  "/api/Librarymembers/saveLibraryMember",
//                                "/api/Librarymembers/getByIdLibraryMember/{id}",
//                                  "/api/Librarymembers/getAllLibraryMembers",
//                                "/api/Librarymembers/updateLibraryMemberById/{id}",
//                                "/api/Librarymembers/deleteLibraryMemberyById/{id}"

//                                "/api/admissions/createAdmission",
                                "/api/admissions/getAllAdmissions",
                                "/api/admissions/getByIdAdmissions/{id}",
                                "/api/admissions/updateAdmissions/{id}",
                                "/api/admissions/deleteAdmissions/{id}",


                                        //websocket chatbot
                                        "api/chat/parent/open",
                                        "api/chat/parent-teacher"



                        ).permitAll()


                         //SESSION APIs
                         // READ sessions -> PUBLIC
                         .requestMatchers(HttpMethod.GET, "/api/sessions/**").permitAll()
                         // CREATE session
                          .requestMatchers(HttpMethod.POST, "/api/sessions/create").permitAll()
                          .requestMatchers(HttpMethod.GET,"/api/session/getAll").permitAll()
                          .requestMatchers(HttpMethod.GET,"/api/session/getById/{Id}").permitAll()
                          .requestMatchers(HttpMethod.GET,"/api/session/{id}/create-meet").permitAll()
                         .requestMatchers(HttpMethod.GET,"/api/session/teacher/{teacherId}").permitAll()


                        //Teacher Exam
                        .requestMatchers(HttpMethod.POST, "/api/teacher/exam/{examId}/questions").hasAnyRole("TEACHER", "PRINCIPAL", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/teacher/exam/submissions").hasAnyRole("TEACHER","ADMIN","PRINCIPAL")
                        .requestMatchers(HttpMethod.GET, "/api/teacher/exam/GetAllQuestion").hasAnyRole("STUDENT", "TEACHER", "ADMIN", "PRINCIPAL")
                        .requestMatchers(HttpMethod.GET, "/api/teacher/exam/{teacherId}/questions").hasAnyRole("STUDENT", "TEACHER", "ADMIN", "PRINCIPAL")


                        // Student Exam
                        .requestMatchers(HttpMethod.POST, "/api/student/exam/submit").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/student/exam/scheduled").hasAnyRole("STUDENT", "TEACHER", "ADMIN", "PRINCIPAL")
                        .requestMatchers(HttpMethod.GET, "/api/student/exam/result").hasAnyRole("STUDENT", "PARENT")

                        //Result
                        .requestMatchers(HttpMethod.POST, "/api/results/Create-result").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/results/GetResult/{id}").hasAnyRole("ADMIN", "TEACHER", "STUDENT","PRINCIPAL")
                        .requestMatchers(HttpMethod.GET, "/api/results/getAllResult").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/api/results/UpdateResult/{id}").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "api/results/DeleteById/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/results/top").hasAnyRole("ADMIN", "TEACHER", "STUDENT")


                        //  Feedback
                        .requestMatchers(HttpMethod.POST, "/api/feedback/FeedbackCreate").hasAnyRole("STUDENT", "PARENT")
                        .requestMatchers(HttpMethod.GET, "/api/feedback/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/feedback/FeedBack_get_student").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/feedback/FeedBack_get_parent").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/feedback/getAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/feedback/{id}/review").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/feedback/Delete/{id}").hasRole("ADMIN")

                        //Admin Exam
                        .requestMatchers(HttpMethod.POST, "/api/exams/AdminExam-Create").hasAnyRole("ADMIN", "PRINCIPAL", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/exams/AdminGetAllExam").hasAnyRole("ADMIN", "TEACHER", "PRINCIPAL")
                        .requestMatchers(HttpMethod.GET, "/api/exams/{id}").hasAnyRole("ADMIN", "TEACHER","PRINCIPAL")
                        .requestMatchers(HttpMethod.PUT, "/api/exams/Update_By/{id}").hasAnyRole("ADMIN", "PRINCIPAL")
                        .requestMatchers(HttpMethod.DELETE, "/api/exams/Delete/{id}").hasAnyRole("ADMIN", "PRINCIPAL")


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
                        .requestMatchers(HttpMethod.POST,"/api/teacher/addTeacher").hasRole("ADMIN")

                        //Timetable Related
                        .requestMatchers(HttpMethod.POST,"/api/timetable/createTimetable").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/timetable/get/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/timetable/getAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/timetable/update/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/timetable/delete/{id}").hasRole("ADMIN")

                        //Location Related
                        .requestMatchers(HttpMethod.POST, "/api/locations/save/{schoolId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/locations/{locationId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/locations/{locationId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/locations/name/{schoolName}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/locations/name/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/locations/{locationId}/map-link").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/locations/update/{locationId}").permitAll()

                        //TeacherTimetable Related
                        .requestMatchers(HttpMethod.GET, "/api/teacherTimetable/{teacherId}/timetable").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET,"/api/teacherTimetable/{teacherId}/sections").hasRole("TEACHER")

                        //StudentTimetable Related
                        .requestMatchers(HttpMethod.GET,"/api/studentTimetable/{sectionId}/timetable").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET,"/api/studentTimetable/{sectionId}/section").hasRole("STUDENT")

                        // STUDENT
                        .requestMatchers(HttpMethod.POST,"/api/students/saveStudent").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/students/getStudentById/**").hasAnyRole("STUDENT", "TEACHER", "ADMIN", "PARENT")
                        .requestMatchers(HttpMethod.GET, "/api/students/getAllStudent").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/students/updateStudentById/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/deleteStudentById/**").hasRole("ADMIN")

                        // parent
                        .requestMatchers(HttpMethod.POST, "/api/parents/saveParent").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/parents/getParentById/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/parents/getAllParent").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/parents/updateParent/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/parents/deleteParentById/**").hasAnyRole("TEACHER", "ADMIN")
                        // Linking parent to student — ADMIN ONLY
                        .requestMatchers(HttpMethod.POST, "/api/parents/linkParentToStudent").hasRole("ADMIN")

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

                        .requestMatchers(HttpMethod.POST, "/api/admissions/createAdmissions").hasRole("PRINCIPAL")


                                // ================= CHAT CONTROLLER =================
                        .requestMatchers("/chat/**", "/ws/**", "/ws-chat/**").permitAll()
                                // WebSocket message mapping (STOMP)
                                .requestMatchers("/app/chat/send").authenticated()

                                 // Parent ↔ Teacher
                                .requestMatchers(HttpMethod.GET, "/api/chat/parent-teacher").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/chat/parent/open").authenticated()

                                 // Student ↔ Teacher
                                .requestMatchers(HttpMethod.GET, "/api/chat/student-teacher").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/chat/student/open").authenticated()

                                 // Teacher views
                                .requestMatchers(HttpMethod.GET, "/api/chat/student-messages").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/chat/parent-messages").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/chat/teacher/messages").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/chat/teacher/open").authenticated()
                                //Payment
                                .requestMatchers(HttpMethod.POST,
                                "/api/payment/create-class-payment-request"
                        ).hasRole("PRINCIPAL")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/payment/delete-payment/**"
                        ).hasRole("PRINCIPAL")

                        //  PARENT only
                        .requestMatchers(HttpMethod.POST,
                                "/api/payment/createOrder",
                                "/api/payment/verify"
                        ).hasRole("PARENT")

                        //  ADMIN + PRINCIPAL
                        .requestMatchers(HttpMethod.GET,
                                "/api/payment/allPayments",
                                "/api/payment/status/**"
                        ).hasAnyRole("ADMIN", "PRINCIPAL")

                        // STUDENT / PARENT / PRINCIPAL
                        .requestMatchers(HttpMethod.GET,
                                "/api/payment/fetch"
                        ).hasAnyRole("STUDENT", "PARENT", "PRINCIPAL")

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


