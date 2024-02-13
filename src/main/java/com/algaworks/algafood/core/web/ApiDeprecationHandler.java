package com.algaworks.algafood.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//class server para interceptar metodos depreciados
//20.18. Depreciando uma versão da API
@Component
public class ApiDeprecationHandler implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (request.getRequestURI().startsWith("/v1/")) {
			response.addHeader("X-AlgaFood-Deprecated",
					"Essa versão da API está depreciada e deixará de existir a partir de 01/01/2021."
							+ "Use a versão mais atual da API.");
		}

		return true;
	}
	
	//20.19. Desligando uma versão da API
	//quando a versao da api precisa ser desligada
//	@Override
//	public boolean preHandle(HttpServletRequest request, 
//	                         HttpServletResponse response, Object handler)
//			throws Exception {
//		if (request.getRequestURI().startsWith("/v1/")) {
//			response.setStatus(HttpStatus.GONE.value());
//			return false;
//		}
//
//		return true;
//	}
}
