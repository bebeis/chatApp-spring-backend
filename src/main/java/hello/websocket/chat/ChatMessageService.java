package hello.websocket.chat;

import hello.websocket.chatroom.ChatRoomService;
import hello.websocket.exception.ChatRoomLoadFailureException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                true
        ).orElseThrow(() -> new ChatRoomLoadFailureException("채팅창 조회 및 생성에 실패하여 메시지를 보낼 수 없습니다."));
        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(
            String senderId, String recipientId
    ) {
        var chatId = chatRoomService.getChatRoomId(
                senderId,
                recipientId,
                false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
}
