import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import GuineaPigs.GuineaPigCarCar;

public class RunRunGuineaPigCarCarRace {
    private Map<GuineaPigCarCar, Double> GPCC_map; // GPCC map to distance
    private Vector<GuineaPigCarCar> GPCCs;
    private GuineaPigCarCar first_GPCC, last_GPCC;
    private int timer;
    private double raceLength;
    private boolean gameStatus;

    public RunRunGuineaPigCarCarRace(Vector<GuineaPigCarCar> tmp_GPCCs) {
        this.GPCCs = new Vector<GuineaPigCarCar>();
        for (int i = 0; i < tmp_GPCCs.size(); i++) {
            this.GPCCs.add(tmp_GPCCs.get(i));
        }
        this.timer = 0;
        this.gameStatus = false;
        this.first_GPCC = null;
        this.last_GPCC = null;
        this.raceLength = 4000;
        this.initMap();
    }

    private void initMap() {
        this.GPCC_map = new HashMap<GuineaPigCarCar, Double>();
        for (int i = 0; i < this.GPCCs.size(); i++) {
            this.GPCC_map.put(this.GPCCs.get(i), (double) 0);
        }
    }

    public void startGame() {
        while (!this.gameStatus) {
            this.timer += 1;
            this.gameStatus = gameWorkhouse();
        }

        // print winner
        if (this.first_GPCC != null) {
            System.out.println("Winner: " + this.first_GPCC.name);
        } else {
            // fail checker
            System.out.println("Error!!!");
        }
    }

    public boolean gameWorkhouse() {
        this.first_GPCC = this.last_GPCC = this.GPCCs.get(0);
        for (int i = 0; i < this.GPCCs.size(); i++) {
            double distance = this.GPCC_map.get(this.GPCCs.get(i));
            distance += this.GPCCs.get(i).car.speed;
            this.GPCC_map.put(this.GPCCs.get(i), distance);
            // check first_GPCC
            if (this.first_GPCC != null && this.GPCC_map.get(this.first_GPCC) < distance) {
                this.first_GPCC = this.GPCCs.get(i);
            }
            // check last_GPCC
            if (this.last_GPCC != null && this.GPCC_map.get(this.last_GPCC) > distance) {
                this.last_GPCC = this.GPCCs.get(i);
            }
        }

        System.out.println("Time: " + this.timer);
        // print all car distance
        for (int i = 0; i < this.GPCCs.size(); i++) {
            System.out.print("I'm: " + this.GPCCs.get(i).name);
            System.out.println("; distance is: " + this.GPCC_map.get(this.GPCCs.get(i)));
        }

        if (this.first_GPCC != null && this.GPCC_map.get(this.first_GPCC) >= this.raceLength) {
            return true;
        }

        if (this.timer % 10 == 0) {
            // first_GPCC eat
            if (this.first_GPCC != null) {
                this.first_GPCC.eat("lettuce");
            }
            // last_GPCC eat
            if (this.last_GPCC != null) {
                this.last_GPCC.eat("carrot");
            }
        }

        return false;
    }
}
