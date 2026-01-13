package com.library.library_backend.service.impl;

import com.library.library_backend.exception.SubscriptionException;
import com.library.library_backend.mapper.SubscriptionMapper;
import com.library.library_backend.model.Subscription;
import com.library.library_backend.model.SubscriptionPlan;
import com.library.library_backend.model.User;
import com.library.library_backend.payload.dto.SubscriptionDTO;
import com.library.library_backend.repository.SubscriptionPlanRepository;
import com.library.library_backend.repository.SubscriptionRepository;
import com.library.library_backend.service.SubscriptionService;
import com.library.library_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private  final UserService userService;

    @Override
    public SubscriptionDTO subscribe(SubscriptionDTO subscriptionDTO) throws Exception {
        User user=userService.getCurrentUser();
        SubscriptionPlan plan=subscriptionPlanRepository.findById(subscriptionDTO.getPlanId()).orElseThrow(
                ()->new  Exception("Plan not found")
        );
        Subscription subscription=subscriptionMapper.toEntity(subscriptionDTO);
        subscription.initializeFromPlan();
        subscription.setIsActive(false);
        Subscription savedSubscription=subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(savedSubscription);
    }

    @Override
    public SubscriptionDTO getUsersActiveSubscription(Long userId) throws Exception {
        User user=userService.getCurrentUser();

        Subscription subscription=subscriptionRepository
                .findActiveSubscriptionByUserId(user.getId(), LocalDate.now())
                .orElseThrow(()->new SubscriptionException("No active Subscription Found!"));
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException {
        Subscription subscription=subscriptionRepository.findById(subscriptionId)
                .orElseThrow(()->new  SubscriptionException("Subscription not found with ID: "+ subscriptionId));
        if (!subscription.getIsActive()){
            throw  new SubscriptionException("Subscription is already inactive");
        }

        subscription.setIsActive(false);
        subscription.setCancelledAt(LocalDateTime.now());
        subscription.setCancellationReason(reason!=null ? reason : "Cancelled by user");
        subscription=subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException {
        Subscription subscription=subscriptionRepository.findById(subscriptionId)
                .orElseThrow(
                        ()->new SubscriptionException("subscription not found by id!")
                );
        subscription.setIsActive(true);
        subscription=subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public List<SubscriptionDTO> getAllSubscriptions(Pageable pageable) {
        List<Subscription> subscriptions=subscriptionRepository.findAll();
        return subscriptionMapper.toDTOList(subscriptions);
    }

    @Override
    public void deactivateExpiredSubscriptions() throws Exception{
        List<Subscription> expiredSubscriptions=subscriptionRepository
                .findExpiredActiveSubscriptions(LocalDate.now());
        for (Subscription subscription:expiredSubscriptions){
            subscription.setIsActive(false);
            subscriptionRepository.save(subscription);
        }
    }
}
