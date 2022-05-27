import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import VideoTypes.Children;
import VideoTypes.NewRelese;
import VideoTypes.Regular;

public class RentingVideoSys {

    public static void main(String[] args) throws CloneNotSupportedException {
        Map<String, Movie> movies;
        movies = new HashMap<String, Movie>();
        movies.put("我的名字", new Movie("我的名字", new NewRelese()));
        movies.put("K-O", new Movie("K-O", new Regular()));
        movies.put("涼宮春日的", new Movie("涼宮春日的", new Regular()));
        movies.put("GIVEN", new Movie("GIVEN", new Children()));

        Map<String, Customer> customers;
        customers = new HashMap<String, Customer>();
        customers.put("倫倫", new Customer("倫倫"));
        customers.put("六花醬", new Customer("六花醬"));
        customers.put("K昂", new Customer("K昂"));
        customers.put("史家瑩", new Customer("史家瑩"));

        // rental records
        customers.get("倫倫").addRental(new Rental(movies.get("GIVEN"), 8));
        customers.get("倫倫").addRental(new Rental(movies.get("我的名字"), 7));
        customers.get("六花醬").addRental(new Rental(movies.get("我的名字"), 8));
        customers.get("六花醬").addRental(new Rental(movies.get("K-O"), 7));
        customers.get("六花醬").addRental(new Rental(movies.get("涼宮春日的"), 8));
        customers.get("K昂").addRental(new Rental(movies.get("K-O"), 7));
        customers.get("史家瑩").addRental(new Rental(movies.get("GIVEN"), 8));

        ArrayList<String> targetNames;
        targetNames = new ArrayList<String>();
        targetNames.add("倫倫");
        targetNames.add("六花醬");
        targetNames.add("K昂");
        targetNames.add("史家瑩");

        // print THE sum of all rental records for some customer
        System.out.println("print THE sum of all rental records for some customer");

        for (String customer_name : targetNames) {
            Customer curr_Customer = customers.get(customer_name);
            System.out.println("curr_Customer: " + curr_Customer.name);
            curr_Customer.statement();
            System.out.println("\n");
        }

        // two months later 我的名字 type 新片 to 舊片。
        movies.get("我的名字").setVideoType(new Regular());
    }
}
