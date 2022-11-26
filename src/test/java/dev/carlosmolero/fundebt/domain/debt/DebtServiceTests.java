/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.domain.debt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import dev.carlosmolero.fundebt.domain.user.UserEntity;
import dev.carlosmolero.fundebt.domain.user.UserRepository;

@SpringBootTest
public class DebtServiceTests {
    @Autowired
    DebtServiceImpl debtService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    DebtRepository debtRepository;

    @Test
    public void shouldCreateADebt() {
        UserEntity user = UserEntity.builder().id(1L).build();
        UserEntity debtUser = UserEntity.builder().id(2L).build();

        Mockito.when(userRepository.getReferenceById(1L)).thenReturn(user);
        Mockito.when(userRepository.getReferenceById(2L)).thenReturn(debtUser);

        DebtEntity userDebt = DebtEntity.builder().user(user).debtUser(debtUser).amount(5f).description("").build();

        Mockito.when(debtRepository.save(userDebt)).thenReturn(userDebt);
        DebtEntity debtEntity = debtService.create(1L, 2L, userDebt.getAmount(), userDebt.getDescription());

        assertNotNull(debtEntity);
    }
}
