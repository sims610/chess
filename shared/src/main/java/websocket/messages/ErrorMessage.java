package websocket.messages;

public class ErrorMessage extends ServerMessage {
    String msg;

    public ErrorMessage(ServerMessageType type, String msg) {
        super(type);
        this.msg = msg;
    }
}
