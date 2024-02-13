package com.algaworks.algafood.api.v2.disassembler;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class CidadeInputDisassemblerV2 {

    @Autowired
    private ModelMapper modelMapper;
    
    public Cidade toDomainObject(@Valid CidadeInputV2 cidadeInputV2) {
        return modelMapper.map(cidadeInputV2, Cidade.class);
    }
    
    public void copyToDomainObject(@Valid CidadeInputV2 cidadeInputV2, Cidade cidade) {
        // Para evitar org.hibernate.HibernateException: identifier of an instance of 
        // com.algaworks.algafood.domain.model.Estado was altered from 1 to 2
        cidade.setEstado(new Estado());
        
        modelMapper.map(cidadeInputV2, cidade);
    }
    
}