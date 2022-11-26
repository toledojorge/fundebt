/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.debt.rest.data.response;

import java.time.Instant;

import lombok.Data;

@Data
public class DebtResponseMin {
    private Float amount;
    private Float reductionAmount;
    private Instant createdAt;
    private String description;
    private Boolean paid;
}
