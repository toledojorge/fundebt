/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.faker;

import java.time.Instant;
import java.time.Year;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.javafaker.Faker;

import dev.carlosmolero.fundebt.domain.debt.DebtEntity;
import dev.carlosmolero.fundebt.domain.debt.DebtRepository;
import dev.carlosmolero.fundebt.domain.user.UserRepository;
import dev.carlosmolero.fundebt.domain.user.UserServiceImpl;

@SpringBootTest
public class PopulateFakeDbTests {
    @Autowired
    private DebtRepository debtRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;

    private Faker faker = new Faker();

    @Test
    public void createFakeDebts() {
        for (int i = 0; i < 100; i++) {
            Long userId = Long.parseLong(String.valueOf(faker.random().nextInt(1, 4)));
            Long debtUserId = Long.parseLong(String.valueOf(faker.random().nextInt(1, 4)));
            Float amount = Float.parseFloat(String.valueOf(faker.random().nextDouble() * 100));
            String description = faker.lorem().characters(50);
            Integer month = faker.random().nextInt(1, 12);
            Instant createdAt = Instant.parse(
                    String.format("%s-%s-03T10:15:30.00Z", Year.now().getValue(), month > 9 ? month : "0" + month));
            var debtEntity = DebtEntity.builder().initialAmount(amount).amount(amount).description(description)
                    .user(userRepository.getReferenceById(userId)).debtUser(userRepository.getReferenceById(debtUserId))
                    .build();

            // @CreationTimeStamp is only triggered the first time we save an entity,
            // updating it with a different createdAt value will override the default one
            var savedDebtEntity = debtRepository.save(debtEntity);
            savedDebtEntity.setCreatedAt(createdAt);
            debtRepository.save(savedDebtEntity);
        }
    }
}
