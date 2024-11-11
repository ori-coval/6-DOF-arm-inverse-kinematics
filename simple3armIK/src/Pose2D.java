public class Pose2D {

    private double x;
    private double y;

    public Pose2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }   

    public void set(Pose2D pose) {
        this.x = pose.x;
        this.y = pose.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void add(Pose2D pose) {  
        x += pose.x;
        y += pose.y;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }   

    public void subtract(Pose2D pose) {
        x -= pose.x;
        y -= pose.y;
    }

    public void subtract(double x, double y) {  
        this.x -= x;
        this.y -= y;
    }


}
