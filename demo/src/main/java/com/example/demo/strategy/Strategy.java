package com.example.demo.strategy;

public class Strategy {

    public String strategy(int temp,int humidity,int rate,int noise) {
        String active1  = "",active2 = "",active3 = "",active4 = "";
        if (temp < 10) {
           active1 = "睡前饮用温热的牛奶或姜茶（提升体温，避免空腹）";
        } else if (temp < 25 && temp>10) {
           active1 = "睡前开窗30分钟换气（低温时开小缝，高温时对流循环）";
        } else if (temp < 40 && temp>30) {
           active1 = "建议使用水循环凉垫，冷冻矿泉水瓶裹毛巾";
        }else if (temp < 50 && temp>40) {
           active1 = "搭建冰帐篷泡沫箱内铺锡纸冰块，头部置于开口处呼吸";
        }

        if (humidity < 49 && humidity>40) {
             active2 = "启用加湿器（目标40%）";
        } else if (humidity < 30 && humidity>20) {
             active2 = "局部加湿（床头)";
        } else if (humidity < 60 && humidity>30) {
             active2 = "每日通风2次（每次30分钟）";
        }else if (humidity < 70 && humidity>60) {
             active2 = "开启除湿机（目标50%）衣柜放置除湿盒";
        }

        if (rate < 49 && rate>40) {
             active3 = "增加日间有氧运动（如快走30分钟）";
        } else if (rate < 100 && rate>50) {
             active3 = "心率正常可安心睡眠";
        } else if (rate>100) {
             active3 = "检查是否噩梦,调整室温至18-22℃";
        }

        if (noise < 65 && noise>50) {
             active4 = "使用白噪音发生器，听会歌";
        } else if (noise < 80 && noise>65) {
             active4 = "佩戴SNR≥21dB的降噪耳塞";
        }
        return active1+active2+active3+active4;

    }

}
