package com.tinder.app;

public class Match {
    private Long userId;
    private String matchedWith;

    public Match() {
    }

    public Match(Long userId, String matchedWith) {
        this.userId = userId;
        this.matchedWith = matchedWith;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMatchedWith() {
        return matchedWith;
    }

    public void setMatchedWith(String matchedWith) {
        this.matchedWith = matchedWith;
    }
}
