package hello.websocket.user;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        repository.save(user);
    }

    public void disconnect(User user) {
        var storedUser = repository.findById(user.getNickName())
                .orElseThrow(() -> new IllegalStateException("해당 닉네임의 사용자가 존재하지 않습니다"));
        storedUser.setStatus(Status.OFFLINE);
        repository.save(storedUser);
    }

    public List<User> findConnectedUsers() {
        return repository.findAllByStatus(Status.ONLINE);
    }
}
