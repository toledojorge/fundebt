/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.debt.rest.data.request;

import java.time.Instant;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DebtsRequest {
    @NotNull(message = "Debt user id should not be null")
    private Long debtUserId;
    private Instant from;
    private DebtsRequestFilters filters;

    @Data
    public static class DebtsRequestFilters {
        private DEBT_FILTER_SHOW show;
        private String orderBy;
        private String orderDirection;
        private Boolean invert;
    }

    public static enum DEBT_FILTER_SHOW {
        ALL,
        PAID,
        UNPAID
    }
}
