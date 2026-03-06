package com.flow.repository;

import com.flow.entity.CustomExtension;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomExtensionRepository extends JpaRepository<CustomExtension, Long> {

    boolean existsByName(String name);

    List<CustomExtension> findAllByOrderByCreatedAtAsc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM CustomExtension e")
    List<CustomExtension> findAllWithLock();

    @Modifying
    @Query("DELETE FROM CustomExtension c WHERE c.id = :id")
    int deleteByIdIfExists(@Param("id") Long id);
}
