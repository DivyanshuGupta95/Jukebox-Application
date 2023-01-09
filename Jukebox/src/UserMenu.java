import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

// This class will diplay all menu to user


public class UserMenu {
    Connection con;
    Scanner sc = new Scanner(System.in);
    PlaySongPodcast play = new PlaySongPodcast();
    public UserMenu(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Jukebox", "root", "password");

        }
        catch (SQLException se){
            System.out.println(se);
        }
    }
    public void firstMenu(String userid) {
        Search search = new Search();
        for (;;) {
            System.out.println("1. Show all Songs");
            System.out.println("2. Show all Podcasts");
            System.out.println("3. Create new Playlist");
            System.out.println("4. Add Songs or Podcasts in the Playlist");
            System.out.println("5. View all Playlists");
            System.out.println("6. Search Songs/Podcasts");
            System.out.println("7. Return back to main menu");
            System.out.println("8. Exit Application");
            System.out.println("Enter choice: ");
            int ch = sc.nextInt();
            switch (ch) {
                case 1:
                    showAllSongs();
                    break;
                case 2:
                    showAllPodcasts();
                    break;
                case 3:
                    createPlaylist(userid);
                    break;
                case 4:
                    addInfo(userid); // pass the arraylist to another class to play songs and other operations
                    break;
                case 5:
                    viewPlaylist(userid);
                    break;
                case 6:
                    search.searchMenu(userid);
                    break;
                case 7:
                    Menu mainmenu = new Menu();
                    mainmenu.mainMenu();
                case 8:
                    System.out.println("Exiting application");
                    System.exit(0);
                default:
                    System.out.println("Enter valid choice");
                    firstMenu(userid);
            }
        }
    }

    public void showAllSongs(){
        String q1 = "Select s.songid,s.title, s.genre, ar.ArtistName, a.albumname, s.duration\n" +
                "FROM songlist s inner join album a on\n" +
                "a.albumid = s.albumid inner join artist ar on\n" +
                "s.artistid = ar.artistid";
        try{
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery(q1);
            System.out.format("%-10s %-30s %-15s %-30s %-9s %35s", "Song ID", "Title of the song","Genre", "Artist Name", "Album", "Duration\n");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");

            while (rs1.next()){
                System.out.format("%-10s %-30s %-15s %-30s %-20s %20s",rs1.getInt(1),rs1.getString(2),
                        rs1.getString(3),rs1.getString(4),rs1.getString(5), rs1.getTime(6)+ "\n");
            }
        }
        catch (SQLException se){
            System.out.println(se);
        }
    }
    public void showAllPodcasts(){
        String q1 = "SELECT e.episodeid, p.hostname, e.podcastname, e.seasonnum, e.episodenum,e.topic,e.guestname," +
                "e.category,e.releasedate,e.duration\n" +
                "from episode e inner join podcast p\n" +
                "on e.hostid = p.hostid";
        try{
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery(q1);
            System.out.format("%-15s %-15s %-30s %-10s %-10s %-40s %-20s %-17s %-8s %20s ", "Episode ID", "Host", "Podcast Title", "Season", "Episode","Topic","Guest Name", "Category","Release Date", "Duration\n");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            while (rs1.next()){
                System.out.format("%-15s %-15s %-30s %-10s %-10s %-40s %-20s %-17s %-10s %20s",
                        rs1.getInt(1),rs1.getString(2),rs1.getString(3),
                        rs1.getInt(4),rs1.getInt(5),rs1.getString(6),
                        rs1.getString(7),rs1.getString(8),rs1.getDate(9),
                        rs1.getTime(10)+"\n");
            }
        }
        catch (SQLException se){
            System.out.println(se);
        }
    }
    public void createPlaylist(String userId){
        System.out.println("Enter playlist Id");
        int pId = sc.nextInt();
        int flag = validatePlaylistId(pId);
        if(flag == 0){
            String q = "INSERT INTO Playlist VALUES(?,?)";
            try {
                PreparedStatement ps = con.prepareStatement(q);
                ps.setInt(1,pId);
                ps.setString(2,userId);
                int res = ps.executeUpdate();
                if(res == 0)
                    System.out.println("Insertion failed");
                else
                    System.out.println("Playlist Created");
            }
            catch (SQLException se){
                System.out.println(se);
            }
        }
        else {
            System.out.println("Playlist Id already Exists.\nRetuning Back");
            firstMenu(userId);

        }
    }
    public void addInfo(String userId){
        System.out.println("Enter playlist Id");
        int pId = sc.nextInt();
        int flag = validatePlaylistId(pId);
        if(flag == 0){
            System.out.println("Playlist Doesn't Exists\nReturning Back");
            firstMenu(userId);
        }
        else {
            // Create playlist and
            int flag1 =0, flag2= 0; // To Display song list and Podcast list only once
            for(;;) {
                System.out.println("1. Add Songs to Playlist");
                System.out.println("2. Add Podcasts to Playlist");
                System.out.println("3. Return back");
                System.out.print("Enter choice");
                int ch = sc.nextInt();
                switch (ch){
                    case 1: // Adds songs to table
                        for (;;){
                            if(flag1 == 0) {
                                showAllSongs();
                                flag1 = 1;
                            }
                            System.out.println("Enter Song Id ");
                            System.out.println("Press 0 to return back");
                            System.out.print("Enter choice:  ");
                            int ch1 = sc.nextInt();
                            if(ch1 == 0)
                                firstMenu(userId);
                            else
                                addSongs(ch1, userId,pId); // method that adds songs

                        }
                    case 2: // Add Podcast to table
                        for (;;){
                            if(flag2 == 0) {
                                showAllPodcasts();
                                flag2 = 1;
                            }
                            System.out.println("Enter Episode Id ");
                            System.out.println("Press 0 to return back");
                            System.out.print("Enter choice:  ");
                            int ch1 = sc.nextInt();
                            if(ch1 == 0)
                                firstMenu(userId);
                            else
                                addPodcast(ch1,userId,pId); // method that adds podcasts

                        }
                    case 3:
                        System.out.println("Returning back");
                        firstMenu(userId);
                    default:
                        System.out.println("Invalid choice: ");
                        System.out.println("Returning back");
                        firstMenu(userId);
                }
            }
        }
    }
    public void addSongs(int songId, String userid, int pId){
        int artistId = 0, albumid = 0;
        String q1 = "SELECT artistId, AlbumId from songlist WHERE songid = ?";
        try {
            PreparedStatement ps = con.prepareStatement(q1);
            ps.setInt(1,songId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                artistId = rs.getInt(1);
                albumid = rs.getInt(2);
            }
            if(albumid == 0 && artistId == 0){
                System.out.println("Invalid Song Id\nReturning back to main menu");
                firstMenu(userid);
            }
            String q2 = "INSERT INTO Songplaylist VALUES(?,?,?,?,?)";
            PreparedStatement ps1 = con.prepareStatement(q2);
            ps1.setInt(1,pId);
            ps1.setString(2,userid);
            ps1.setInt(3,songId);
            ps1.setInt(4,artistId);
            ps1.setInt(5,albumid);
            int res = ps1.executeUpdate();
            if(res == 0)
                System.out.println("Insertion failed");
            else
                System.out.println("**** Song Added ****\n");
        }
        catch (SQLException se){
            System.out.println(se);
        }

    }
    public void addPodcast(int episodeId,String userId,int pId){
        int hostId = 0;
        System.out.println(pId);
        String q1 = "SELECT hostId from episode WHERE episodeid = ?";
        try {
            PreparedStatement ps = con.prepareStatement(q1);
            ps.setInt(1,episodeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                hostId = rs.getInt(1);
            }

            if(hostId == 0 ){
                System.out.println("Invalid Podcast Id\nReturning back to main menu");
                firstMenu(userId);
            }
            String q2 = "INSERT INTO PodcastPlaylist VALUES(?,?,?,?)";
            PreparedStatement ps1 = con.prepareStatement(q2);
            ps1.setInt(1,pId);
            ps1.setString(2,userId);
            ps1.setInt(3,episodeId);
            ps1.setInt(4,hostId);
            int res = ps1.executeUpdate();
            if(res == 0)
                System.out.println("Insertion failed");
            else
                System.out.println("**** Podcast Added ****\n");
        }
        catch (SQLException se){
            System.out.println(se);
        }
    }
    public void viewPlaylist(String userId){
        PlaySongPodcast play = new PlaySongPodcast();
        for(;;){
            System.out.println("1. Songs Playlist");
            System.out.println("2. Podcast Playlist");
            System.out.println("3. Return Back");
            int ch = sc.nextInt();
            switch (ch){
                case 1:
                   play.songPlaylist(userId);
                case 2:
                    play.podcastPlaylist(userId);
                case 3:
                    System.out.println("Returning Back");
                    firstMenu(userId);
                default:
                    System.out.println("Invalid choice");
                    firstMenu(userId);
            }

        }

    }


    public int validatePlaylistId(int playlistId){ // Return 0 if not exists, 1 if exists
        int Flag = 0;
        try{
            String q = "SELECT PlaylistId from Playlist";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(q);
            while(rs.next()){
                if(rs.getInt(1) == playlistId)
                    Flag = 1;
            }
        }
        catch (SQLException se) {
            System.out.println(se);
        }
        return Flag;
    }
}
