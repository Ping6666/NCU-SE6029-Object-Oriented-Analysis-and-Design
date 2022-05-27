package GuineaPigs;

public class GuineaPig {
    public String name;
    public int foodCount;

    GuineaPig(String name) {
        this.name = (name == null) ? "GuineaPig" : name;
        this.foodCount = 0;
    }

    public void noise() {
        System.out.println(this.name + " PuiPui.");
    }

    public void eat(String food) {
        System.out.print(this.name + " eat " + food);

    }

    public void pupu() {
        System.out.println(this.name + " pupu.");
        this.foodCount = 0;
    }
}
