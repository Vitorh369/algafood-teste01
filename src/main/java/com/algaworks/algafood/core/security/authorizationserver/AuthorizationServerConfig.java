package com.algaworks.algafood.core.security.authorizationserver;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

//22.8. Criando o projeto do Authorization Server com Spring Security OAuth2
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService detailsService;
	
	@Autowired
	private JwtKeyStoreProperties jwtKeyStoreProperties;
	
	@Autowired
	private DataSource dataSource;
	
//	@Autowired
//	private RedisConnectionFactory connectionFactory;

	//22.9. Configurando o fluxo Authorization Server com Password Credentials e Opaque Tokens
	//configurando detalhes dos clientes
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		
		//configurando cadastro de clientes no banco de dados
		clients.jdbc(dataSource);

	}
	
	//22.10. Configurando o endpoint de introspecção de tokens no Authorization Server
	//configurando acesso do endpoint do acesso ao token
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

		// para a cessar a tem q ser autenticado!. isAuthenticated()"
		//security.checkTokenAccess("isAuthenticated()");
		
		//permiti todos, nao precisa de login e senha.permitAll()
		security.checkTokenAccess("permitAll()")
		.tokenKeyAccess("permitAll()")// libera acessp da chave publica
		.allowFormAuthenticationForClients();
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		//23.16. Adicionando claims customizadas no payload do JWT
		var enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(
				Arrays.asList(new JwtCustomClaimsTokenEnhancer(), jwtAccessTokenConverter()));
		
		endpoints.authenticationManager(authenticationManager)
			.userDetailsService(detailsService)
			.reuseRefreshTokens(false)//para não reutilizar o refresh token. vai gera outro refresh token
			//.tokenStore(redisTokenStore())
			.authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource))
			.accessTokenConverter(jwtAccessTokenConverter())
			.tokenEnhancer(enhancerChain)
			.approvalStore(approvalStore(endpoints.getTokenStore()))
			.tokenGranter(tokenGranter(endpoints));
	}
	
	//23.13. Revisando o fluxo de aprovação do Authorization Code com JWT
	private ApprovalStore approvalStore(TokenStore tokenStore) {
		var approvalStore = new TokenApprovalStore();
		approvalStore.setTokenStore(tokenStore);
		
		return approvalStore;
	}

    //23.45. Implementando o endpoint do JSON Web Key Set (JWKS)
    //para obter chave publica atyras de uri
    @Bean
    JWKSet jwkset() {
		 RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey)keyPair().getPublic())
				 .keyUse(KeyUse.SIGNATURE)
				 .algorithm(JWSAlgorithm.RS256)
				 .keyID("algafood-key-id");
		 
		 return new JWKSet(builder.build());
	}


    //23.5. Gerando JWT com chave simétrica (HMAC SHA-256) no Authorization Server
    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter() {
		var jwtAccessTokenConverter = new JwtAccessTokenConverter();
		//jwtAccessTokenConverter.setSigningKey("dasefwesfwefawdfwf1fe85f4e89f48e9s4f5e7fre588fe4fe7fe8");// chave para criptografia
		
		jwtAccessTokenConverter.setKeyPair(keyPair());
		
		return jwtAccessTokenConverter;
	}
	
	private KeyPair keyPair() {
		//AULA 23.8 e 23.9
		// Assinando o JWT com RSA SHA-256 (chave assimétrica)
		var keyStorePass = jwtKeyStoreProperties.getPassword();// senha do arquivo q foi definido na aula 23.8
		var keyPairAlias = jwtKeyStoreProperties.getKeypairAlias(); // nome do par de chave
		
		var keyStoreKeyFactory = new KeyStoreKeyFactory(jwtKeyStoreProperties.getJkslocation(), 
				keyStorePass.toCharArray());
		return keyStoreKeyFactory.getKeyPair(keyPairAlias);
		
	}
	
	//22.23. Implementando o suporte a PKCE com o fluxo Authorization Code
	private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
		var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
				endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
				endpoints.getOAuth2RequestFactory());
		
		var granters = Arrays.asList(
				pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());
		
		return new CompositeTokenGranter(granters);
	}

}
