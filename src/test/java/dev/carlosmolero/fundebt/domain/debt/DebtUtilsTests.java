/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.debt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DebtUtilsTests {
    @Autowired
    DebtUtils debtUtils;

    @Test
    public void shouldMarkAsPaidOrReduceAmountWhenCreatedDebtIsEqual() {
        DebtEntity createdDebt = DebtEntity.builder().amount(15f).build();

        var userDebts = new ArrayList<DebtEntity>();
        userDebts.add(DebtEntity.builder().amount(5f).build());
        userDebts.add(DebtEntity.builder().amount(2f).build());
        userDebts.add(DebtEntity.builder().amount(5f).build());
        userDebts.add(DebtEntity.builder().amount(3f).build());

        float substractionAmount = createdDebt.getAmount();

        for (int i = 0; i < userDebts.size(); i++) {
            var debtCancellationResult = debtUtils
                    .getDebtCancellation(substractionAmount, userDebts.get(i), createdDebt);

            substractionAmount = debtCancellationResult.getModifiedSubstractionAmount();
            createdDebt = debtCancellationResult.getModifiedCreatedDebt();
        }

        assertEquals(substractionAmount, 0f);
        assertEquals(createdDebt.getPaid(), true);
        assertEquals(createdDebt.getAmount(), 3f);
        assertEquals(createdDebt.getReductionAmount(), 12f);
    }

    @Test
    public void shouldMarkAsPaidOrReduceAmountWhenCreatedDebtIsLess() {
        DebtEntity createdDebt = DebtEntity.builder().amount(4f).build();

        var userDebts = new ArrayList<DebtEntity>();
        userDebts.add(DebtEntity.builder().amount(5f).build());
        userDebts.add(DebtEntity.builder().amount(2f).build());
        userDebts.add(DebtEntity.builder().amount(5f).build());
        userDebts.add(DebtEntity.builder().amount(3f).build());

        float substractionAmount = createdDebt.getAmount();

        for (int i = 0; i < userDebts.size(); i++) {
            var debtCancellationResult = debtUtils
                    .getDebtCancellation(substractionAmount, userDebts.get(i), createdDebt);

            substractionAmount = debtCancellationResult.getModifiedSubstractionAmount();
            createdDebt = debtCancellationResult.getModifiedCreatedDebt();
        }

        assertEquals(substractionAmount, 0f);
        assertEquals(userDebts.get(0).getAmount(), 1f);
        assertEquals(createdDebt.getPaid(), true);
        assertEquals(createdDebt.getAmount(), 4f);
        assertEquals(createdDebt.getReductionAmount(), 0f);
    }

    @Test
    public void shouldMarkAsPaidOrReduceAmountWhenCreatedDebtIsMore() {
        DebtEntity createdDebt = DebtEntity.builder().amount(20f).build();

        var userDebts = new ArrayList<DebtEntity>();
        userDebts.add(DebtEntity.builder().amount(5f).build());
        userDebts.add(DebtEntity.builder().amount(2f).build());
        userDebts.add(DebtEntity.builder().amount(5f).build());
        userDebts.add(DebtEntity.builder().amount(3f).build());

        float substractionAmount = createdDebt.getAmount();

        for (int i = 0; i < userDebts.size(); i++) {
            var debtCancellationResult = debtUtils
                    .getDebtCancellation(substractionAmount, userDebts.get(i), createdDebt);

            substractionAmount = debtCancellationResult.getModifiedSubstractionAmount();
            createdDebt = debtCancellationResult.getModifiedCreatedDebt();
        }

        assertEquals(substractionAmount, 5f);
        assertEquals(createdDebt.getPaid(), false);
        assertEquals(createdDebt.getAmount(), 5f);
        assertEquals(createdDebt.getReductionAmount(), 15f);
    }
}
