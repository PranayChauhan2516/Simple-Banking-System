package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        String url = "jdbc:sqlite:" + args[1];
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {

                statement.executeUpdate("CREATE TABLE card (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0)");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int[] cardNumber=new int[10];
        int[] PIN=new int[4];
        while(true){

            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");

            int n=in.nextInt();
            boolean exit=false;

            if(n==1){

                System.out.println("Your card has been created");
                System.out.println("Your card number: ");
                System.out.print(400000);

                Random generator=new Random();
                for(int i=0;i<10;++i) {
                    cardNumber[i]=generator.nextInt(10);
                }
                checkSumGenerator(cardNumber);
                System.out.println();

                System.out.println("Your card PIN: ");

                for(int i=0;i<4;++i){
                    PIN[i]=generator.nextInt(10);
                    System.out.print(PIN[i]);
                }
                System.out.println();

                String tempCard="400000";
                for(int i=0;i<10;++i) tempCard+=Integer.toString(cardNumber[i]);
                String tempPin="";
                for(int i=0;i<4;++i) tempPin+=Integer.toString(PIN[i]);
                String sql = "INSERT INTO card (number, pin) VALUES (?, ?)";
                try (Connection con = dataSource.getConnection()) {
                    try (Statement statement = con.createStatement()) {

                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setString(1, tempCard);
                        pstmt.setString(2, tempPin);

                        pstmt.executeUpdate();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }

            if(n==2){

                System.out.println("Enter your card number:");
                char[] enteredCardNumber=in.next().toCharArray();
                System.out.println("Enter your PIN:");
                char[] enteredPIN=in.next().toCharArray();
                boolean wrong=false;
                int[] enteredCardNumberInt=new int[10];
                if(enteredCardNumber.length==16) {
                    for (int i = 0; i < 10; ++i) {
                        enteredCardNumberInt[i] = enteredCardNumber[i + 6] - 48;
                    }
                }
                else wrong=true;

                for (int i = 0; i < 10; ++i) {
                    if (enteredCardNumberInt[i] != cardNumber[i]) {
                        wrong = true;
                        break;
                    }
                }

                for(int i=0;i<4;++i){
                    if(enteredPIN[i]-48!=PIN[i]){
                        wrong=true;
                        break;
                    }
                }


                if(!wrong){

                    System.out.println("You have successfully logged in!\n");
                    while(true) {

                        System.out.println("1. Balance");
                        System.out.println("2. Log out");
                        System.out.println("0. Exit");

                        int choice=in.nextInt();

                        if(choice==1){
                            System.out.println("\nBalance: 0\n");
                        }
                        if(choice==2){
                            System.out.println("You have successfully logged out!\n");
                            break;
                        }
                        if(choice==0){
                            exit=true;
                            break;
                        }

                    }

                }
                else{
                    System.out.println("Wrong card number or PIN!\n");
                }

            }

            if(exit || n==0){
                System.out.println("Bye!");
                break;
            }

        }

    }

    public static void checkSumGenerator(int[] cardNumber){
        int sum=8;
        for(int i=0;i<9;++i){
            int temp=cardNumber[i];
            if(i%2==0) temp*=2;
            if(temp>9) temp-=9;
            sum+=temp;
            System.out.print(cardNumber[i]);
        }
        cardNumber[9]=10-sum%10;
        System.out.print(cardNumber[9]);
    }
}
