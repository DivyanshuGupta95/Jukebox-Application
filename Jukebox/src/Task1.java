import java.sql.*;
import java.util.Scanner;

public class Task1 {
    Scanner sc = new Scanner(System.in);
    Connection con;
    Menu menu = new Menu();
    public Task1(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Jukebox", "root", "password");
        }
        catch (SQLException se){
            System.out.println(se);
        }
    }
    public void userLogin(){
        UserMenu userMenu = new UserMenu();
        System.out.println("Enter User ID");
        String userId = sc.next();
        int flag1 = validateUserID(userId);
        System.out.println("Enter Password");
        String password = sc.next();
        int flag2 = validatePassword(password);
        if(flag2 ==1 && flag1 ==1){
            System.out.println("Welcome User");
            userMenu.firstMenu(userId);
        }
        else{
            System.out.println("Invalid user Id or Password.");
            System.out.println("Returning Back\n");
            menu.mainMenu();
        }
    }
    public void createNewUser(){

        System.out.println("Welcome new user");
        System.out.println("Enter your name: ");
        String name = sc.next();
        sc.nextLine();
        System.out.println("Enter your City:");
        String city = sc.next();
        System.out.println("Enter your Country: ");
        String country = sc.next();
        System.out.println("Enter your Phone Number: ");
        String pNum = sc.next();
        //int c = 0;
        String userId = "";
        for(;;){
            System.out.println("Enter user id: ");
            userId = sc.next();
           int c = validateUserID(userId);
            System.out.println(c);
            if(c==1)
                System.out.println("User Id already exists.. Try  another userId");
            else if(c==0)
                break;
        }

        System.out.println("Enter Password: ");
        String pass = sc.next();

        try{
            String q = "INSERT INTO userDetails VALUES(?,?,?,?,?,?)";
            PreparedStatement ps =con.prepareStatement(q);
            ps.setString(1,userId);
            ps.setString(2,name);
            ps.setString(3,city);
            ps.setString(4,country);
            ps.setString(5,pNum);
            ps.setString(6,pass);
            int res = ps.executeUpdate();
            if(res == 0)
                System.out.println("Insertion failed");
            else
                System.out.println("User account created. Enjoy listening to great Songs and Podcasts");
        }
        catch (SQLException se){
            System.out.println(se);
        }
    }
    public int validateUserID(String userid){
    /*
    * This methods takes user id , check with database.
    * If the user id exists it returns 1 otherwise it returns 0.
    */
        int Flag = 0;
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Jukebox", "root", "password");
            String q = "SELECT userid from userdetails";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(q);
            while(rs.next()){
                if(rs.getString(1).equals(userid))
                    Flag = 1;
            }
        }
        catch (SQLException se) {
            System.out.println(se);
        }
        return Flag;
    }
    public int validatePassword(String password){
        int Flag = 0;
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Jukebox", "root", "password");
            String q = "SELECT Password from userdetails";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(q);
            while(rs.next()){
                if(rs.getString(1).equals(password))
                    Flag = 1;
            }
        }
        catch (SQLException se) {
            System.out.println(se);
        }
        return Flag;
    }

}
