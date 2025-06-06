package hello.websocket.exception;

public class ChatRoomLoadFailureException extends RuntimeException {
  public ChatRoomLoadFailureException(String message) {
    super(message);
  }
}
