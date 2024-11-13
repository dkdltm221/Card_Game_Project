package Main;

import User.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class InputUsers {
    static ArrayList<User> users=new ArrayList<>();

    public void readAll(){
        users.clear();
        String id;
        int point;
        Scanner scanner = openFile("user.txt");
        while(scanner.hasNext()){
            id = scanner.next();
            point = scanner.nextInt();
            User user = new User(id);
            user.addScore(point);
            users.add(user);
        }
    }

    boolean matches(String id){
        for(User user:users){
            if(user.getName().equals(id)){
                return true;
            }
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
    public void addUserToFile(String id, int point) {
        boolean userExists = false;

        // 기존 유저 점수 업데이트
        for (User user : users) {
            if (user.getName().equals(id)) {
                userExists = true;
                user.addScore(point); // 기존 유저의 점수를 업데이트
                break;
            }
        }

        if (!userExists) {
            // 기존 유저가 없다면 새로운 유저 추가
            User user = new User(id);
            user.addScore(point);
            users.add(user);
        }

        // 업데이트된 사용자 목록을 파일에 쓰기
        writeSortedToFile();
    }

    private static void writeSortedToFile() {
        try (FileWriter writer = new FileWriter("user.txt")) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User u1, User u2) {
                    return Integer.compare(u2.getScore(), u1.getScore());
                }
            });

            // 파일에 정렬된 사용자 목록 쓰기
            for (User user : users) {
                writer.write(user.getName() + " " + user.getScore() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    User getUser(String id){
        for(User user:users){
            if(user.getName().equals(id)){
                return user;
            }
        }
        return null;
    }
    static void userUpdate(String userName, int point) {
        for (User u : users) {
            if (u.getName().equals(userName)) {
                u.addScore(point);
                break;
            }
        }
        writeSortedToFile();
    }

}