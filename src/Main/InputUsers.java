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

    void readAll(){
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
    void addUserToFile(String id,int point) {
        try (FileWriter writer = new FileWriter("user.txt", true)) {
            writer.write(id + " "+point+"\n");
            writeSortedToFile();
            users.clear();
            readAll();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    void writeSortedToFile() {
        try (FileWriter writer = new FileWriter("user.txt")) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User u1, User u2) {
                    return Integer.compare(u2.getScore(), u1.getScore());
                }
            });

            // Write sorted users to file
            for (User user : users) {
                writer.write(user.getName() + " " + user.getScore() + "\n");
            }
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
        InputUsers inputUsers = new InputUsers();
        inputUsers.writeSortedToFile();
    }

}
