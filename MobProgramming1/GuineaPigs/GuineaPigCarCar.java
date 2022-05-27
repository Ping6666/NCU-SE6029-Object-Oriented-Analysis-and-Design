package GuineaPigs;

import java.util.ArrayList;

import Cars.*;

public class GuineaPigCarCar extends GuineaPig {
    public Car car;
    ArrayList<String> foods = new ArrayList<String>();

    public GuineaPigCarCar(String name, Car car) {
        super(name);
        this.car = car;
    }

    public void eat(String food) {
        super.eat(food);
        this.foods.add(food);

        switch (food) {
            case "carrot":
                this.car.accelerate();
                break;
            default:
                break;
        }

        if (foods.size() > 5) {
            this.pupu();
        }
    }

    @Override
    public void pupu() {
        for (String food : foods) {
            System.out.println(food);
        }
        this.car.resetSpeed();
        this.foods.clear();
    }
}
