package com.divizia.dbconstructor.subscriptions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SubscriptionSenderTest {

    @Autowired
    private SubscriptionSender subscriptionSender;

    @Test
    void notifySubscribers() {
        subscriptionSender.notifySubscribers();
    }

}