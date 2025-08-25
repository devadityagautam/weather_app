package com.example.demow.Controller;

import com.example.demow.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class WeatherController {

    @Value("${api.key}") // FIXED placeholder
    private String apikey;

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam("city") String city, Model model) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey + "&units=metric";

        RestTemplate restTemplate = new RestTemplate();
        WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);

        if (weatherResponse != null) {
            model.addAttribute("city", weatherResponse.getName());

            if (weatherResponse.getSys() != null) {
                model.addAttribute("country", weatherResponse.getSys().getCountry());
            }

            if (weatherResponse.getMain() != null) {
                model.addAttribute("temperature", weatherResponse.getMain().getTemp());
                model.addAttribute("humidity", weatherResponse.getMain().getHumidity());
            }

            if (weatherResponse.getWind() != null) {
                model.addAttribute("windSpeed", weatherResponse.getWind().getSpeed());
            }

            if (weatherResponse.getWeather() != null && !weatherResponse.getWeather().isEmpty()) {
                model.addAttribute("weatherDescription", weatherResponse.getWeather().get(0).getDescription());
                String weatherIcon = "wi wi-own-" + weatherResponse.getWeather().get(0).getId();
                model.addAttribute("weatherIcon", weatherIcon);
            } else {
                model.addAttribute("weatherDescription", "No description available");
                model.addAttribute("weatherIcon", "wi wi-na");
            }
        } else {
            model.addAttribute("error", "City not found or API error.");
        }

        return "weather";
    }
}
