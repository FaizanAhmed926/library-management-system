package com.library.library_backend.repository;

import com.library.library_backend.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan,Long> {
    Boolean existsByPlanCode(String planCode);
    SubscriptionPlan findByPlanCode(String planCode);
}
