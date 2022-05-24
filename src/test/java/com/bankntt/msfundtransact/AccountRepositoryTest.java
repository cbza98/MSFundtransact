package com.bankntt.msfundtransact;

import com.bankntt.msfundtransact.domain.repository.AccountRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@DataMongoTest
public class AccountRepositoryTest {

    @Autowired
    AccountRepository fooRepository;


    @Test
    public void shouldBeNotEmpty() {
        assertThat(fooRepository.countByAccountTypeAndCodeBusinessPartner("PAH","P20524207509").filter(
                c->c<1
        ).block()).isNull();

    }
}