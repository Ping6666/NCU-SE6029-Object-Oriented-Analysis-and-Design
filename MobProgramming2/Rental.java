public class Rental {
    private int RentedDay;
    private Movie Movie;

    public Rental(Movie movie, int daysRented) throws CloneNotSupportedException {
        this.RentedDay = daysRented;
        this.Movie = (Movie) movie.clone();
    }

    public void MovieInfo() {
        float totalPrice = Movie.videoType.getPrice(RentedDay);
        float totalPoint = Movie.videoType.getPoint();
        System.out.print("Movie Name:");
        System.out.print(Movie.movieName);
        System.out.print("  Rented Days:");
        System.out.print(RentedDay);
        System.out.print("  Price:");
        System.out.print(totalPrice);
        System.out.print("  Point:");
        System.out.println(totalPoint);
    }

    public float getPrice() {
        return Movie.videoType.getPrice(RentedDay);
    }

    public float getPoint() {
        return Movie.videoType.getPoint();
    }
}
