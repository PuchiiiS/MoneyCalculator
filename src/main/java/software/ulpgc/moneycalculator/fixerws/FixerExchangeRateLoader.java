package software.ulpgc.moneycalculator.fixerws;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import software.ulpgc.moneycalculator.Currency;
import software.ulpgc.moneycalculator.ExchangeRate;
import software.ulpgc.moneycalculator.ExchangeRateLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;

public class FixerExchangeRateLoader implements ExchangeRateLoader {
    private static final String BASE_CURRENCY_CODE = "EUR";

    @Override
    public ExchangeRate load(Currency from, Currency to) {
        try {
            String json = loadJson();
            LocalDate date = LocalDate.parse(new Gson().fromJson(json, JsonObject.class).get("date").getAsString());
            JsonObject ratesObject = new Gson().fromJson(json, JsonObject.class).getAsJsonObject("rates");

            // Si alguna de las monedas es EUR, manejar directamente
            if (from.code().equals(BASE_CURRENCY_CODE)) {
                return new ExchangeRate(from, to, date, ratesObject.get(to.code()).getAsDouble());
            } else if (to.code().equals(BASE_CURRENCY_CODE)) {
                return new ExchangeRate(from, to, date, 1 / ratesObject.get(from.code()).getAsDouble());
            } else {
                // Para la conversión entre dos monedas que no son EUR, convertir a través de EUR
                double rateFromToEUR = 1 / ratesObject.get(from.code()).getAsDouble();
                double rateEURToTo = ratesObject.get(to.code()).getAsDouble();
                double rateFromToTo = rateFromToEUR * rateEURToTo;
                return new ExchangeRate(from, to, date, rateFromToTo);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Maneja la excepción de manera apropiada
        }
        return null; // Devuelve null si no se encuentra la tasa de cambio o si ocurre un error
    }

    private String loadJson() throws IOException {
        URL url = new URL("http://data.fixer.io/api/latest?access_key=" + FixerAPI.key);
        try (InputStream is = url.openStream()) {
            return new String(is.readAllBytes());
        }
    }
}
