package websocket.messages;

import com.google.gson.Gson;

public class NotificationMessage extends ServerMessage {
    public String message;

    public NotificationMessage(ServerMessageType type, String msg) {
        super(type);
        this.message = msg;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }


}
