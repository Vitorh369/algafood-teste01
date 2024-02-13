package com.algaworks.algafood.api;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//24.16. Entendendo o Poor Man's Load Balancer (DNS Round Robin)
@RestController
public class HostCheckeController {

	//metodo vai servir para verificar o host de cada container quando subir. 
	//como algafood-api1 e algafood-api2. para diferenciar um do outro
	@GetMapping("/hostcheck")
	public String checkHost() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress()
				+ " - "  + InetAddress.getLocalHost().getHostName();
	}
}
