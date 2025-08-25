package com.status.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.status.repositories.SwitchRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private SwitchRepository switchRepository;

    private String botName;

    public TelegramBot( String botName, String botToken) {
        super(botToken);
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            var chatId = message.getChatId();
            log.info("Message received: {}", message);
            var messageText = message.getText();
            log.info(messageText);
            // try {
            //     execute(new SendMessage(chatId.toString(), "Hello world"));
            // } catch(TelegramApiException e) {
            //     log.error("Exception during processing telegram api: {}", e.getMessage());
            // }

            if (messageText.equalsIgnoreCase("/switches")) {
                try {
                    Integer switches = switchRepository.findAll().size();
                    execute(new SendMessage(chatId.toString(), switches.toString()));
                } catch (TelegramApiException e) {
                    log.error("Exception during processing telegram api: {}", e.getMessage());
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }
}
