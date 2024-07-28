package pro.sky.telegrambot.Exception;

public class ExceptionBot extends Exception{
    public ExceptionBot(String name){super("Wrong message: " + name);}
}
