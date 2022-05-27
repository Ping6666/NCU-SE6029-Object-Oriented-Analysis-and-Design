package VideoTypes;

public abstract class VideoType {
	protected float price, point, delayPrice;
	protected int rentableDay;

	public VideoType() {
	}

	public float getPrice(int days) {
		Float totalPrice = price;
		if (days > rentableDay) {
			days -= rentableDay;
			totalPrice += delayPrice * days;
		}
		return totalPrice;
	}

	public float getPoint() {
		return point;
	}
}
