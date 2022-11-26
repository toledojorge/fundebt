package dev.carlosmolero.fundebt.domain.debt.rest.data.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DebtSumByMonthRequest {
    @NotNull(message = "debt user id cannot be null")
    private Long debtUserId;
}
