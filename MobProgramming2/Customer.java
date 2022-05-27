import java.util.ArrayList;

public class Customer {
    public String name;
    private ArrayList<Rental> rentalRecords = new ArrayList<Rental>();

    public Customer(String name) {
        this.name = (name == null) ? "" : name;
    }

    public void statement() {
        float totalmoney = 0, totalpoint = 0;
        for (Rental rental : rentalRecords) {
            rental.MovieInfo();
            totalmoney = totalmoney + rental.getPrice();
            totalpoint = totalpoint + rental.getPoint();
        }
        System.out.println("totalmoney=" + totalmoney);
        System.out.println("totalpoint =" + totalpoint);
    }

    public void addRental(Rental rental) {
        this.rentalRecords.add(rental);
    }
}
