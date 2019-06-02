package org.redrock.gobang.controller;

import com.alibaba.fastjson.JSON;
import org.redrock.gobang.entity.ActionEntity;
import org.redrock.gobang.entity.GobangEntity;
import org.redrock.gobang.entity.RoomEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/gobang")
public class WebSocketChess {

    private static Logger logger = LoggerFactory.getLogger(WebSocketChess.class);
    private static ConcurrentHashMap<String, RoomEntity> roomMap = new ConcurrentHashMap<String, RoomEntity>();

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        String roomId = getRoomId(session);
        if ("connect".equals(message)) {
            doConnect(session);
        } else if (message.startsWith("chess")) {
            String content = message.substring(5);
            ActionEntity actionEntity = JSON.parseObject(content, ActionEntity.class);
            actionEntity.setCode("chess");
            RoomEntity roomEntity = roomMap.get(roomId);
            GobangEntity gobangEntity=(GobangEntity) roomEntity;
            if (gobangEntity.vaildAction(actionEntity)) {
                Map<String, Object> res = new HashMap<>();
                res.put("status", 200);
                res.put("data", "成功");
                res.put("action", actionEntity);
                roomEntity.broadcast(JSON.toJSONString(res));
            }
        } else if ("ready".equals(message)) {
            doReady(session);
        } else if ("free".equals(message)) {
            undoReady(session);
        } else if ("over".equals(message)) {
            doOver(session);
        }
    }

    private void doReady(Session session) {
        RoomEntity roomEntity = roomMap.get(getRoomId(session));
        roomEntity.doReady(session);
    }

    private void undoReady(Session session) {
        RoomEntity roomEntity = roomMap.get(getRoomId(session));
        roomEntity.undoReady(session);
    }

    private void doOver(Session session) {
        RoomEntity roomEntity = roomMap.get(getRoomId(session));
        roomEntity.gameOver();
    }

    private String getRoomId(Session session) {
        List<String> roomList = session.getRequestParameterMap().get("roomId");
        String roomId = roomList.get(0);
        return roomId;
    }

    public void doConnect(Session session) {
        List<String> roomList = session.getRequestParameterMap().get("roomId");
        String roomId = roomList.get(0);
        if (roomMap.containsKey(roomId)) {
            RoomEntity roomEntity = roomMap.get(roomId);
            if (roomEntity.joinRoom(session)) {
                session.getUserProperties().put("roomId", roomId);
            } else {
                Map<String, Object> res = new HashMap<>();
                res.put("status", 400);
                res.put("data", "进入房间失败");
                session.getAsyncRemote().sendText(JSON.toJSON(res).toString());
            }
        } else {
            RoomEntity roomEntity = new GobangEntity(roomId);
            roomEntity.joinRoom(session);
            roomMap.put(roomId, roomEntity);
            session.getUserProperties().put("roomId", roomId);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("现在来连接的客户id：" + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        String roomId = (String) session.getUserProperties().get("roomId");
        if (roomId != null) {
            RoomEntity roomEntity = roomMap.get(roomId);
            if (roomEntity != null) {
                roomEntity.leaveRoom(session);
                if (roomEntity.getNowNumber() <= 0) {
                    roomMap.remove(roomId);
                }
            }
        }
        logger.info("连接关闭");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("服务端发生了错误  " + error.getMessage());
    }
}
