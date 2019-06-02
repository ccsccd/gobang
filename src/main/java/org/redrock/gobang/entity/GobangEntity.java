package org.redrock.gobang.entity;

import com.alibaba.fastjson.JSON;

import javax.websocket.Session;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GobangEntity extends RoomEntity {
    private Integer[][] chessBoard = new Integer[15][15];

    private void initChessBoard() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                chessBoard[i][j] = 0;
            }
        }
    }

    public GobangEntity(String roomId) {
        this.roomId = roomId;
        this.createTime = new Date();
        initChessBoard();
    }

    @Override
    public void gameStart() {
        int tmp = 0;
        for (Session session : sessionMap.keySet()) {
            ActionEntity actionEntity = new ActionEntity();
            actionEntity.setCode("start");
            if (tmp == 0) {
                actionEntity.setMain("black");
            } else {
                actionEntity.setMain("white");
            }
            try {
                Map<String, Object> res = new HashMap<>();
                res.put("status", 200);
                res.put("data", "成功");
                res.put("action", actionEntity);
                session.getAsyncRemote().sendText(JSON.toJSONString(res));
            } catch (Exception e) {
                e.printStackTrace();
            }
            tmp++;
        }
        super.gameStart();
    }

    public boolean vaildAction(ActionEntity actionEntity) {
        if (0 != chessBoard[actionEntity.getX()][actionEntity.getY()]) {
            return false;
        }
        chessBoard[actionEntity.getX()][actionEntity.getY()] = "black".equals(actionEntity.getColor()) ? 1 : 2;
        return true;
    }
}
