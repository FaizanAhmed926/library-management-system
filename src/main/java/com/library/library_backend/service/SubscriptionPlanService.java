package com.library.library_backend.service;

import com.library.library_backend.model.SubscriptionPlan;
import com.library.library_backend.payload.dto.SubscriptionPlanDTO;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO planDTO) throws Exception;

    SubscriptionPlanDTO updateSubscriptionPlan(Long planId , SubscriptionPlanDTO planDTO) throws Exception;

    void deleteSubscriptionPlan(Long planId) throws Exception;

    List<SubscriptionPlanDTO> getAllSubscriptionPlan();

    SubscriptionPlan getBySubscriptionPlanCode(String subscriptionPlanCode) throws Exception;
}
