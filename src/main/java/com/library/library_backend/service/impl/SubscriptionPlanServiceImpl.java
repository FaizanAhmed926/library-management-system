package com.library.library_backend.service.impl;

import com.library.library_backend.mapper.SubscriptionPlanMapper;
import com.library.library_backend.model.SubscriptionPlan;
import com.library.library_backend.model.User;
import com.library.library_backend.payload.dto.SubscriptionPlanDTO;
import com.library.library_backend.repository.SubscriptionPlanRepository;
import com.library.library_backend.service.SubscriptionPlanService;
import com.library.library_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository planRepository;
    private  final SubscriptionPlanMapper planMapper;
    private final UserService userService;

    @Override
    public SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO planDTO) throws Exception {
        if (planRepository.existsByPlanCode(planDTO.getPlanCode())){
            throw new Exception("Plan Code is Already exist");
        }
        SubscriptionPlan plan=planMapper.toEntity(planDTO);
        User currentUser=userService.getCurrentUser();
        plan.setCreatedBy(currentUser.getFullName());
        plan.setUpdatedBy(currentUser.getFullName() );
        SubscriptionPlan savedPlan=planRepository.save(plan);


        return  planMapper.toDTO(savedPlan);
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlan(Long planId, SubscriptionPlanDTO planDTO) throws Exception {
        SubscriptionPlan existingPlan=planRepository.findById(planId).orElseThrow(
                ()->new Exception("Plan not found")
        );
        planMapper.UpdateEntity(existingPlan,planDTO);
        User currentUser=userService.getCurrentUser();
        existingPlan.setUpdatedBy(currentUser.getFullName());
        SubscriptionPlan updatedPlan=planRepository.save(existingPlan);
        return planMapper.toDTO(updatedPlan);
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) throws Exception {
        SubscriptionPlan existingPlan=planRepository.findById(planId).orElseThrow(
                ()->new Exception("Plan not found")
        );
        planRepository.delete(existingPlan);
    }

    @Override
    public List<SubscriptionPlanDTO> getAllSubscriptionPlan() {
        List<SubscriptionPlan> planList=planRepository.findAll();

        return planList.stream().map(
                planMapper::toDTO
        ).collect(Collectors.toList());
    }
}
