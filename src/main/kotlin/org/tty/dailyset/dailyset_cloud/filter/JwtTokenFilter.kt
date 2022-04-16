//package org.tty.dailyset.dailyset_cloud.filter
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.http.HttpHeaders
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
//import org.springframework.stereotype.Component
//import org.springframework.web.filter.OncePerRequestFilter
//import org.tty.dailyset.dailyset_cloud.bean.Constant
//import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes
//import org.tty.dailyset.dailyset_cloud.bean.ResponseCodes.success
//import org.tty.dailyset.dailyset_cloud.component.IntentFactory
//import org.tty.dailyset.dailyset_cloud.service.UserService
//import javax.servlet.FilterChain
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
//class JwtTokenFilter: OncePerRequestFilter() {
//    @Autowired
//    private lateinit var userService: UserService
//
//    @Autowired
//    private lateinit var intentFactory: IntentFactory
//
//
//    override fun doFilterInternal(
//        request: HttpServletRequest,
//        response: HttpServletResponse,
//        chain: FilterChain
//    ) {
//
//        // Get authorization header and validate
//        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
//        if (authHeader.isNullOrEmpty() || !authHeader.startsWith(Constant.BEARER)) {
//            chain.doFilter(request, response)
//            return
//        }
//
//        // Get the state of the token
//        val intent = intentFactory.createUserStateIntent(authHeader)
//        val userState = userService.internalState(intent)
//
//        // validate the state
//        if (userState?.userActivity == null) {
//            chain.doFilter(request, response)
//            return
//        }
//
//        val userDetails = userState.user!!.toUserDetails()
//        val authentication = UsernamePasswordAuthenticationToken(
//            userDetails,
//            null,
//            userDetails.authorities
//        )
//
//        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
//        SecurityContextHolder.getContext().authentication = authentication
//        chain.doFilter(request, response)
//    }
//}