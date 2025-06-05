package com.deepread.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class MeansRes {
    private Channel channel;

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public static class Channel {
        private String description;
        @Getter
        @Setter
        private List<Item> item;
        private Long lastBuildDate;
        private String link;
        private int num;
        private int start;
        private String title;
        private int total;

    }

    @Getter
    public static class Item {
        private String link;
        private String pos;
        private List<Sense> sense;
        private int sup_no;
        private int target_code;
        private String word;
    }


    @Getter
    @Setter
    public static class Sense {
        private int sense_order;       // 뜻풀이 순서
        private String definition;     // 뜻풀이
        private String type;           // 의미의 종류 (예: 본뜻, 비유적 표현 등)
        private String pattern;        // 문형 (예: [동사] ~을 하다)
        private String example;        // 용례 (예문)
        private String proverb;        // 관용구, 속담
        private String usage;          // 사용 범위
    }
}
