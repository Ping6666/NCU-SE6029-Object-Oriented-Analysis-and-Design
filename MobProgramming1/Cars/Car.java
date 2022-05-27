package Cars;

public class Car {
    public int initSpeed, speed, accelerateNum;

    public void accelerate() {
        this.speed += this.accelerateNum;
    }

    public void resetSpeed() {
        this.speed = this.initSpeed;
    }
}
