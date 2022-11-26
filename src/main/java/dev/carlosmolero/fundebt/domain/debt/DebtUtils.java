/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.debt;

import org.springframework.stereotype.Component;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
public class DebtUtils {
    public DebtCancellationResult getDebtCancellation(float substractionAmount, DebtEntity debt,
            DebtEntity createdDebt) {
        var result = new DebtCancellationResult();
        DebtEntity modifiedDebt = debt;
        DebtEntity modifiedCreatedDebt = createdDebt;
        float modifiedSubstractionAmount = substractionAmount;

        if (Float.compare(substractionAmount, 0) > 0) {
            // ! If my debt as user is equal to the debt I'm creating
            if (Float.compare(debt.getAmount(), substractionAmount) == 0) {
                modifiedDebt.setPaid(true);
                modifiedCreatedDebt.setPaid(true);
                modifiedSubstractionAmount -= debt.getAmount();
                // ! If my debt as user is greater that the debt I'm creating
            } else if (Float.compare(debt.getAmount(), substractionAmount) > 0) {
                float newDebtAmount = debt.getAmount() - substractionAmount;
                modifiedSubstractionAmount = 0f;
                modifiedDebt.setAmount(newDebtAmount);
                modifiedDebt.setReductionAmount(modifiedDebt.getReductionAmount() + substractionAmount);
                modifiedCreatedDebt.setPaid(true);
                // ! If my debt as user is less than the debt I'm creating
            } else if (Float.compare(debt.getAmount(), substractionAmount) < 0) {
                modifiedDebt.setPaid(true);
                float newCreatedDebtAmount = createdDebt.getAmount() - debt.getAmount();
                modifiedCreatedDebt.setAmount(newCreatedDebtAmount);
                modifiedCreatedDebt.setReductionAmount(modifiedCreatedDebt.getReductionAmount() + debt.getAmount());
                modifiedSubstractionAmount -= debt.getAmount();
            }
        }

        result.setModifiedSubstractionAmount(modifiedSubstractionAmount);
        result.setModifiedCreatedDebt(modifiedCreatedDebt);
        result.setModifiedDebt(modifiedDebt);

        return result;
    }

    @Data
    @NoArgsConstructor
    public static class DebtCancellationResult {
        private float modifiedSubstractionAmount;
        private DebtEntity modifiedDebt;
        private DebtEntity modifiedCreatedDebt;
    }
}