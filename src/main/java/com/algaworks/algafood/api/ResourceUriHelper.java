package com.algaworks.algafood.api;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.experimental.UtilityClass;


//class utilitaria para uri de recursos. QUANDO CRIADO UM RECURSO VAI DEVOLVE UMA URL DE DESSE RECURSO CRIADO
@UtilityClass
public class ResourceUriHelper {

	public static void addUriResponseHeader(Object resorceId) {
		
		  //19.2. Adicionando a URI do recurso criado no header da resposta
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
        	.path("/{id}")
        	.buildAndExpand(resorceId).toUri();
        
        HttpServletResponse response = ((ServletRequestAttributes) 
        		RequestContextHolder.getRequestAttributes()).getResponse();
        
        response.setHeader(HttpHeaders.LOCATION, uri.toString());
	}
}
