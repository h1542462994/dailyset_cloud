/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.tty.dailyset.dailyset_cloud.bean.UserState
import org.tty.dailyset.dailyset_cloud.component.IntentFactory
import org.tty.dailyset.dailyset_cloud.service.UserService
import org.tty.dailyset.dailyset_cloud.util.getToken


@Component
class UserStateParameterResolver: HandlerMethodArgumentResolver {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var intentFactory: IntentFactory

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == UserState::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val authHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        val token = getToken(authHeader) ?: return null

        val intent = intentFactory.createUserStateIntent(token)
        return userService.internalState(intent)
    }
}