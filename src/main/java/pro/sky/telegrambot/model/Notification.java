package pro.sky.telegrambot.model;

import org.springframework.data.annotation.Id;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity(name = "notification")
public class Notification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String message;
    private LocalDateTime timeDate;
    private LocalDateTime sentDate;
    @Enumerated(EnumType.STRING)
    private NotificationBotStatus status = NotificationBotStatus.SCHEDULED;
    public Notification(){}
    public Notification(LocalDateTime sentDate, String message){
        this.message = message;
        this.sentDate = sentDate;
    }

    public Long getChatId() {return chatId;}
    public void setChatId(Long chatId){this.chatId = chatId;}
    public String getMessage() {return message;}
    public LocalDateTime getTimeDate(){return timeDate;}
    public LocalDateTime getSentDate(){return sentDate;}
    public NotificationBotStatus getStatus(){return status;}


    public void setSentDate(){
        this.status = NotificationBotStatus.SENT;
        this.sentDate = LocalDateTime.now();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(message, that.message) && Objects.equals(timeDate, that.timeDate) && Objects.equals(sentDate, that.sentDate) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getChatId(), getMessage(), getTimeDate(), getSentDate(), getStatus());
    }

    @Override
    public String toString() {
        return "NotificationBot{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                ", timeDate=" + timeDate +
                ", sentDate=" + sentDate +
                ", status=" + status +
                '}';
    }
}
