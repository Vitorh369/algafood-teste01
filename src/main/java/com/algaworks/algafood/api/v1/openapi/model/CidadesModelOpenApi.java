package com.algaworks.algafood.api.v1.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.v1.model.CozinhaModel;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("CozinhasModel")
@Data
public class CidadesModelOpenApi {
	
	private CozinhasEmbeddedModelOpenApi _embedded;
	private Links _links;
	private PageModelOpenApi page;

	@ApiModel("CozinhassembeddedModel")
	@Data
	public class CozinhasEmbeddedModelOpenApi {
		
		private List<CozinhaModel> cozinhas;
	}
}
