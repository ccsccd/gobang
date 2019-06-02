package org.redrock.gobang.entity;

import javax.websocket.Session;
import java.util.*;

public class RoomEntity {
    String roomId;
    Date createTime;
    Map<Session, PlayerEntity> sessionMap = new HashMap<Session, PlayerEntity>();
    private int nowNumber;

    public int getNowNumber() {
        return nowNumber;
    }

    public boolean joinRoom(Session session) {
        if (nowNumber < 2) {
            sessionMap.put(session, new PlayerEntity(session));
            nowNumber++;
            return true;
        }
        return false;
    }

    public void doReady(Session session) {
        PlayerEntity playerEntity = sessionMap.get(session);
        playerEntity.setStatus("ready");
        if (checkReady()) {
            gameStart();
        }
    }

    public void undoReady(Session session) {
        PlayerEntity playerEntity = sessionMap.get(session);
        playerEntity.setStatus("free");
    }

    public void gameOver() {
        for (Map.Entry<Session, PlayerEntity> entry : sessionMap.entrySet()) {
            entry.getValue().setStatus("free");
        }
    }

    public void leaveRoom(Session session) {
        sessionMap.remove(session);
        nowNumber--;
    }

    public void broadcast(String text) {
        for (Session session : sessionMap.keySet()) {
            try {
                session.getAsyncRemote().sendText(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void gameStart() {
        for (Map.Entry<Session, PlayerEntity> item : sessionMap.entrySet()) {
            item.getValue().setStatus("playing");
        }
    }

    public boolean checkReady() {
        if (nowNumber < 2) {
            return false;
        }
        for (Map.Entry<Session, PlayerEntity> item : sessionMap.entrySet()) {
            if (!"ready".equals(item.getValue().getStatus())) {
                return false;
            }
        }
        return true;
    }
}
