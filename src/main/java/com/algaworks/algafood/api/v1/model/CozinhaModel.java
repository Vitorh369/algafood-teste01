package com.algaworks.algafood.api.v1.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "cozinhas")
@Getter
@Setter
public class CozinhaModel extends RepresentationModel<CozinhaModel>{

	@ApiModelProperty(example = "1")
	//(RestauranteView.resumo.class)
	private Long id;
	
	@ApiModelProperty(example = "Brasileira")
	//@JsonView(RestauranteView.resumo.class)
	private String nome;
}
