package com.algaworks.algafood.core.io;

import java.util.Base64;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

//23.46. Externalizando o KeyStore: criando um ProtocolResolver para Base64
//class para chave privada criptograda 
@Component
public class Base64ProtocolResolver implements ProtocolResolver,
		ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
		configurableApplicationContext.addProtocolResolver(this);
	}

	@Override
	public Resource resolve(String location, ResourceLoader resourceLoader) {
		if (location.startsWith("base64:")) {
			byte[] decodedResource = Base64.getDecoder().decode(location.substring(7));
			return new ByteArrayResource(decodedResource);
		}

		return null;
	}

}
