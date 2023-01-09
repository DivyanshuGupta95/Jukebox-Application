import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;

public class Search {
    Connection con;
    ArrayList<Songs> songsArrayList;
    public Search() {
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Jukebox", "root", "password");
        }
        catch (SQLException se){
            System.out.println(se);
        }
        songsArrayList = createSongList();
        creatPoddcastList();
    }

    // create list of songs and podcast

    ArrayList<Podcast> podcastArrayList = new ArrayList<Podcast>();
    Scanner sc = new Scanner(System.in);

    public ArrayList<Songs> createSongList(){
        ArrayList<Songs> mylist  = new ArrayList<>();
        String q1 = "Select s.songid,s.title, ar.ArtistName, a.albumname, s.genre, s.duration, s.url\n" +
                "FROM songlist s inner join album a on\n" +
                "a.albumid = s.albumid inner join artist ar on\n" +
                "s.artistid = ar.artistid";
        try{
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery(q1);

            while (rs1.next()){
                int songid = rs1.getInt(1);
                String title = rs1.getString(2);
                String artistName = rs1.getString(3);
                String albumName = rs1.getString(4);
                String genre = rs1.getString(5);
                String duration = rs1.getString(6);
                String url = rs1.getString(7);

                Songs songs = new Songs( songid, title, genre, artistName, albumName, duration,url);
                mylist.add(songs);
            }
        }
        catch (SQLException se){
            System.out.println(se);
        }
        return mylist;
    }

    public void creatPoddcastList(){
        String q1 = "SELECT e.episodeid, p.hostname, e.podcastname, e.seasonnum, e.episodenum,e.topic,e.guestname," +
                "e.category,e.releasedate,e.duration, e.url\n" +
                "from episode e inner join podcast p\n" +
                "on e.hostid = p.hostid";
        try{
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery(q1);
            while (rs1.next()){
                int episodeId = rs1.getInt(1);
                String hostName = rs1.getString(2);
                String  podcastName = rs1.getString(3);
                int seasonNum = rs1.getInt(4) ;
                int episodeNum = rs1.getInt(5);
                String topic = rs1.getString(6);
                String guestName = rs1.getString(7);
                String category = rs1.getString(8);
                String releaseDate = rs1.getDate(9).toString();
                String duration = rs1.getTime(10).toString();
                String url = rs1.getString(11);
                Podcast podcast = new Podcast(episodeId, hostName, podcastName, seasonNum, episodeNum, topic,
                        guestName, category,releaseDate, duration, url);
                podcastArrayList.add(podcast);
            }
        }
        catch (SQLException se){
            System.out.println(se);
        }
    }
    public void searchMenu(String userId){
        UserMenu userMenu = new UserMenu();
        for (;;) {
            System.out.println("Welcome to the search menu");
            System.out.println("1. Search Songs");
            System.out.println("2. Search Podcast");
            System.out.println("3. Return Back");
            System.out.println("Enter choice;");
            int ch = sc.nextInt();
            switch (ch) {
                case 1: searchSong(userId);
                case 2: searchPodcast(userId);
                case 3: userMenu.firstMenu(userId);
            }
        }
    }

    public void searchSong(String userid){
        for (;;){

            System.out.println("1. Search song by song name");
            System.out.println("2. Search song by Artist name");
            System.out.println("3. Search song by Genre");
            System.out.println("4. Search song by Album Name");
            System.out.println("5. Return Back");
            System.out.println("Enter choice:");
            int ch = sc.nextInt();
            switch (ch){
                case 1: System.out.println("Enter Name of the song");
                    sc.nextLine();
                    String name = sc.next();
                    List<Songs> searchResult1 = searchSongByName(songsArrayList,name);
                    if(!searchResult1.isEmpty()) {
                        System.out.println("All songs matching  "+ name+ ": ");
                        printList(searchResult1);
                    }
                    else{
                        System.out.println("No Matching Songs found");
                        searchSong(userid);
                    }
                    break;

                case 2: System.out.println("Enter name of the Artist: ");
                    sc.nextLine();
                    String artist = sc.next();
                    List<Songs> searchResult2 = searchSongByArtist(songsArrayList, artist);
                    if(!searchResult2.isEmpty()) {
                        System.out.println("All songs matching  "+ artist+ ": ");
                        printList(searchResult2);
                    }
                    else{
                        System.out.println("No Matching Artist found");
                        searchSong(userid);
                    }
                    break;

                case 3:   System.out.println("Enter Genre: ");
                    sc.nextLine();
                    String genre = sc.next();
                    List<Songs> searchResult3 = searchSongByGenre(songsArrayList, genre);
                    if(!searchResult3.isEmpty()) {
                        System.out.println("All songs matching  "+ genre+ ": ");
                        printList(searchResult3);
                    }
                    else{
                        System.out.println("No  Songs matching this genre found");
                        searchSong(userid);
                    }
                    break;

                case 4: System.out.println("Enter name of the Album: ");
                    sc.nextLine();
                    String album = sc.next();
                    List<Songs> searchResult4 = searchSongByAlbum(songsArrayList, album);
                    if(!searchResult4.isEmpty()) {
                        System.out.println("All songs matching  "+ album+ ": ");
                        printList(searchResult4);
                    }
                    else{
                        System.out.println("No Matching Album found");
                        searchSong(userid);
                    }
                    break;

                case 5: searchMenu(userid);
                default:
                    System.out.println("Invalid choice");
                    searchSong(userid);
            }
        }
    }
    public List<Songs> searchSongByName(ArrayList<Songs> songsList,String song){
        Stream<Songs> songStream1 = songsList.stream().filter(val->val.getTitle().toLowerCase().contains(song.toLowerCase()));
        return songStream1.collect(Collectors.toList());
    }
    public List<Songs> searchSongByArtist(ArrayList<Songs> songsList, String artist){

        Stream<Songs> songStream2 = songsList.stream().filter(val->val.getArtistName().toLowerCase().contains(artist.toLowerCase()));
        return songStream2.collect(Collectors.toList());
    }
    public List<Songs> searchSongByGenre(ArrayList<Songs> songsList, String genre){

        Stream<Songs> songStream3 = songsList.stream().filter(val->val.getGenre().toLowerCase().contains(genre.toLowerCase()));
        return songStream3.collect(Collectors.toList());
    }
    public List<Songs> searchSongByAlbum(ArrayList<Songs> songsList, String album){

        Stream<Songs> songStream4 = songsList.stream().filter(val->val.getArtistName().toLowerCase().contains(album.toLowerCase()));
        return songStream4.collect(Collectors.toList());
    }
    public void searchPodcast(String userid){
        for (;;) {

            System.out.println("1. Search Podcast by Celebrity name");
            System.out.println("2. Search Podcast by Release date");
            System.out.println("3. Search Podcast by Name of the show");
            System.out.println("4. Return Back");
            System.out.println("Enter choice;");
            int ch = sc.nextInt();
            switch (ch) {
                case 1:
                    System.out.println("Enter Celebrity Name");
                    sc.nextLine();
                    String celebrity = sc.next();
                    List<Podcast> searchResult1 = searchPodcastByCelebrity(podcastArrayList, celebrity);
                    if(!searchResult1.isEmpty()) {
                        System.out.println("All Podcast matching  "+ celebrity+ ": ");
                        printPodcastlist(searchResult1);
                    }
                    else{
                        System.out.println("No Matching Podcast found");
                        searchPodcast(userid);
                    }
                    break;

                case 2:
                    System.out.println("Enter Release Date");
                    sc.nextLine();
                    String date = sc.next();
                    List<Podcast> searchResult2 = searchPodcastByReleaseDate(podcastArrayList, date);
                    if(!searchResult2.isEmpty()) {
                        System.out.println("All Podcast released on : " + date);
                        printPodcastlist(searchResult2);
                    }
                    else{
                        System.out.println("No Matching Podcast found");
                        searchPodcast(userid);
                    }
                    break;

                case 3:
                    System.out.println("Enter Name of the show");
                    sc.nextLine();
                    String show = sc.next();
                    List<Podcast> searchResult3 = searchPodcastByShow(podcastArrayList, show);
                    if(!searchResult3.isEmpty()) {
                        System.out.println("All Podcast matching Show name"+ show);
                        printPodcastlist(searchResult3);
                    }
                    else{
                        System.out.println("No Matching Podcast found");
                        searchPodcast(userid);
                    }
                    break;
                case 4:searchMenu(userid);
                default:
                    System.out.println("Invalid choice");
                    searchMenu(userid);
            }
        }
    }
    public List<Podcast> searchPodcastByCelebrity(ArrayList<Podcast> podcastList, String celebrity){

        Stream<Podcast> PodcastStream1 = podcastList.stream().filter(val->val.getHostName().toLowerCase().contains(celebrity.toLowerCase()));
        return PodcastStream1.collect(Collectors.toList());

    }
    public List<Podcast> searchPodcastByReleaseDate(ArrayList<Podcast> podcastList, String date){

        Stream<Podcast> PodcastStream2 = podcastList.stream().filter(val->val.getReleaseDate().equalsIgnoreCase(date));
        return PodcastStream2.collect(Collectors.toList());

    }
    public List<Podcast> searchPodcastByShow(ArrayList<Podcast> podcastList, String show){

        Stream<Podcast> PodcastStream3 = podcastList.stream().filter(val->val.getPodcastName().toLowerCase().contains(show.toLowerCase()));
        return PodcastStream3.collect(Collectors.toList());

    }
    public void printList(List<Songs> searchResult){
        System.out.format("%-10s %-30s %-15s %-35s %-9s %35s", "Song ID", "Title of the song","Genre", "Artist Name", "Album", "Duration\n");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
        for(Songs val: searchResult){
             System.out.format("%-10s %-30s %-15s %-35s %-20s %20s",val.getSongid(),val.getTitle(), val.getGenre(),
                    val.getArtistName(),val.getAlbumName(),val.getDuration()+"\n");
        }
    }

    public void printPodcastlist(List<Podcast> searchresult){
        System.out.format("%-15s %-15s %-30s %-10s %-10s %-40s %-20s %-17s %-8s %20s ", "Episode ID", "Host", "Podcast Title", "Season", "Episode","Topic","Guest Name", "Category","Release Date", "Duration\n");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for(Podcast val: searchresult){
            System.out.format("%-15s %-15s %-30s %-10s %-10s %-40s %-20s %-17s %-10s %20s",
                    val.getEpisodeId(),val.getHostName(),val.getPodcastName(),val.getSeasonNum(),val.getEpisodeNum(),
                    val.getTopic(), val.getGuestName(),val.getCategory(),val.getReleaseDate(),val.getDuration()+"\n");
        }
    }
}
