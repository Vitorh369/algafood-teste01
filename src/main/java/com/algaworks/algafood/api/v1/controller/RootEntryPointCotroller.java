package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.core.security.AlgaSecurity;

import springfox.documentation.annotations.ApiIgnore;

//  liks de innnicializacao de navegacao. catalago de links
@ApiIgnore
@RestController
@RequestMapping(path = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class RootEntryPointCotroller {
	
	@Autowired
	private AlgaSecurity algaSecurity; 
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@GetMapping
	public RootEntryPoinModel root() {
	    var rootEntryPointModel = new RootEntryPoinModel();
	    
	    if (algaSecurity.podeConsultarCozinhas()) {
	        rootEntryPointModel.add(algaLinks.linkToCozinhas("cozinhas"));
	    }
	    
	    if (algaSecurity.podePesquisarPedidos()) {
	        rootEntryPointModel.add(algaLinks.linkToPedidos("pedidos"));
	    }
	    
	    if (algaSecurity.podeConsultarRestaurantes()) {
	        rootEntryPointModel.add(algaLinks.linkToRestaurantes("restaurantes"));
	    }
	    
	    if (algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
	        rootEntryPointModel.add(algaLinks.linkToGrupos("grupos"));
	        rootEntryPointModel.add(algaLinks.linkToUsuarios("usuarios"));
	        rootEntryPointModel.add(algaLinks.linkToPermissoes("permissoes"));
	    }
	    
	    if (algaSecurity.podeConsultarFormasPagamento()) {
	        rootEntryPointModel.add(algaLinks.linkToFormasPagamento("formas-pagamento"));
	    }
	    
	    if (algaSecurity.podeConsultarEstados()) {
	        rootEntryPointModel.add(algaLinks.linkToEstados("estados"));
	    }
	    
	    if (algaSecurity.podeConsultarCidades()) {
	        rootEntryPointModel.add(algaLinks.linkToCidades("cidades"));
	    }
	    
	    if (algaSecurity.podeConsultarEstatisticas()) {
	        rootEntryPointModel.add(algaLinks.linkToEstatisticas("estatisticas"));
	    }
	    
	    return rootEntryPointModel;
	}
	

	private static class RootEntryPoinModel extends RepresentationModel<RootEntryPoinModel>{
		
	}
}
