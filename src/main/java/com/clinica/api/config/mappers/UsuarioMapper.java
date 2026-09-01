package com.clinica.api.config.mappers;

import com.clinica.api.dto.request.UsuarioRequest;
import com.clinica.api.dto.response.UsuarioResponse;
import com.clinica.api.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "tentativasFalhas", ignore = true)
    @Mapping(target = "bloqueadoAte", ignore = true)
    Usuario toEntity(UsuarioRequest request);

    UsuarioResponse toResponse(Usuario usuario);
}