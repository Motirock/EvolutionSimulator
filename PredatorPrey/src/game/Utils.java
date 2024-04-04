package game;

public class Utils {
    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public static double unsgmoid(double x) {
        return Math.log(x / (1 - x));
    }
}
