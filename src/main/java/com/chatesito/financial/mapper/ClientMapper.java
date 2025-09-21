package com.chatesito.financial.mapper;

import com.chatesito.financial.dto.client.ClientRequestDTO;
import com.chatesito.financial.dto.client.ClientResponseDTO;
import com.chatesito.financial.entity.Client;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Client toEntity(ClientRequestDTO source);

    ClientResponseDTO toResponseDTO(Client source);

    List<ClientResponseDTO> toResponseDTO(List<Client> source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ClientRequestDTO source, @MappingTarget Client target);
}