import java.util.Vector;

import Cars.*;
import GuineaPigs.GuineaPigCarCar;

public class game {
    static Vector<GuineaPigCarCar> theGPCCs;

    public static void main(String[] args) {
        theGPCCs = new Vector<GuineaPigCarCar>();
        theGPCCs.add(new GuineaPigCarCar("Shiromo", new Policecar()));
        theGPCCs.add(new GuineaPigCarCar("Abbey", new Ambulance()));
        theGPCCs.add(new GuineaPigCarCar("Teddy", new TrashTrunck()));

        RunRunGuineaPigCarCarRace handler_RRGPCC = new RunRunGuineaPigCarCarRace(theGPCCs);
        handler_RRGPCC.startGame();
    }
}
