package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The UserRole repository。
 */
public interface UserRoleRepository extends JpaRepository<UserRole,Long>, JpaSpecificationExecutor<UserRole> {
}

