package ru.job4j.di;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringDI {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("ru.job4j.di");
        context.refresh();
        Store store = context.getBean(Store.class);
        store.add("Ivan ivanov");
        Store anStore = context.getBean(Store.class);
        anStore.getAll().forEach(System.out::println);
        System.out.println("after anStore getAll()");
        store.getAll().forEach(System.out::println);
    }
}
