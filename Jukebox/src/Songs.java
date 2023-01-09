public class Songs {

    int songid;
    String title, artistName, genre,albumName, duration,url;

    public Songs(int songid, String title, String genre,String artistName, String albumName, String duration, String url) {
        this.songid = songid;
        this.title = title;
        this.genre  = genre;
        this.artistName = artistName;
        this.albumName = albumName;
        this.duration = duration;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public int getSongid() {
        return songid;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getGenre() {
        return genre;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getDuration() {
        return duration;
    }
}
