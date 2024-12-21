package com.zahid.socks_api.repositories;

import com.zahid.socks_api.entity.SocksBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SocksRepository extends JpaRepository<SocksBatch, Integer> {
    Optional<SocksBatch> findSocksBatchByColorAndCotton(String color, int cotton);
}
