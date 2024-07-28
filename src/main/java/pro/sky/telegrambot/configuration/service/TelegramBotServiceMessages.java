package pro.sky.telegrambot.service;

import pro.sky.telegrambot.Exception.ExceptionBot;
import pro.sky.telegrambot.model.Notification;


import java.util.Optional;

public interface TelegramBotServiceMessages {
    void botServiceMessages(Notification notification, Long id);
    Optional<Notification> parse(String notificationMessage) throws ExceptionBot;
    void mandrelMessage(Long id, String messageUserText);
    void mandrelMessage(Notification notification);
    void mandrelNotificationMessage();
}
