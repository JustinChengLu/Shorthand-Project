# Shorthand-Project
## 配置

```gr
compile project(':shorthand')
apt project(':shorthand-compiler')
```



## 注解使用说明

- **JsonObject**

  使用在需解析的Class

- **JsonAlias**

  作用于变量，在变量名和Json对应字段不同时使用

- **Ignore**

  作用于变量，忽略某个字段

注：只有保证变量有setter才能进行解析，否则默认为忽略。

## Sample

**Java bean class**

```java
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
  	@JsonAlias("date_y")
    private String date;
    private String week;
    private String temperature;
    private String weather;
    private WeatherIdEntity weather_id;
  	@Ignore
	public String test;
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
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

    }
}

```

**解析**

```java
   String json = " {\n" +
                "            \"city\": \"天津\",\n" +
                "            \"date_y\": \"2014年03月21日\",\n" +
                "            \"week\": \"星期五\",\n" +
                "            \"temperature\": \"8℃~20℃\",\n" +
                "            \"weather\": \"晴转霾\",\n" +
                "            \"weather_id\": {\t/*天气唯一标识*/\n" +
                "                \"fa\": \"00\",\t/*天气标识00：晴*/\n" +
                "                \"fb\": \"53\"\t/*天气标识53：霾 如果fa不等于fb，说明是组合天气*/\n" +
                "            }" +
                "}";
  Test test = Shorthand.parseObject(json, Test.class);
```

