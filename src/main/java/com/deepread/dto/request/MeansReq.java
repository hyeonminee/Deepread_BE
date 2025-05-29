package com.deepread.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class MeansReq {

    @Schema(description = "API 인증키")
    private String key;

    @Schema(description = "검색어")
    private String q;

    @Schema(description = "검색 대상 파트", defaultValue = "word")
    private String part;

    @Schema(description = "번역 포함 여부", defaultValue = "n")
    private String translated;

    @Schema(description = "심화 정보 포함 여부", defaultValue = "y")
    private String advanced;

    @Schema(description = "검색 방식", defaultValue = "exact")
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
