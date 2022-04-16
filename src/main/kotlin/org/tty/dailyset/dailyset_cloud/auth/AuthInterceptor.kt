package org.tty.dailyset.dailyset_cloud.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.tty.dailyset.dailyset_cloud.bean.isActive
import org.tty.dailyset.dailyset_cloud.component.IntentFactory
import org.tty.dailyset.dailyset_cloud.service.UserService
import org.tty.dailyset.dailyset_cloud.util.getToken
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor: HandlerInterceptor {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var intentFactory: IntentFactory

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {


        /**
         * if the handler is not a function, skip.
         */
        if (handler !is HandlerMethod) {
            return true
        }

        /**
         * if the handler annotation is Anonymous, skip.
         */
        if (handler.hasMethodAnnotation(Anonymous::class.java)) {
            return true
        }

        val auth = request.getHeader(HttpHeaders.AUTHORIZATION) ?: throw AuthError(AuthError.AUTH_TYPE_TOKEN_ERROR,"没有token")
        val token = getToken(auth) ?: throw AuthError(AuthError.AUTH_TYPE_TOKEN_ERROR,"auth格式错误")
        val intent = intentFactory.createUserStateIntent(token)

        val userState = userService.internalState(intent)
        if (userState.isActive()) {
            return true
        } else {
            if (userState == null) {
                throw AuthError(AuthError.AUTH_TYPE_TOKEN_ERROR,"token格式错误")
            } else if (userState.user == null) {
                throw AuthError(AuthError.AUTH_TYPE_USER_NO_EXISTS,"用户不存在")
            } else {
                throw AuthError(AuthError.AUTH_TYPE_DEVICE_CODE_ERROR,"当前设备不存在")
            }
        }
    }
}