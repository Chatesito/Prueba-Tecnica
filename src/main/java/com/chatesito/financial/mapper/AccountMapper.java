package com.chatesito.financial.mapper;

import com.chatesito.financial.dto.account.AccountRequestDTO;
import com.chatesito.financial.dto.account.AccountResponseDTO;
import com.chatesito.financial.entity.Account;
import com.chatesito.financial.entity.Client;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    // Create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "balance", expression = "java(source.getInitialBalance() == null ? 0d : source.getInitialBalance())")
    @Mapping(target = "client", source = "client")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Account toEntity(AccountRequestDTO source, Client client);

    // Update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(AccountRequestDTO source, @MappingTarget Account target, Client client);

    // Response
    @Mapping(target = "clientId", source = "client.id")
    AccountResponseDTO toResponseDTO(Account source);

    List<AccountResponseDTO> toResponseDTO(List<Account> source);
}