package justin.shorthand.sample.bean;

import justin.shorthand.annotations.JsonObject;

/**
 * @author justin on 2017/02/17 14:42
 * @version V1.0
 */
@JsonObject
public class Test {

    /**
     * city : 天津
     * date_y : 2014年03月21日
     * week : 星期五
     * temperature : 8℃~20℃
     * weather : 晴转霾
     * weather_id : {"fa":"00","fb":"53"}
     */

    private String city;
    private String date_y;
    private String week;
    private String temperature;
    private String weather;
    private WeatherIdEntity weather_id;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate_y() {
        return date_y;
    }

    public void setDate_y(String date_y) {
        this.date_y = date_y;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public WeatherIdEntity getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(WeatherIdEntity weather_id) {
        this.weather_id = weather_id;
    }

    @Override
    public String toString() {
        return "Test{" +
                "city='" + city + '\'' +
                ", date_y='" + date_y + '\'' +
                ", week='" + week + '\'' +
                ", temperature='" + temperature + '\'' +
                ", weather='" + weather + '\'' +
                ", weather_id=" + weather_id +
                '}';
    }

    @JsonObject
    public static class WeatherIdEntity {
        /**
         * fa : 00
         * fb : 53
         */

        private String fa;
        private String fb;

        public String getFa() {
            return fa;
        }

        public void setFa(String fa) {
            this.fa = fa;
        }

        public String getFb() {
            return fb;
        }

        public void setFb(String fb) {
            this.fb = fb;
        }

        @Override
        public String toString() {
            return "WeatherIdEntity{" +
                    "fa='" + fa + '\'' +
                    ", fb='" + fb + '\'' +
                    '}';
        }
    }
}
