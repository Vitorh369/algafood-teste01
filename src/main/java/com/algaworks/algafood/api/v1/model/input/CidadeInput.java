package com.algaworks.algafood.api.v1.model.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class CidadeInput {

	@ApiModelProperty(example = "Uberlândia", required = true)
	@NotBlank
	private String nome;
	
	@Valid
	@NonNull
	private EstadoIdInput estado;
}
