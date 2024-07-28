package pro.sky.telegrambot.service;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.Exception.ExceptionBot;
import pro.sky.telegrambot.Repository.RepositoryNotificationBot;
import pro.sky.telegrambot.model.Notification;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotServiceMessagesImpl implements pro.sky.telegrambot.service.TelegramBotServiceMessages {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotServiceMessagesImpl.class);
    private static final String regularExpression = "([0-9\\.\\:\\s]{16})(\\s)([\\w+]+)";
    private final RepositoryNotificationBot repository;
    private TelegramBot bot;

    public TelegramBotServiceMessagesImpl(RepositoryNotificationBot repository, TelegramBot bot) {
        this.repository = repository;
        this.bot = bot;
    }

    public void botServiceMessages(Notification notification, Long id) {
        notification.setChatId(id);
        Notification save = repository.save(notification);
        logger.info("Notification " + save + " planned ");
    }

    @Override
    public Optional<Notification> parse(String message)  throws ExceptionBot {
        Notification notification = null;
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String messageSave = matcher.group(3);
            LocalDateTime notificationDataTime =
                    LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm"));
            if (notificationDataTime.isAfter(LocalDateTime.now())) {
                notification = new Notification(notificationDataTime, messageSave);
                logger.info("Save {} to db", notification);
                repository.save(notification);
            } else {
                logger.error("The date specified is incorrect");
                throw new ExceptionBot("Указана неверная дата ");
            }
        }
        return Optional.ofNullable(notification);
    }


    @Override
    public void mandrelMessage(Long id, String messageUserText) {
        SendMessage sendMessage = new SendMessage(id, messageUserText);
        bot.execute(sendMessage);
        logger.info("Message was sent {}", messageUserText);
    }

    @Override
    public void mandrelMessage(Notification notification) {
        mandrelMessage(notification.getChatId(), notification.getMessage());
    }

    @Override
    public void mandrelNotificationMessage() {
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        Collection<Notification> notifications = repository.findByTimeDate(localDateTime);
        notifications.forEach(task -> {
            mandrelMessage(task);
            task.setSentDate();
            logger.info("The notification has been sent {}", task );
        });
        repository.saveAll(notifications);
        logger.info("Notification were saved");
    }
}