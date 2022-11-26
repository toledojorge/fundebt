/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.debt;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dev.carlosmolero.fundebt.domain.debt.rest.data.request.DebtsRequest.DebtsRequestFilters;
import dev.carlosmolero.fundebt.domain.debt.rest.data.response.aggregation.IDebtSumByMonthResponse;
import dev.carlosmolero.fundebt.domain.user.UserEntity;
import dev.carlosmolero.fundebt.domain.user.UserRepository;
import dev.carlosmolero.fundebt.utils.FormatUtils;

@Service
@Transactional
public class DebtServiceImpl implements IDebtService {
    @Autowired
    private DebtRepository debtRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DebtUtils debtUtils;
    @Autowired
    private FormatUtils formatUtils;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "balance", allEntries = true),
            @CacheEvict(value = "paginatedDebts", allEntries = true),
            @CacheEvict(value = "debtsSumByMonth", allEntries = true)
    })
    public DebtEntity create(Long userId, Long debtUserId, Float amount, String description) {
        UserEntity user = userRepository.getReferenceById(userId);
        UserEntity debtUser = userRepository.getReferenceById(debtUserId);

        DebtEntity createdDebt = debtRepository
                .save(DebtEntity.builder().initialAmount(amount).amount(amount).description(description).user(user)
                        .debtUser(debtUser)
                        .build());

        List<DebtEntity> userDebts = debtRepository.findByUserAndDebtUserAndPaid(debtUser, user, false);
        if (userDebts.size() > 0) {
            float substractionAmount = amount;
            for (DebtEntity debt : userDebts) {
                var result = debtUtils.getDebtCancellation(substractionAmount,
                        debt,
                        createdDebt);

                createdDebt = result.getModifiedCreatedDebt();
                debt = result.getModifiedDebt();
                substractionAmount = result.getModifiedSubstractionAmount();
                debtRepository.save(debt);
            }
        }
        return createdDebt;
    }

    @Override
    @Cacheable("paginatedDebts")
    public List<DebtEntity> getWithPagination(Long debtUserId, Long userId, DebtsRequestFilters filters) {
        UserEntity debtUser = userRepository.getReferenceById(debtUserId);
        UserEntity user = userRepository.getReferenceById(userId);
        Sort sort = filters.getOrderDirection().equals("asc") ? Sort.by(filters.getOrderBy()).ascending()
                : Sort.by(filters.getOrderBy()).descending();
        PageRequest request = PageRequest.of(0, 10, sort);

        UserEntity userParam1;
        UserEntity userParam2;

        if (filters.getInvert()) {
            userParam1 = user;
            userParam2 = debtUser;
        } else {
            userParam1 = debtUser;
            userParam2 = user;
        }

        switch (filters.getShow()) {
            case ALL:
                return debtRepository.findByUserAndDebtUser(userParam1, userParam2,
                        request);
            case PAID:
                return debtRepository.findByUserAndDebtUserAndPaid(userParam1, userParam2, true,
                        request);
            case UNPAID:
                return debtRepository.findByUserAndDebtUserAndPaid(userParam1, userParam2, false,
                        request);
            default:
                return null;
        }
    }

    @Override
    @Cacheable("balance")
    public Float getBalance(Long userId, Long debtUserId) {
        float userUnpaidDebts = debtRepository.findSum(debtUserId, userId).orElse(0.0f);
        float debtUserUnpaidDebts = debtRepository.findSum(userId, debtUserId).orElse(0.0f);
        float balance = -userUnpaidDebts + debtUserUnpaidDebts;
        return formatUtils.truncate2Decimals(balance);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "balance", allEntries = true),
            @CacheEvict(value = "paginatedDebts", allEntries = true),
            @CacheEvict(value = "debtsSumByMonth", allEntries = true)
    })
    public Boolean markAsPaid(ArrayList<Long> ids) {
        Iterable<DebtEntity> debts = debtRepository.findAllById((Iterable<Long>) ids);
        for (DebtEntity debt : debts) {
            debt.setPaid(true);
            debtRepository.save(debt);
        }
        return true;
    }

    @Override
    @Cacheable("debtsSumByMonth")
    public List<IDebtSumByMonthResponse> getSumByYearGroupedByMonth(Integer year, Long userId, Long debtUserId) {
        return debtRepository.findSumByYearGroupedByMonth(year, userId, debtUserId);
    }

}
