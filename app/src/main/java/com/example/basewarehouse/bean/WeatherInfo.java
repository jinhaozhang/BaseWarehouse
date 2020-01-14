package com.example.basewarehouse.bean;

import java.util.List;

public class WeatherInfo {
//     "message": "success感谢又拍云(upyun.com)提供CDN赞助",
//             "status": 200,
//             "date": "20200114",
//             "time": "2020-01-14 10:54:49"
    public String message;
    public String status;
    public String date;
    public String time;
//    "cityInfo": {
//        "city": "北京市",
//                "citykey": "101010100",
//                "parent": "北京",
//                "updateTime": "10:23"
//    },
    public CityInfo cityInfo;
    public DataInfo data;
    public ForecastInfo yesterday;

    public class CityInfo{
        public String city;
        public String citykey;
        public String parent;
        public String updateTime;
    }
    public class DataInfo{
//         "shidu": "31%",
//                 "pm25": 11.0,
//                 "pm10": 24.0,
//                 "quality": "优",
//                 "wendu": "-5",
//                 "ganmao": "各类人群可自由活动"
        public String shidu;
        public String pm25;
        public String pm10;
        public String quality;
        public String wendu;
        public String ganmao;
        public List<ForecastInfo> forecast;
    }

    public class ForecastInfo{
// "date": "15",
//         "high": "高温 3℃",
//         "low": "低温 -6℃",
//         "ymd": "2020-01-15",
//         "week": "星期三",
//         "sunrise": "07:35",
//         "sunset": "17:12",
//         "aqi": 90,
//         "fx": "西南风",
//         "fl": "<3级",
//         "type": "晴",
//         "notice": "愿你拥有比阳光明媚的心情"

        public String date;
        public String high;
        public String low;
        public String ymd;
        public String week;
        public String sunrise;
        public String sunset;
        public String aqi;
        public String fx;
        public String fl;
        public String type;
        public String notice;



    }
}
