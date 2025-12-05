package websocket.messages;

public class NotificationMessage extends ServerMessage {
    public String msg;

    public NotificationMessage(ServerMessageType type, String msg) {
        super(type);
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
