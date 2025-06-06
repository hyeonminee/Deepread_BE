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
    @Setter
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
        private int sense_order;
        private String definition;
        private String type;
        private String link;
    }
}
