package com.algaworks.algafood.core.web;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//aula 16.6 Habilitando CORS globalmente no projeto da API
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	ApiDeprecationHandler apiDeprecationHandler;

	// 20.18. Depreciando uma versão da API
	// para interceptar metodos depreciados
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(apiDeprecationHandler);
//	}
//	

	// aula 17.5 filtro para Etags. aula de caches
	@Bean
	public Filter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
}
