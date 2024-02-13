package com.algaworks.algafood.api.v2.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
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

import com.algaworks.algafood.api.v2.assembler.CozinhaModelAssemblerV2;
import com.algaworks.algafood.api.v2.disassembler.CozinhaInputDisassemblerV2;
import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;
import com.algaworks.algafood.api.v2.openapi.controller.CozinhaControllerV2OpenApi;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping("/v2/cozinhas")
public class CozinhaControllerV2 implements CozinhaControllerV2OpenApi {

	@Autowired
	private CozinhaModelAssemblerV2 cozinhaModelAssembler;

	@Autowired
	private CozinhaInputDisassemblerV2 cozinhaInputDisassembler;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	@Autowired
	private CozinhaRepository cozinhaRepository;

	// aula 19.15 linka paginacao
	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

	// paginação aula 13.8
	// @PageableDefault: quantidades de pagina
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public PagedModel<CozinhaModelV2> listar(@PageableDefault(size = 10) Pageable pageable) {
		Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);

		// aula 19.15 linka paginacao
		PagedModel<CozinhaModelV2> cozinhasPageModel = pagedResourcesAssembler.toModel(cozinhasPage,
				cozinhaModelAssembler);

		return cozinhasPageModel;

	}

	@GetMapping("/{cozinhaId}")
	public CozinhaModelV2 buscar(@PathVariable Long cozinhaId) {
		Cozinha cozinha = cadastroCozinha.BuscarOuFalhar(cozinhaId);

		return cozinhaModelAssembler.toModel(cozinha);
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED) // vai retorna 201
	public CozinhaModelV2 adicionar(@RequestBody @Valid CozinhaInputV2 cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
		cozinha = cadastroCozinha.salvar(cozinha);

		return cozinhaModelAssembler.toModel(cozinha);
	}

	@PutMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CozinhaModelV2 atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInputV2 cozinhaInput) {
		Cozinha cozinhaAtual = cadastroCozinha.BuscarOuFalhar(cozinhaId);
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		cozinhaAtual = cadastroCozinha.salvar(cozinhaAtual);

		return cozinhaModelAssembler.toModel(cozinhaAtual);
	}

	@DeleteMapping(path = "/{cozinhaId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT) // devolve 204
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinha.excluir(cozinhaId);

	}

}
