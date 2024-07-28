package pro.sky.telegrambot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Notification;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RepositoryNotificationBot extends JpaRepository<Notification, Long> {
    List<Notification> findByTimeDate(LocalDateTime dateTime);
}
