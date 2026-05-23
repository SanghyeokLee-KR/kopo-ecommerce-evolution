package kr.co.javaex.sec23.repository;

import kr.co.javaex.sec23.domain.User;
import kr.co.javaex.sec23.util.json.JsonUtil;

import java.nio.file.Path;
import java.util.List;


/**
 * <h3>유저 레파지토리</h3>
 */
public class UserRepository {

    private final Path path = Path.of("src/data/user.json");

    // 카테고리 json파일 전체 리스트로 가져오기
    public List<User> findAll() {
        return JsonUtil.readUsers(path.toString());
    }

    // 받아온 값을 json파일로 저장
    public void save(User newUser) {
        List<User> userList = findAll();
        userList.add(newUser);
        JsonUtil.writeUsers(path.toString(), userList);
    }


    // 저장
    public void updateAll(List<User> userList) {
        JsonUtil.writeUsers(path.toString(), userList);
    }

    // 받은 이메일로 user 리스트 전체를 돌려 있는지 확인한다.
    public User findByEmail(String email) {
        List<User> userList = findAll();

        for (User user : userList) {
            if (user.getUserEmail().equals(email)) {
                return user;
            }
        }

        return null;
    }

    // id값에 맞는거 json파일 객체로 받아옴
    public User findByUserId(String userId) {
        List<User> userList = findAll();

        for (User user : userList) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }

        return null;
    }
}