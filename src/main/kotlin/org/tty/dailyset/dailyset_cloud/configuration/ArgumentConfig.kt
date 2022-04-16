package org.tty.dailyset.dailyset_cloud.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.tty.dailyset.dailyset_cloud.auth.UserStateParameterResolver

@Configuration
class ArgumentConfig: WebMvcConfigurer{
    @Autowired
    private lateinit var userStateParameterResolver: UserStateParameterResolver

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userStateParameterResolver)
    }
}