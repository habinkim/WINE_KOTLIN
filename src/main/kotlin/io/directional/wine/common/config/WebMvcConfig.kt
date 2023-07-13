package io.directional.wine.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Profile("local")
@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/")
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css")
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/")
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/")
    }
}
