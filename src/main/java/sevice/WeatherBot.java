package sevice;

import lombok.SneakyThrows;
import model.Response;
import model.WeatherItem;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WeatherBot extends TelegramLongPollingBot {
    public static final String botName = "weathercheckuzbot";
    public static final String botToken = "1474379787:AAFF6ghrenOyVwgHGb2LkRZiB6ZGbovpGuU";
    public static String chatText;
    int step = 0;

    public String getBotToken() {
        return botToken;
    }

    public String getBotUsername() {
        return botName;
    }

    @SneakyThrows
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        chatText = message.getText();
        sendMessage.setChatId(chatId);
        if (chatText.equals("/start")) {
            sendMessage.setText("Xush kelibsiz " + message.getFrom().getFirstName() + "!");
            step = 1;
        }
        switch (step) {
            case 1:
                sendMessage.setText("Bot tilini tanlang ⏬");
                languageButtons(sendMessage);
                step = 2;
                break;
            case 2:
                if (chatText.equalsIgnoreCase("⏪")) {
                    sendMessage.setText("⏪");
                    languageButtons(sendMessage);
                    step = 1;
                } else if (chatText.equals("\uD83C\uDDFA\uD83C\uDDFF O'ZBEKCHA")) {
                    sendMessage.setText("Joy nomini tanlang ⏬");
                    uzWeather(sendMessage);
                    step = 3;
                } else if (chatText.equals("\uD83C\uDDF7\uD83C\uDDFA РУССКИЙ")) {
                    sendMessage.setText("Выберите локацию ⏬");
                    ruWeather(sendMessage);
                    step = 4;
                }
                else if (chatText.equals("\uD83C\uDDEC\uD83C\uDDE7 ENGLISH")) {
                    sendMessage.setText("Choose location:");
                    enWeather(sendMessage);
                    step = 5;
                }
                break;
            case 3:

                if (chatText.equalsIgnoreCase("⏪")){
                    sendMessage.setText("Bot tilini tanlang⏬");
                    languageButtons(sendMessage);
                    step=2;
                    break;
                }
                weatherInfo(sendMessage,1);
                step=8;
                break;
            case 4:
                if (chatText.equalsIgnoreCase("⏪")){
                    sendMessage.setText("Выберите язык⏬");
                    languageButtons(sendMessage);
                    step=2;
                    break;
                }
                weatherInfo(sendMessage,2);
                step=6;
                break;
            case 5:
                if (chatText.equalsIgnoreCase("⏪")){
                    sendMessage.setText("Choose language⏬");
                    languageButtons(sendMessage);
                    step=2;
                    break;
                }
                weatherInfo(sendMessage,3);
                step=7;
                break;
            case 6:
                if (chatText.equalsIgnoreCase("⏪⏪")){
                    sendMessage.setText("Выберите локацию⏬");
                    ruWeather(sendMessage);
                    step=4;
                    break;
                }
            case 7:
                if (chatText.equalsIgnoreCase("⏪⏪")){
                    sendMessage.setText("Choose the location⏬");
                    enWeather(sendMessage);
                    step=5;
                    break;
                }

            case 8:
                if (chatText.equalsIgnoreCase("⏪⏪")){
                    sendMessage.setText("Joy nomini tanlang⏬");
                    uzWeather(sendMessage);
                    step=3;
                    break;
                }
        }

        execute(sendMessage);
    }

    public void languageButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<KeyboardRow>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        // 🇺🇿 O'ZBEKCHA
        row1.add(new KeyboardButton("\uD83C\uDDFA\uD83C\uDDFF O'ZBEKCHA"));
        row2.add(new KeyboardButton("\uD83C\uDDF7\uD83C\uDDFA РУССКИЙ"));
        row3.add(new KeyboardButton("\uD83C\uDDEC\uD83C\uDDE7 ENGLISH"));
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    @SneakyThrows
    public void weatherInfo(SendMessage sendMessage,int choice) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<KeyboardRow>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("⏪⏪"));
        keyboardRows.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        UtilWeather.region=chatText;
        String regionName=chatText;
        Response response = UtilWeather.getWeatherItems();
        StringBuilder stringBuilder = new StringBuilder();
        LocalDate localDate = LocalDate.now();
        double kelvin=response.getMain().getTemp();
        double celcius=kelvin-273.15;
        if (choice==1){
        stringBuilder.append("\uD83D\uDCCD Joy nomi: " + regionName)
                .append("\n" + "\uD83D\uDDD3 Sana: " + localDate)
                .append("\n" +"\uD83C\uDF24 Temperatura: "+(int)celcius+"°C || "+kelvin +" K")
                .append("\n"+"\uD83D\uDCA7 Namlik: "+response.getMain().getHumidity()+" %")
                .append("\n"+"\uD83D\uDCA8 Shamol tezligi: "+response.getWind().getSpeed()+" km/h");
        sendMessage.setText(stringBuilder.toString());
        }
        if (choice==2){
            stringBuilder.append("\uD83D\uDCCD Локация: " + regionName)
                    .append("\n" + "\uD83D\uDDD3 Дата: " + localDate)
                    .append("\n" +"\uD83C\uDF24 Температура: "+(int)celcius+"°C || "+kelvin +" K")
                    .append("\n"+"\uD83D\uDCA7 Влажность: "+response.getMain().getHumidity()+" %")
                    .append("\n"+"\uD83D\uDCA8 Скорость ветра: "+response.getWind().getSpeed()+" km/h");
            sendMessage.setText(stringBuilder.toString());
        }

        if (choice==3){
            stringBuilder.append("\uD83D\uDCCD Location: " + regionName)
                    .append("\n" + "\uD83D\uDDD3 Date: " + localDate)
                    .append("\n" +"\uD83C\uDF24 Temperature: "+(int)celcius+"°C || "+kelvin +" K")
                    .append("\n"+"\uD83D\uDCA7 Humidity: "+response.getMain().getHumidity()+" %")
                    .append("\n"+"\uD83D\uDCA8 Wind speed: "+response.getWind().getSpeed()+" km/h");
            sendMessage.setText(stringBuilder.toString());
        }
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    @SneakyThrows
    public void uzWeather(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRows = new ArrayList<KeyboardRow>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2=new KeyboardRow();
        KeyboardRow row3=new KeyboardRow();
        KeyboardRow row4=new KeyboardRow();
        KeyboardRow row5=new KeyboardRow();
        row1.add(new KeyboardButton("Tashkent"));
        row1.add(new KeyboardButton("Andijon"));
        row1.add(new KeyboardButton("Namangan"));
        row2.add(new KeyboardButton("Farg'ona"));
        row2.add(new KeyboardButton("Sirdaryo"));
        row2.add(new KeyboardButton("Jizzax"));
        row3.add(new KeyboardButton("Samarqand"));
        row3.add(new KeyboardButton("Buxoro"));
        row3.add(new KeyboardButton("Navoiy"));
        row4.add(new KeyboardButton("Qarshi"));
        row4.add(new KeyboardButton("Termiz"));
        row4.add(new KeyboardButton("Urganch"));
        row5.add(new KeyboardButton("⏪"));
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        keyboardRows.add(row4);
        keyboardRows.add(row5);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
    @SneakyThrows
    public void ruWeather(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRows = new ArrayList<KeyboardRow>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2=new KeyboardRow();
        KeyboardRow row3=new KeyboardRow();
        KeyboardRow row4=new KeyboardRow();
        KeyboardRow row5=new KeyboardRow();
        row1.add(new KeyboardButton("Ташкент"));
        row1.add(new KeyboardButton("Андижан"));
        row1.add(new KeyboardButton("Наманган"));
        row2.add(new KeyboardButton("Фергана"));
        row2.add(new KeyboardButton("Гулистан"));
        row2.add(new KeyboardButton("Джизак"));
        row3.add(new KeyboardButton("Самарканд"));
        row3.add(new KeyboardButton("Бухара"));
        row3.add(new KeyboardButton("Навои"));
        row4.add(new KeyboardButton("Карши"));
        row4.add(new KeyboardButton("Термез"));
        row4.add(new KeyboardButton("Ургенч"));
        row5.add(new KeyboardButton("⏪"));
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        keyboardRows.add(row4);
        keyboardRows.add(row5);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
    @SneakyThrows
    public void enWeather(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRows = new ArrayList<KeyboardRow>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2=new KeyboardRow();
        KeyboardRow row3=new KeyboardRow();
        KeyboardRow row4=new KeyboardRow();
        KeyboardRow row5=new KeyboardRow();
        row1.add(new KeyboardButton("Tashkent"));
        row1.add(new KeyboardButton("Andijan"));
        row1.add(new KeyboardButton("Namangan"));
        row2.add(new KeyboardButton("Fergana"));
        row2.add(new KeyboardButton("Sirdaryo"));
        row2.add(new KeyboardButton("Jizzakh"));
        row3.add(new KeyboardButton("Samarkand"));
        row3.add(new KeyboardButton("Bukhara"));
        row3.add(new KeyboardButton("Navoiy"));
        row4.add(new KeyboardButton("Qashqadaryo"));
        row4.add(new KeyboardButton("Termez"));
        row4.add(new KeyboardButton("Urganch"));
        row5.add(new KeyboardButton("⏪"));
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        keyboardRows.add(row4);
        keyboardRows.add(row5);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
}
