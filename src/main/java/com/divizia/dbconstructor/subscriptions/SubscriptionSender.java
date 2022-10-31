package com.divizia.dbconstructor.subscriptions;

import com.divizia.dbconstructor.excel.ExcelSaver;
import com.divizia.dbconstructor.model.entity.Record;
import com.divizia.dbconstructor.model.entity.*;
import com.divizia.dbconstructor.model.service.RecordService;
import com.divizia.dbconstructor.model.service.SubscriptionTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionSender {

    private final SubscriptionTaskService subscriptionTaskService;
    private final RecordService recordService;
    private final EmailService emailService;
    private final ExcelSaver excelSaver;
    @Value("${subscriptions.send-emails}")
    private boolean sendEmails;

    @Scheduled(cron = "0 0 15 * * *")
    public void notifySubscribers() {

        List<SubscriptionTask> subscriptionTaskList = subscriptionTaskService.findAll();

        Map<User, Map<Subscription, Set<Long>>> notifyList = subscriptionTaskList
                .stream()
                .collect(Collectors.groupingBy(
                        x -> x.getSubscription().getUser(),
                        Collectors.groupingBy(
                                SubscriptionTask::getSubscription,
                                Collectors.mapping(
                                        SubscriptionTask::getRecordId,
                                        Collectors.toSet()
                                ))));

        if (sendEmails)
            notifyList.forEach(this::notifyUser);

        subscriptionTaskService.deleteAll(subscriptionTaskList);
    }

    private void notifyUser(User user, Map<Subscription, Set<Long>> subscriptions) {

        Map<CustomTable, List<Record>> data = subscriptions.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        x -> x.getKey().getCustomTable(),
                        x -> recordService.findAllById(x.getKey().getCustomTable().getId(), x.getValue())
                ));

        Resource resource = excelSaver.exportRecords("new_records", data);

        emailService.sendMessageWithAttachment(
                user.getEmail(),
                "DB-Constructor: Notification about new records",
                "See attachment",
                resource);
    }

}
