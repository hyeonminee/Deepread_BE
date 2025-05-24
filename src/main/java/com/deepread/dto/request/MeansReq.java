package com.deepread.dto.request;

public class MeansReq {
    private String key;
    private String q;
    private String part;
    private String translated;
    private String advanced;
    private String method;

    public MeansReq(String key, String q) {
        this.key = key;
        this.q = q;
        this.part = "word";
        this.translated = "n";
        this.advanced = "y";
        this.method = "exact";
    }

    public String getParameter() {
        return "?key=" + this.key +
                "&q=" + this.q +
                "&part=" + this.part +
                "&translated=" + this.translated +
                "&advanced=" + this.advanced +
                "&method=" + this.method;
    }
}
