package game;

public class Vector2D {
    public double x = 0.0;
    public double y = 0.0;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this(0.0, 0.0);
    }

    public double distance(Vector2D p) {
        return Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2));
    }

    public double angle(Vector2D p) {
        double theta = Math.acos((x*p.x+y*p.y)/(magnitude()*p.magnitude()));
        return theta > Math.PI ? -(2*Math.PI-theta) : theta;
    }

    public double angle0_2PI(Vector2D p) {
        return Math.acos((x*p.x+y*p.y)/(magnitude()*p.magnitude()));
    }

    public Vector2D add(Vector2D p) {
        return new Vector2D(x + p.x, y + p.y);
    }

    public Vector2D subtract(Vector2D p) {
        return new Vector2D(x - p.x, y - p.y);
    }

    public Vector2D multiply(double s) {
        return new Vector2D(x * s, y * s);
    }

    public Vector2D divide(double s) {
        return new Vector2D(x / s, y / s);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D normalize() {
        return divide(magnitude());
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public Vector2D limit(double magnitude) {
        if (this.magnitude() > magnitude) {
            return this.normalize().multiply(magnitude);
        }
        return this;
    }
}
