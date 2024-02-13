package com.algaworks.algafood.api.v1.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.v1.disassemblers.CozinhaInputDisassembler;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.api.v1.openapi.controller.CozinhaControllerOPenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

//@Slf4j anotacao para log
//@Slf4j
@RestController
@RequestMapping("/v1/cozinhas")
public class CozinhaController implements CozinhaControllerOPenApi{
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;

	@Autowired
	private CozinhaInputDisassembler cozinhaInputDisassembler;       

	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	//aula 19.15 linka paginacao
	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

	//paginação aula 13.8
	//@PageableDefault: quantidades de pagina
	//@PreAuthorize("isAuthenticated()")//aula 23.21. fazer uma pre autorizacao. antes de executar o metodo listar vai verificar se é verdadeira
	@CheckSecurity.Cozinhas.PodeConsultar
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public PagedModel<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable) {
		//System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		
		//21.1. Introdução ao Logback e SLF4J
//		log.info("consultando cozinhas com páginas de {} registros...", pageable.getPageSize());
//		
//		if (true) {
//			throw new RuntimeException("Teste de exception");
//		}
		
	    Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);
	    
	  //aula 19.15 linka paginacao
	    PagedModel<CozinhaModel> cozinhasPageModel = pagedResourcesAssembler
	    		.toModel(cozinhasPage, cozinhaModelAssembler);
	    
	    return cozinhasPageModel;
	    		
	}

//	@GetMapping("/{cozinhaId}")
//	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
//		Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);
//		if (cozinha.isPresent()) {// esta presente
//			return ResponseEntity.ok(cozinha.get()); // reotrna 200
//		}
//		return ResponseEntity.notFound().build(); // retorna 404
//
//	}
	
//	@GetMapping("/{cozinhaId}")
//	public Cozinha buscar(@PathVariable Long cozinhaId) {
//		return cozinhaRepository.findById(cozinhaId)
//				.orElseThrow(() -> new EntidadeNaoEncotradaException("a"));// vai retorna 200 se não tiver cozinha retorna 404
//	}
	
	//@PreAuthorize("isAuthenticated()")
	@CheckSecurity.Cozinhas.PodeConsultar
	@GetMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
		Cozinha cozinha = cadastroCozinha.BuscarOuFalhar(cozinhaId);// vai retorna 200, se não tiver cozinha retorna 404
		 return cozinhaModelAssembler.toModel(cozinha);
	}
	
	//@PreAuthorize("hasAuthority('EDITAR_COZINHAS')")//autorizando edicao de cozinhas
	@CheckSecurity.Cozinhas.PodeEditar
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED) // vai retorna 201
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
	    Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
	    cozinha = cadastroCozinha.salvar(cozinha);
	    
	    return cozinhaModelAssembler.toModel(cozinha);
	}

	@CheckSecurity.Cozinhas.PodeEditar
	@PutMapping(path = "/{cozinhaId}",produces = MediaType.APPLICATION_JSON_VALUE)
	public CozinhaModel atualizar(@PathVariable Long cozinhaId,
	        @RequestBody @Valid CozinhaInput cozinhaInput) {
	    Cozinha cozinhaAtual = cadastroCozinha.BuscarOuFalhar(cozinhaId);
	    cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
	    cozinhaAtual = cadastroCozinha.salvar(cozinhaAtual);
	    
	    return cozinhaModelAssembler.toModel(cozinhaAtual);
	}
//
//	@DeleteMapping("/{cozinhaId}")
//	public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId) {
//		try {
//			cadastroCozinha.excluir(cozinhaId);
//			return ResponseEntity.noContent().build(); // devolve 204
//
//		} catch (EntidadeNaoEncotradaException e) {
//			return ResponseEntity.notFound().build(); // retorna 404 não encotrado
//
//		} catch (EntidadeEmUsoException e) {
//			return ResponseEntity.status(HttpStatus.CONFLICT).build(); // retorna 409, nãp ode deleta o recurso
//		}
//	}

	@CheckSecurity.Cozinhas.PodeEditar
	@DeleteMapping(path = "/{cozinhaId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT) // devolve 204
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinha.excluir(cozinhaId);

	}

//	@DeleteMapping("/{cozinhaId}")
//	@ResponseStatus(HttpStatus.NO_CONTENT) // devolve 204
//	public void remover(@PathVariable Long cozinhaId) {
//		try {
//			cadastroCozinha.excluir(cozinhaId);
//		} catch (EntidadeNaoEncotradaException e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//		}
//
//	}

}
