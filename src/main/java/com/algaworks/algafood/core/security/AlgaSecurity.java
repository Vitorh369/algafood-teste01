package com.algaworks.algafood.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

//23.17. Obtendo usuário autenticado no Resource Server
@Component
public class AlgaSecurity {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;

	// metodo para pegar o token autenticado
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	//pegando id do usuario
	public Long getUsuarioId() {
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();
		
		return jwt.getClaim("usuario_id");
	}
	
	// 23.28. Restringindo acessos de forma contextual (sensível à informação)
	public boolean gerenciaRestaurante(Long restauranteId) {
		if (restauranteId == null) {
	        return false;
	    }
		return restauranteRepository.existsResponsavel(restauranteId, getUsuarioId());
	}
	
	public boolean gerenciaRestauranteDoPedido(String codigoPedido) {
	    return pedidoRepository.isPedidoGerenciadoPor(codigoPedido, getUsuarioId());
	}    
	
	public boolean usuarioAutenticadoIgual(Long usuarioId) {
		return getUsuarioId() != null && usuarioId != null
				&& getUsuarioId().equals(usuarioId);
	}
	
	public boolean hasAuthority(String authorityName) {
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(authorityName));
	}
	
	//link dinamico para usuario do pedido
	public boolean podeGerenciarPedido(String codigoPedido) {
		return hasAuthority("SCOPE_WRITE") && (hasAuthority("GERENCIAR_PEDIDOS"))
				|| gerenciaRestauranteDoPedido(codigoPedido);
	}
	
	// verificar se o usuário está autenticado
	public boolean isAutenticado() {
	    return getAuthentication().isAuthenticated();
	}
	
	//possui os escopos de escrita
	public boolean temEscopoEscrita() {
	    return hasAuthority("SCOPE_WRITE");
	}

	//possui os escopos de leitura
	public boolean temEscopoLeitura() {
	    return hasAuthority("SCOPE_READ");
	}
	
	public boolean podeConsultarRestaurantes() {
	    return temEscopoLeitura() && isAutenticado();
	}

	public boolean podeGerenciarCadastroRestaurantes() {
	    return temEscopoEscrita() && hasAuthority("EDITAR_RESTAURANTES");
	}

	public boolean podeGerenciarFuncionamentoRestaurantes(Long restauranteId) {
	    return temEscopoEscrita() && (hasAuthority("EDITAR_RESTAURANTES")
	            || gerenciaRestaurante(restauranteId));
	}

	public boolean podeConsultarUsuariosGruposPermissoes() {
	    return temEscopoLeitura() && hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
	}

	public boolean podeEditarUsuariosGruposPermissoes() {
	    return temEscopoEscrita() && hasAuthority("EDITAR_USUARIOS_GRUPOS_PERMISSOES");
	}

	public boolean podePesquisarPedidos(Long clienteId, Long restauranteId) {
	    return temEscopoLeitura() && (hasAuthority("CONSULTAR_PEDIDOS")
	            || usuarioAutenticadoIgual(clienteId) || gerenciaRestaurante(restauranteId));
	}

	public boolean podePesquisarPedidos() {
	    return isAutenticado() && temEscopoLeitura();
	}

	public boolean podeConsultarFormasPagamento() {
	    return isAutenticado() && temEscopoLeitura();
	}

	public boolean podeConsultarCidades() {
	    return isAutenticado() && temEscopoLeitura();
	}

	public boolean podeConsultarEstados() {
	    return isAutenticado() && temEscopoLeitura();
	}

	public boolean podeConsultarCozinhas() {
	    return isAutenticado() && temEscopoLeitura();
	}

	public boolean podeConsultarEstatisticas() {
	    return temEscopoLeitura() && hasAuthority("GERAR_RELATORIOS");
	}    

}








