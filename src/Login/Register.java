package Login;

import User.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Register {
    ArrayList<UserPw> users = new ArrayList<>();

    void readAll() {
        String id;
        String pw;
        Scanner scanner = openFile("register.txt");
        while (scanner.hasNext()) {
            id = scanner.next();
            pw = scanner.next();
            UserPw user = new UserPw(id, pw);
            users.add(user);
        }
    }

    boolean matches(String kwd) {
        for (User user : users) {
            if (user.getName().equals(kwd)) {
                return true;
            }
        }
        for(UserPw user:users){
            if(user.getPw().equals(kwd))
                return true;

        }
        return false;
    }

    Scanner openFile(String filename) {
        Scanner filein = null;
        try {
            filein = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            System.out.println("File open failed : " + filename);
            throw new RuntimeException(e);
        }
        return filein;
    }

    void addUserToFile(String id, String pw) {
        if (!matches(id)) { // 이미 존재하는 아이디는 추가하지 않음
            try (FileWriter writer = new FileWriter("register.txt", true)) {
                writer.write(id + " " + pw + "\n");
                users.add(new UserPw(id, pw)); // 새 사용자 리스트에 추가
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } else {
            System.out.println("아이디가 이미 존재합니다.");
        }
    }

    void writeSortedToFile() {
        try (FileWriter writer = new FileWriter("register.txt")) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User u1, User u2) {
                    return Integer.compare(u2.getScore(), u1.getScore());
                }
            });
            for (User user : users) {
                writer.write(user.getName() + " " + user.getScore() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
