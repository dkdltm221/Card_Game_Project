package Login;


import GameUser.User;

public class UserPw extends User {
    private String pw;

    public UserPw(String name, String pw) {
        super(name);
        this.pw = pw;
    }

    public String getPw() {
        return pw; // 기본 User의 경우 null 반환
    }
}
