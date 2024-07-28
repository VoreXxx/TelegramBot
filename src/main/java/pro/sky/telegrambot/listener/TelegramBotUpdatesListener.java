package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sky.telegrambot.Exception.ExceptionBot;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.service.TelegramBotServiceMessages;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static pro.sky.telegrambot.util.BotConstant.*;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBotServiceMessages telegramBotServiceMessages;
    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, TelegramBotServiceMessages telegramBotServiceMessages) {
        this.telegramBot = telegramBot;
        this.telegramBotServiceMessages = telegramBotServiceMessages;
    }

    @PostConstruct
    public void init() {
        this.telegramBot.setUpdatesListener(this);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void mandrelNotificationMessage() {
        telegramBotServiceMessages.mandrelNotificationMessage();
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Message message = update.message();
            if(message.text().startsWith(start)){
                logger.info(start + " " + LocalDateTime.now());
                telegramBotServiceMessages.mandrelMessage(chatId(message), hello + message.from().firstName() + " ");
                telegramBotServiceMessages.mandrelMessage(chatId(message), helpOfMessage);
            } else {
                try {
                    telegramBotServiceMessages.parse(message.text())
                            .ifPresentOrElse(
                                    task -> chartNotification(chatId(message), task),
                                    () -> telegramBotServiceMessages.mandrelMessage(chatId(message), errorMessage )
                            );
                }catch (ExceptionBot e){
                    telegramBotServiceMessages.mandrelMessage(chatId(message), e.getMessage());
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void chartNotification(Long chatId, Notification notification){
        telegramBotServiceMessages.botServiceMessages(notification, chatId);
        telegramBotServiceMessages.mandrelMessage(chatId, "The task " + notification.getMessage() + " is created");
    }
    private Long chatId(Message message){ return  message.chat().id();}
}