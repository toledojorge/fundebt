/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.debt.rest;

import java.util.List;

import org.mapstruct.Mapper;

import dev.carlosmolero.fundebt.domain.debt.DebtEntity;
import dev.carlosmolero.fundebt.domain.debt.rest.data.response.DebtResponse;

@Mapper
public interface DebtRestMapper {
    List<DebtResponse> fromDebtEntityListToDebtListResponse(List<DebtEntity> debts);

    DebtResponse fromDebtEntityToDebtResponse(DebtEntity debt);
}
