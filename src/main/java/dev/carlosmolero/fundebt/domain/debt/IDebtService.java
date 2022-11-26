
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

import dev.carlosmolero.fundebt.domain.debt.rest.data.request.DebtsRequest.DebtsRequestFilters;
import dev.carlosmolero.fundebt.domain.debt.rest.data.response.aggregation.IDebtSumByMonthResponse;

public interface IDebtService {
    public DebtEntity create(Long userId, Long debtUserId, Float amount, String description);

    public List<DebtEntity> getWithPagination(Long userId, Long debtUserId, DebtsRequestFilters filters);

    public Float getBalance(Long userId, Long debtUserId);

    public List<IDebtSumByMonthResponse> getSumByYearGroupedByMonth(Integer year, Long userId, Long debtUserId);

    public Boolean markAsPaid(ArrayList<Long> ids);
}
