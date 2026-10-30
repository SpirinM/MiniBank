package spirin.dev;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spirin.dev.console.ConsoleListener;

public class Main {
    static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("spirin.dev")) {
            ConsoleListener listener = context.getBean(ConsoleListener.class);
            listener.start();
        }
    }
}