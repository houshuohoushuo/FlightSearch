package com.example.monkey.myapplication.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class UserSystem implements Serializable {

    private HashMap<String, User> users = new HashMap();
    private HashMap<String, ArrayList<String>> accounts = new HashMap<>();

    /**
     * Returns the information stored for the client with the given email.
     *
     * @param email
     *            the email address of a client
     * @return the information stored for the client with the given email in
     *         this format:
     *         LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
     *         (the ExpiryDate is stored in the format YYYY-MM-DD)
     * @throws IOException
     */
    public String getClient(String email) throws IOException {
        String result = "";
        for (User clients : users.values()) {
            if (clients.getEmail().equals(email)) {
                result += users.get(email);
            }
        }
        return result;
    }

    /**
     * Uploads client information to the application from the file at the given
     * path.
     *
     * @param file
     *            the path to an input csv file of client information with lines
     *            in the format:
     *            LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate,Password
     *            (the ExpiryDate is stored in the format YYYY-MM-DD)
     * @throws IOException
     */
    public void uploadClientInfo(File file) throws FileNotFoundException {

        Scanner s = new Scanner(file);
        while (s.hasNext()) {
            String line = s.nextLine();
            String[] l = line.split(",");
            String key = l[2];
            User value = new User(l[0], l[1], l[2], l[3], l[4], l[5]);
            users.put(key, value);
        }

        s.close();
    }

    public void uploadAccount(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        while (s.hasNext()) {
            String line = s.nextLine();
            String[] l = line.split(",");
            String key = l[0];
            ArrayList<String> value = new ArrayList<>();
            value.add(l[1]);
            value.add(l[2]);
            accounts.put(key, value);
        }
        s.close();
    }

    public boolean checkAccount(String account, String password) {
        for (String key : accounts.keySet()) {
            if ((account.equals(key)) && (accounts.get(key).get(0).equals(password))) {
                return true;
            }
        }
        return false;
    }

    public String getUserType(String account) {
        for (String key : accounts.keySet()) {
            if (account.equals(key)) {
                return accounts.get(key).get(1);
            }
        }
        return "account is neither Client nor Admin";

    }

    public User getUser(String email){
        for (User clients : users.values()) {
            if (clients.getEmail().equals(email)) {
                return clients;
            }
        }
        return null;
    }

    public void saveFile(File file) throws IOException{

        PrintWriter pw = new PrintWriter(new FileWriter(file));

        for(User user1: users.values()){

            pw.println(user1.toString());

        }

        pw.close();

    }

    public void savePasswordFile(File file) throws IOException{
        PrintWriter pw = new PrintWriter(new FileWriter(file));

        for(String key: accounts.keySet()){

            pw.println(key+ accounts.get(key).get(0)+ accounts.get(key).get(1));

        }

        pw.close();

    }

    public String getMap() {
        return accounts.toString();
    }
}
