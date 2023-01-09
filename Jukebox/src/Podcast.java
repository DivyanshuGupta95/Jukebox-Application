public class Podcast {
    int episodeId;
    String hostName;
    String  podcastName;
    int seasonNum;
    int episodeNum;
    String topic;
    String guestName;
    String category;
    String releaseDate;
    String duration;
    String url;

    public Podcast(int episodeId, String hostName, String podcastName, int seasonNum, int episodeNum, String topic,
                   String guestName, String category, String releaseDate, String duration, String url) {
        this.episodeId = episodeId;
        this.hostName = hostName;
        this.podcastName = podcastName;
        this.seasonNum = seasonNum;
        this.episodeNum = episodeNum;
        this.topic = topic;
        this.guestName = guestName;
        this.category = category;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public String getHostName() {
        return hostName;
    }

    public String getPodcastName() {
        return podcastName;
    }

    public int getSeasonNum() {
        return seasonNum;
    }

    public int getEpisodeNum() {
        return episodeNum;
    }

    public String getTopic() {
        return topic;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getCategory() {
        return category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getDuration() {
        return duration;
    }
}
