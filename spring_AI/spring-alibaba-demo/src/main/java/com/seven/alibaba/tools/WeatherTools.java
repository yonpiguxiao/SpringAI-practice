package com.seven.alibaba.tools;

public class WeatherTools {
    String getCurrentWeatherByCityName(String cityName) {
        switch (cityName){
            case "北京":
                return "北京今天天⽓: 晴空万⾥";
            case "上海":
                return "上海今天天⽓: 电闪雷鸣";
            case "⼴州":
                return "⼴州今天天⽓: 细⾬蒙蒙";
            default:
                return "没有该城市的天⽓信息";
        }
    }
}
