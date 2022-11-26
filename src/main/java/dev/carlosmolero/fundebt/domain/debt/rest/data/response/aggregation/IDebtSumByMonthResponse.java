package dev.carlosmolero.fundebt.domain.debt.rest.data.response.aggregation;

public interface IDebtSumByMonthResponse {
    Integer getMonth();

    Float getTotal();
}
