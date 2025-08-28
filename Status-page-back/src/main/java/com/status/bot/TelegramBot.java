package com.status.bot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.status.repositories.SwitchRepository;
import com.status.services.StatusService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private SwitchRepository switchRepository;
    @Autowired
    private StatusService statusService;

    private String botName;

    private List<Long> ids = new ArrayList<>();

    public TelegramBot( String botName, String botToken) {
        super(botToken);
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            var messageText = message.getText();
            ids.add(chatId);
            // try {
            //     execute(new SendMessage(chatId.toString(), "Hello world"));
            // } catch(TelegramApiException e) {
            //     log.error("Exception during processing telegram api: {}", e.getMessage());
            // }

            if (messageText.equalsIgnoreCase("/switches")) {
                try {
                    Integer switches = switchRepository.findAll().size();
                    execute(new SendMessage(chatId.toString(), "You have " + switches.toString() + " switches"));
                } catch (TelegramApiException e) {
                    log.error("Exception during processing telegram api: {}", e.getMessage());
                }
            }
            else if (messageText.equalsIgnoreCase("/offline")) {
                try {
                    Integer switches = statusService.getOfflines().size();
                    System.out.println("offines: " + switches);
                    if (switches == 0)
                        execute(new SendMessage(chatId.toString(), "There is no offline switch"));
                    else if (switches == 1)
                        execute(new SendMessage(chatId.toString(), "There is one offline switch"));
                    else if (switches > 1)
                        execute(new SendMessage(chatId.toString(), "There are " + switches.toString() + " offline switches"));
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

    public void sendMessage(String text) {
        try {
            for (Long chatId : ids) {
                execute(new SendMessage(chatId.toString(), text));
            }
        } catch (TelegramApiException e) {
            log.error("Failed to send Telegram message: {}", e.getMessage());
        }
    }
}
