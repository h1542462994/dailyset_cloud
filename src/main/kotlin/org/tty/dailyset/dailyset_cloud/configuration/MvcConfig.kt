/**
 * create at 2022/4/16
 * @author h1542462994
 */

package org.tty.dailyset.dailyset_cloud.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.tty.dailyset.dailyset_cloud.auth.AuthInterceptor
import org.tty.dailyset.dailyset_cloud.auth.UserStateParameterResolver

@Configuration
class MvcConfig: WebMvcConfigurer{
    @Autowired
    private lateinit var userStateParameterResolver: UserStateParameterResolver

    @Autowired
    private lateinit var authInterceptor: AuthInterceptor

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userStateParameterResolver)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").order(-1)
    }



}