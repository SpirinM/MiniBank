package spirin.dev;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spirin.dev.console.ConsoleListener;

public class Main {
    static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("spirin.dev");

        ConsoleListener listener = context.getBean(ConsoleListener.class);
        listener.Start();
    }
}