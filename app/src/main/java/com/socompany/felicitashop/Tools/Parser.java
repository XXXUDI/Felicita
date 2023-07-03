package com.socompany.felicitashop.Tools;

public class Parser {

    // Метод для конверту з 0123456789 в (012) 345 67-89
    public static String phoneParser(String phone) {
        // Видаляємо всі символи, окрім цифр, з номера телефону
        String digits = phone.replaceAll("\\D", "");

        // Перевіряємо, чи маємо ми достатньо цифр у номері телефону
        if (digits.length() != 10) {
            throw new IllegalArgumentException("Неправильний формат номера телефону");
        }

        // Форматуємо номер телефону у вигляді "(XXX) XXX-XX-XX"
        String formattedNumber = String.format("(%s) %s-%s-%s",
                digits.substring(0, 3),
                digits.substring(3, 6),
                digits.substring(6, 8),
                digits.substring(8, 10));

        return formattedNumber;
    }
}
