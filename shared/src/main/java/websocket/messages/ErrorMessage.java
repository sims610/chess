package websocket.messages;

import com.google.gson.Gson;

public class ErrorMessage extends ServerMessage {
    public String errorMessage;

    public ErrorMessage(ServerMessageType type, String msg) {
        super(type);
        this.errorMessage = msg;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
