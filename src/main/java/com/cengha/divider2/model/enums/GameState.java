package com.cengha.divider2.model.enums;

public enum GameState {
    CREATED(0), STARTED(1), FINISHED(2), TERMINATED(3);

    private Integer stateId;

    GameState(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getStateId() {
        return stateId;
    }
}