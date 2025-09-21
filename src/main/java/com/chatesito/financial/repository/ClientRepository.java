package com.chatesito.financial.repository;

import com.chatesito.financial.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
