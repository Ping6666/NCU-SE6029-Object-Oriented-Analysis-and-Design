import VideoTypes.VideoType;

public class Movie implements Cloneable {
    VideoType videoType;
    String movieName;

    public Movie(String name, VideoType videoType) {
        this.movieName = name;
        this.videoType = videoType;
    }

    public Object clone() throws CloneNotSupportedException {
        return (Movie) super.clone();
    }

    public String getName() {
        return movieName;
    }

    public void setVideoType(VideoType videoType) {
        this.videoType = videoType;
    }
}
