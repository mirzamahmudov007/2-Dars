package com.example.demo.bot.btn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.games.CallbackGame;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class InlineBtn {

    public SendMessage sendInlineKeyBoardMessage(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("LOTIN ");
        inlineKeyboardButton1.setCallbackData("lt");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("КРИЛ");
        inlineKeyboardButton1.setCallbackData("krl");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("Assalomu Aleykum botga hush kelibsiz!\n" +
                "O'zingiz uchun kerakli yozuv turini tanlang.\n" +
                "---------------------------------------------------------------------\n" +
                "Aссалому Aлейкум Ботга Ҳуш келибсиз!\n" +
                "Ўзингиз учун керакли ёзув турини танланг.");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    } public SendMessage tilniOzgartirish(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("LOTIN ");
        inlineKeyboardButton1.setCallbackData("lt1");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("КРИЛ");
        inlineKeyboardButton1.setCallbackData("krl1");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("O'zingiz uchun qulay tilni tanlang!");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public SendMessage sendInlineKeyBoardRegion(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Toshket ");
        inlineKeyboardButton1.setCallbackData("toshkent");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Namangan");
        inlineKeyboardButton1.setCallbackData("namangan");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Andijon");
        inlineKeyboardButton1.setCallbackData("andijon");
        keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Farg'ona");
        inlineKeyboardButton1.setCallbackData("fargona");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Samarqand");
        inlineKeyboardButton1.setCallbackData("samarqand");
        keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Jizzah");
        inlineKeyboardButton1.setCallbackData("jizzah");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Buxoro");
        inlineKeyboardButton1.setCallbackData("buxoro");
        keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Xorazm");
        inlineKeyboardButton1.setCallbackData("xorazm");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Navoiy");
        inlineKeyboardButton1.setCallbackData("navoiy");
        keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Qashqadaryo ");
        inlineKeyboardButton1.setCallbackData("qashqadaryo");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);


        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Surxandaryo");
        inlineKeyboardButton1.setCallbackData("surxondaryo");
        keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Sirdaryo");
        inlineKeyboardButton1.setCallbackData("sirdaryo");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardButton1 = new InlineKeyboardButton();
        keyboardButtonsRow1 = new ArrayList<>();
        inlineKeyboardButton1.setText("Joylashuv orqali");
        inlineKeyboardButton1.setCallbackData("location");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);


        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("O'zingiz joylashgan viloyat nomini tanlang:");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    } public SendMessage sendInlineKeyBoardRegionK(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Тошкент");
        inlineKeyboardButton1.setCallbackData("toshkent");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Наманган");
        inlineKeyboardButton1.setCallbackData("namangan");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Aндижон");
        inlineKeyboardButton1.setCallbackData("andijon");
        keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Фарғона");
        inlineKeyboardButton1.setCallbackData("fargona");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Самарқанд");
        inlineKeyboardButton1.setCallbackData("samarqand");
        keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Жиззаҳ");
        inlineKeyboardButton1.setCallbackData("jizzah");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Бухоро");
        inlineKeyboardButton1.setCallbackData("buxoro");
        keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Хоразм");
        inlineKeyboardButton1.setCallbackData("xorazm");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Навоий");
        inlineKeyboardButton1.setCallbackData("navoiy");
        keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Қашқадарё");
        inlineKeyboardButton1.setCallbackData("qashqadaryo");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);


        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Сурхандарё");
        inlineKeyboardButton1.setCallbackData("surxondaryo");
        keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Сирдарё");
        inlineKeyboardButton1.setCallbackData("sirdaryo");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardButton1 = new InlineKeyboardButton();
        keyboardButtonsRow1 = new ArrayList<>();
        inlineKeyboardButton1.setText("Жойлашув орқали");
        inlineKeyboardButton1.setCallbackData("location");
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        rowList.add(keyboardButtonsRow1);


        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("Ўзингиз жойлашган вилоят номини танланг:");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

}
