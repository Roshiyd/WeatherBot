import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import sevice.UtilWeather;
import sevice.WeatherBot;

import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws IOException {
        ApiContextInitializer.init();
        TelegramBotsApi api=new TelegramBotsApi();
        try {
            api.registerBot(new WeatherBot());
            System.out.println("Bot ishga tushdi");
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
