//package org.tty.dailyset.dailyset_cloud.configuration
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//import org.springframework.web.filter.CorsFilter
//import org.tty.dailyset.dailyset_cloud.service.UserService
//import javax.servlet.http.HttpServletResponse
//
//class SecurityConfig: WebSecurityConfigurerAdapter() {
//
//    @Autowired
//    private lateinit var userService: UserService
//
//    override fun configure(auth: AuthenticationManagerBuilder) {
//        auth.userDetailsService(userService)
//    }
//
//    override fun configure(http: HttpSecurity) {
//        // Enable CORS and disable CSRF
//        http.cors().and().csrf().disable()
//
//        // Set session management to stateless
//        http
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//        // Set unauthorized requests exception handler
//        http
//            .exceptionHandling()
//            .authenticationEntryPoint { _, response, ex ->
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.message)
//            }
//
//        http
//            .authorizeRequests()
//            .antMatchers("/api/user/**").permitAll()
//            .anyRequest().authenticated()
//
//
//    }
//
//    @Bean
//    fun corsFilter(): CorsFilter {
//        val source = UrlBasedCorsConfigurationSource()
//        val config = CorsConfiguration()
//        config.allowCredentials = true
//        config.addAllowedOrigin("*")
//        config.addAllowedHeader("*")
//        config.addAllowedMethod("*")
//        source.registerCorsConfiguration("/**", config)
//
//        return CorsFilter(source)
//    }
//
//    @Bean
//    override fun authenticationManagerBean(): AuthenticationManager {
//        return super.authenticationManagerBean()
//    }
//}