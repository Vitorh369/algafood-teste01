package com.algaworks.algafood.api.v2.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v2.assembler.CidadeModelAssemblerV2;
import com.algaworks.algafood.api.v2.disassembler.CidadeInputDisassemblerV2;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.api.v2.openapi.controller.CidadeControllerV2OpenApi;
import com.algaworks.algafood.domain.exception.EstadoNaoEncotradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

import io.swagger.annotations.ApiParam;

//20.11. Implementando o versionamento da API por Media Type
@RestController
@RequestMapping(path = "/v2/cidades")
public class CidadeControllerV2 implements CidadeControllerV2OpenApi{

	@Autowired
	private CidadeModelAssemblerV2 cidadeModelAssembler;

	@Autowired
	private CidadeInputDisassemblerV2 cidadeInputDisassembler; 
	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidade;

	//20.10. Preparando o projeto para versionamento da API por Media Type
	//V1_APPLICATION_JSON_VALUE para versao da api
	@GetMapping(produces =  MediaType.APPLICATION_JSON_VALUE)	
	public CollectionModel<CidadeModelV2> listar() {
	    List<Cidade> todasCidades = cidadeRepository.findAll();
	   
	    return cidadeModelAssembler.toCollectionModel(todasCidades);
	
	}

	@GetMapping(path = "/{cidadeId}", produces =  MediaType.APPLICATION_JSON_VALUE)
	public CidadeModelV2 buscar(@PathVariable Long cidadeId) {
	    Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
	    
	   return cidadeModelAssembler.toModel(cidade);
	}

	
	@PostMapping(produces =  MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModelV2 adicionar(@RequestBody @Valid CidadeInputV2 cidadeInputV2) {
	    try {
	        Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInputV2);
	        
	        cidade = cadastroCidade.salvar(cidade);
	        
	        CidadeModelV2 cidadeModelV2 = cidadeModelAssembler.toModel(cidade);

	      //19.2. Adicionando a URI do recurso criado no header da resposta
	        ResourceUriHelper.addUriResponseHeader(cidadeModelV2.getIdCidade());
	        
	        return cidadeModelV2;
	    } catch (EstadoNaoEncotradaException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}

	
	@PutMapping(path = "/{cidadeId}", produces =  MediaType.APPLICATION_JSON_VALUE)
	public CidadeModelV2 atualizar(@PathVariable Long cidadeId,
			@ApiParam(name ="corpo", value ="representação de uma cidade com os novos dados")
	        @RequestBody @Valid CidadeInputV2 cidadeInputV2) {
	    try {
	        Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);
	        
	        cidadeInputDisassembler.copyToDomainObject(cidadeInputV2, cidadeAtual);
	        
	        cidadeAtual = cadastroCidade.salvar(cidadeAtual);
	        
	        return cidadeModelAssembler.toModel(cidadeAtual);
	    } catch (EstadoNaoEncotradaException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}

	@DeleteMapping(path = "/{cidadeId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT) // devolve 204
	public void remover(@ApiParam(value ="ID de uma cidade")@PathVariable Long cidadeId) {
		cadastroCidade.excluir(cidadeId);
	}







}
