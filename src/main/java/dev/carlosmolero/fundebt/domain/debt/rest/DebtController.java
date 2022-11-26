/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.debt.rest;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.carlosmolero.fundebt.domain.debt.DebtEntity;
import dev.carlosmolero.fundebt.domain.debt.DebtServiceImpl;
import dev.carlosmolero.fundebt.domain.debt.rest.data.request.DebtBalanceRequest;
import dev.carlosmolero.fundebt.domain.debt.rest.data.request.DebtSumByMonthRequest;
import dev.carlosmolero.fundebt.domain.debt.rest.data.request.DebtCreateRequest;
import dev.carlosmolero.fundebt.domain.debt.rest.data.request.DebtsRequest;
import dev.carlosmolero.fundebt.domain.debt.rest.data.request.DebtsRequest.DebtsRequestFilters;
import dev.carlosmolero.fundebt.domain.debt.rest.data.response.DebtResponse;
import dev.carlosmolero.fundebt.domain.debt.rest.data.response.aggregation.DebtSumByMonthBidirectionalHolder;
import dev.carlosmolero.fundebt.domain.debt.rest.data.response.aggregation.IDebtSumByMonthResponse;

@RequestMapping("/api/debt")
@RestController()
public class DebtController {
        @Autowired
        private DebtServiceImpl debtService;
        @Autowired
        private DebtRestMapper debtRestMapper;

        @PostMapping("/balance")
        public ResponseEntity<Float> balance(@RequestBody DebtBalanceRequest debtBalanceRequest,
                        @RequestAttribute Long userId) {
                float balance = debtService.getBalance(userId, debtBalanceRequest.getDebtUserId());
                return ResponseEntity.ok(balance);
        }

        @PostMapping("/sum/byMonth")
        public ResponseEntity<DebtSumByMonthBidirectionalHolder> chart(
                        @RequestBody DebtSumByMonthRequest debtChartRequest,
                        @RequestAttribute("userId") Long userId) {
                Integer year = Year.now().getValue();
                Long debtUserId = debtChartRequest.getDebtUserId();
                List<IDebtSumByMonthResponse> owedToPrincipal = debtService.getSumByYearGroupedByMonth(year,
                                userId,
                                debtUserId);
                List<IDebtSumByMonthResponse> owedByPrincipal = debtService.getSumByYearGroupedByMonth(year,
                                debtUserId,
                                userId);
                var debtSumByMonthBidirectionalHolder = DebtSumByMonthBidirectionalHolder.builder()
                                .owedByPrincipal(owedByPrincipal).owedToPrincipal(owedToPrincipal).build();
                return ResponseEntity.ok(debtSumByMonthBidirectionalHolder);
        }

        @PostMapping("/paginated")
        public ResponseEntity<List<DebtResponse>> paginated(@RequestBody @Valid DebtsRequest debtsRequest,
                        @RequestAttribute Long userId) {
                DebtsRequestFilters filters = debtsRequest.getFilters();
                List<DebtEntity> debts = debtService.getWithPagination(debtsRequest.getDebtUserId(), userId,
                                filters);
                return ResponseEntity.ok(debtRestMapper.fromDebtEntityListToDebtListResponse(debts));
        }

        @PostMapping("/create")
        public ResponseEntity<DebtResponse> create(@RequestBody @Valid DebtCreateRequest debtCreateRequest,
                        @RequestAttribute Long userId) {
                DebtEntity debt = debtService.create(userId, debtCreateRequest.getDebtUserId(),
                                debtCreateRequest.getAmount(), debtCreateRequest.getDescription());
                return ResponseEntity.ok(debtRestMapper.fromDebtEntityToDebtResponse(debt));
        }

        @PostMapping("/paid")
        public ResponseEntity<Void> markAsPaid(@RequestBody @Valid ArrayList<Long> ids) {
                debtService.markAsPaid(ids);
                return ResponseEntity.ok(null);
        }
}
