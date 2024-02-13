package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.openapi.controller.FluxoPedidoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.service.FluxoPedidoService;

@RestController
@RequestMapping(value = "/v1/pedidos/{codigoPedido}")
public class FluxoPedidoController implements FluxoPedidoControllerOpenApi {

	@Autowired
	private FluxoPedidoService fluxoPedidoService;
	
	@CheckSecurity.Pedidos.PodeGerenciarPedidos
	@PutMapping(value = "/confirmacao", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> confirmacao(@PathVariable String codigoPedido) {
		
		fluxoPedidoService.confirmar(codigoPedido);
		
		return ResponseEntity.noContent().build();
		
	}
	
	@CheckSecurity.Pedidos.PodeGerenciarPedidos
	@PutMapping(value ="/cancela", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> cancelar(@PathVariable String codigoPedido) {
		
		fluxoPedidoService.cancelar(codigoPedido);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Pedidos.PodeGerenciarPedidos
	@PutMapping(value ="/entrega", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> entregar(@PathVariable String codigoPedido) {
	    fluxoPedidoService.entregar(codigoPedido);
	    
	    
	    return ResponseEntity.noContent().build();
	}

}