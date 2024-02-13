package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.openapi.controller.UsuarioGrupoControllerOpenApi;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(value = "/v1/usuarios/{usuarioId}/grupos")
public class UsuarioGrupoController implements UsuarioGrupoControllerOpenApi{

	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
	
	@Autowired
	private AlgaSecurity algaSecurity;    
	
	private AlgaLinks algaLinks;
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<GrupoModel> listar(@PathVariable Long usuarioId){
		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
	    
	    CollectionModel<GrupoModel> gruposModel = grupoModelAssembler.toCollectionModel(usuario.getGrupos())
	            .removeLinks();
	    
	    if (algaSecurity.podeEditarUsuariosGruposPermissoes()) {
	        gruposModel.add(algaLinks.linkToGrupoPermissaoAssociacao(usuarioId, "associar"));
	        
	        gruposModel.getContent().forEach(grupoModel -> {
	            grupoModel.add(algaLinks.linkToGrupoPermissaoDesassociacao(
	                    usuarioId, grupoModel.getId(), "desassociar"));
	        });
	    }
	    
	    return gruposModel;
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@DeleteMapping(value = "/{grupoId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		cadastroUsuarioService.desassociarGrupo(usuarioId, grupoId);
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@PutMapping(value = "/{grupoId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		cadastroUsuarioService.associarGrupo(usuarioId, grupoId);
	}
	
}











