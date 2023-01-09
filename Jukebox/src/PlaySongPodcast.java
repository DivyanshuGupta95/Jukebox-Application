import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class PlaySongPodcast {
    Connection con;
    public PlaySongPodcast() {
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Jukebox", "root", "password");

        }
        catch (SQLException se){
            System.out.println(se);
        }
    }

    Scanner sc = new Scanner(System.in);
    ArrayList<Songs> songlist = new ArrayList<Songs>();
    ArrayList<Podcast> podcastList = new ArrayList<Podcast>();


    public void songPlaylist(String userId){
        System.out.println("Enter Playlist Id");
        int pId = sc.nextInt();

        try{
            String q ="Select s.songid,s.title, s.genre, ar.ArtistName, a.albumname, s.duration, s.url\n" +
                    "FROM songlist s inner join album a on\n" +
                    "a.albumid = s.albumid inner join artist ar on\n" +
                    "s.artistid = ar.artistid inner join songplaylist sp \n" +
                    "on s.songid = sp.songid WHERE sp.playlistid = ?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1,pId);
            ResultSet rs1 = ps.executeQuery();
            System.out.format("%-10s %-30s %-15s %-30s %-9s %35s", "Song ID", "Title of the song","Genre", "Artist Name", "Album", "Duration\n");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");

            while (rs1.next()){
                System.out.format("%-10s %-30s %-15s %-30s %-20s %20s",rs1.getInt(1),rs1.getString(2),
                        rs1.getString(3),rs1.getString(4),rs1.getString(5), rs1.getTime(6)+ "\n");


                Songs songs = new Songs(rs1.getInt(1),rs1.getString(2),
                        rs1.getString(3),rs1.getString(4),rs1.getString(5),
                        rs1.getTime(6).toString(), rs1.getString(7));

                songlist.add(songs); // will be used to play songs.
            }
            playSong(userId); // This method will play songs

        }
        catch (SQLException se){
            System.out.println(se);
        }
    }
    public void podcastPlaylist(String userId){
        System.out.println("Enter playlist id: ");
        int pId = sc.nextInt();

        try{
            String q1 = " SELECT e.episodeid, p.hostname, e.podcastname, e.seasonnum, e.episodenum,e.topic,e.guestname,\n" +
                    "e.category,e.releasedate,e.duration, e.url\n" +
                    "from episode e inner join podcast p\n" +
                    "on e.hostid = p.hostid inner join PodcastPlaylist pp\n" +
                    "on e.episodeid = pp.episodeid where pp.playlistid = ?";
            PreparedStatement ps = con.prepareStatement(q1);
            ps.setInt(1,pId);
            ResultSet rs1 = ps.executeQuery();
            System.out.format("%-15s %-15s %-30s %-10s %-10s %-40s %-20s %-17s %-8s %20s ", "Episode ID", "Host", "Podcast Title", "Season", "Episode","Topic","Guest Name", "Category","Release Date", "Duration\n");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            while (rs1.next()){
                System.out.format("%-15s %-15s %-30s %-10s %-10s %-40s %-20s %-17s %-10s %20s",
                        rs1.getInt(1),rs1.getString(2),rs1.getString(3),
                        rs1.getInt(4),rs1.getInt(5),rs1.getString(6),
                        rs1.getString(7),rs1.getString(8),rs1.getDate(9),
                        rs1.getTime(10)+"\n");


                Podcast podcast = new Podcast( rs1.getInt(1),rs1.getString(2),rs1.getString(3),
                        rs1.getInt(4),rs1.getInt(5),rs1.getString(6),
                        rs1.getString(7),rs1.getString(8),rs1.getDate(9).toString(),
                        rs1.getTime(10).toString(), rs1.getString(11));
                podcastList.add(podcast);
            }
            playPodcast(userId); // this class will play podcast.
        }
        catch (SQLException se){
            System.out.println(se);
        }
    }
    public void playSong(String userId) {
        for (;;) {
            System.out.println("Enter song ID of the song to be played: ");
            System.out.println("Press 0 to return back: ");
            System.out.println("Enter choice: ");
            int ch1 = sc.nextInt();
            UserMenu userMenu = new UserMenu();
            Songs song;
            int flag = 0;
            if (ch1 == 0) {
                System.out.println("returning back");
                userMenu.viewPlaylist(userId);
            }

            else{
                Iterator<Songs> i = songlist.iterator();
                while (i.hasNext()){
                    song = (Songs) i.next();
                    if(song.getSongid() == ch1){
                        flag = 1;
                        try{
                        playMusic(song.getUrl(), song.getTitle(),userId, "song");
                        }
                        catch (Exception e){
                            System.out.println(e);
                        }
                    }

                }
                if(flag == 0){
                    System.out.println("Wrong input");

                }
            }
        }
    }
    public void playPodcast(String userId){
        UserMenu userMenu = new UserMenu();
        int flag = 0;
        for (;;){
            System.out.println("Enter Episode ID of the podcast to be played: ");
            System.out.println("Press 0 to return back: ");
            System.out.println("Enter choice: ");
            int ch1 = sc.nextInt();
            if(ch1 == 0){
                System.out.println("returning back");
                userMenu.viewPlaylist(userId);
            }
            else{
                Podcast podcast;
                Iterator<Podcast> i = podcastList.iterator();
                while (i.hasNext()){
                    podcast = (Podcast) i.next();
                    if(podcast.getEpisodeId() == ch1){
                        flag = 1;
                        try{
                            playMusic(podcast.getUrl(), podcast.getTopic(),userId, "podcast");
                        }
                        catch (Exception e){
                            System.out.println(e);
                        }
                    }
                }
                if(flag == 0){
                    System.out.println("Wrong input");
                }
            }

        }
    }

    public void playMusic(String url, String name, String userId, String prevMenu) throws EmptyListException{

        try {
            Scanner sc = new Scanner(System.in);
            File file = new File(url); //IOException,


            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file); //UnsupportedAudioFileException
            Clip clip = AudioSystem.getClip(); // LineUnavailableException
            clip.open(audioStream);

            while (true) {
                System.out.println("1: Play");
                System.out.println("2: Stop");
                System.out.println("3: Reset");
                System.out.println("4: Jump to specific time");
                System.out.println("5: Fast Forward");
                System.out.println("6: Fast Reverse");
                System.out.println("7: Play next "+ prevMenu);
                System.out.println("8: Play previous "+ prevMenu);
                System.out.println("9: Return to previous menu");
                System.out.println("10: Quit");

                int response = sc.nextInt();
                switch (response) {
                    case 1:
                        System.out.println("Now Playing: " + name);
                        clip.start();
//                        while (clip.getMicrosecondPosition()<= 10000000){ // Display remaining time for 10 seconds.
//                            System.out.println((clip.getMicrosecondLength()-clip.getMicrosecondPosition())/60000000f);
//                        }
                        if(clip.getMicrosecondLength() == clip.getMicrosecondPosition()) {
                            //playSong(userId);// return back after playing the song
                            // nextSong(url) or nextPodcast(url)
                            clip.stop();
                            nextItem(url,prevMenu,userId); // automatically play next song or podcast.
                        }
                        break;
                    case 2:
                        clip.stop();
                        break;
                    case 3:
                        clip.setMicrosecondPosition(0);
                        break;
                    case 4:
                        System.out.println("Enter seconds");
                        long s = sc.nextInt();
                        if (s > 0 && s <= clip.getMicrosecondLength()*1000000f)
                            clip.setMicrosecondPosition(clip.getMicrosecondPosition() + s*1000000);
                            //clip.start();
                        break;
                    case 5:
                        clip.setMicrosecondPosition(clip.getMicrosecondPosition() + 10000000); // 10sec
                        break;
                    case 6:
                        clip.setMicrosecondPosition(clip.getMicrosecondPosition() - 10000000);
                        break;
                    case 7:
                        clip.stop();
                        nextItem(url,prevMenu,userId);
                        break;
                    case 8:
                        clip.stop();
                        prevItem(url,prevMenu,userId);
                        break;
                    case 9:
                        clip.stop();
                        if(prevMenu.equalsIgnoreCase("podcast"))
                            playPodcast(userId);
                        else if(prevMenu.equalsIgnoreCase("song"))
                            playSong(userId);
                        break;
                    case 10: //nextItem(url, "song", userId);
                        System.exit(0);
                    default:
                        System.out.println("wrong input");

                }
            }
        }
        catch (UnsupportedAudioFileException afe){
            System.out.println("Can play wav. format only");
        }
        catch (IOException ie){
            System.out.println(ie);
        }
        catch (LineUnavailableException le){
            System.out.println("Unavailable content");
        }
    }
    //public void startMusic()
    public void nextItem(String url, String type, String userId) throws EmptyListException{

        if(type.equalsIgnoreCase("song")){ // Song playlist
            Songs song;
            Iterator<Songs> i = songlist.iterator();
            while (i.hasNext()){
                song = (Songs) i.next();
                if(song.getUrl().equalsIgnoreCase(url)){
                    try{
                        if(i.hasNext()){
                            song = (Songs) i.next();
                            playMusic(song.getUrl(), song.getTitle(),userId, "song");
                        }
                        else
                            throw new EmptyListException("No next "+ type+ " in the playlist");
                    }
                    catch (EmptyListException exp){
                        System.out.println(exp);
                    }
                }

            }
        } else if (type.equalsIgnoreCase("podcast")) {
            Podcast podcast;
            Iterator<Podcast> i = podcastList.iterator();
            while (i.hasNext()){
                podcast = (Podcast) i.next();
                if(podcast.getUrl().equalsIgnoreCase(url)){
                    try{
                        if(i.hasNext()){
                            podcast = (Podcast) i.next();
                            playMusic(podcast.getUrl(), podcast.getTopic(),userId, "podcast");
                        }
                        else
                            throw new EmptyListException("No next "+ type+ " in the playlist");
                    }
                    catch (EmptyListException e){
                        System.out.println(e);
                    }
                }
            }
        }
    }

    public void prevItem(String url, String type, String userId) throws EmptyListException{

        if(type.equalsIgnoreCase("song")){ // Song playlist
            Songs song;
            ListIterator<Songs> i = songlist.listIterator(songlist.size());
            while (i.hasPrevious()){
                song = (Songs) i.previous();
                if(song.getUrl().equalsIgnoreCase(url)){
                    try{
                        if(i.hasPrevious()){
                            song = (Songs) i.previous();
                            playMusic(song.getUrl(), song.getTitle(),userId, "song");
                        }
                        else
                            throw new EmptyListException("No previous "+ type+ " in the playlist");
                    }
                    catch (EmptyListException e){
                        System.out.println(e);
                    }
                }

            }
        }
        else if(type.equalsIgnoreCase("podcast")){
            Podcast podcast;
            ListIterator<Podcast> i = podcastList.listIterator(podcastList.size());

            while (i.hasPrevious()){
                podcast = (Podcast) i.previous();
                if(podcast.getUrl().equalsIgnoreCase(url)){
                    try{
                        if(i.hasPrevious()){
                            podcast = (Podcast) i.previous();
                            playMusic(podcast.getUrl(), podcast.getTopic(),userId, "podcast");
                        }
                        else
                            throw new EmptyListException("No previous "+ type+ " in the playlist");
                    }
                    catch (EmptyListException e){
                        System.out.println(e);
                    }
                }
            }
        }
    }

}

