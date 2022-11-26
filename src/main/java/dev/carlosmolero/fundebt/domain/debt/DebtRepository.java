/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.debt;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.carlosmolero.fundebt.domain.debt.rest.data.response.aggregation.IDebtSumByMonthResponse;
import dev.carlosmolero.fundebt.domain.user.UserEntity;

@Repository
public interface DebtRepository extends JpaRepository<DebtEntity, Long> {

        List<DebtEntity> findByUserAndDebtUser(UserEntity user, UserEntity debtUser, PageRequest request);

        List<DebtEntity> findByUserAndDebtUserAndPaid(UserEntity user, UserEntity debtUser, boolean paid,
                        PageRequest request);

        List<DebtEntity> findByUserAndDebtUserAndPaid(UserEntity user, UserEntity debtUser, boolean paid);

        @Query(value = "SELECT SUM(d.amount) FROM debts d WHERE d.user_id = :userId AND d.debt_user_id = :debtUserId AND paid = false", nativeQuery = true)
        Optional<Float> findSum(@Param("userId") Long userId, @Param("debtUserId") Long debtUserId);

        @Query(value = "SELECT extract(MONTH from created_at) AS month,sum(initial_amount) AS total FROM debts d WHERE year(created_at) = :year AND user_id = :userId AND debt_user_id = :debtUserId GROUP BY month", nativeQuery = true)
        List<IDebtSumByMonthResponse> findSumByYearGroupedByMonth(@Param("year") Integer year,
                        @Param("userId") Long userId,
                        @Param("debtUserId") Long debtUserId);
}
