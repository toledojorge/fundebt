package dev.carlosmolero.fundebt.domain.debt.rest.data.response.aggregation;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DebtSumByMonthBidirectionalHolder {
    private List<IDebtSumByMonthResponse> owedToPrincipal;
    private List<IDebtSumByMonthResponse> owedByPrincipal;
}
