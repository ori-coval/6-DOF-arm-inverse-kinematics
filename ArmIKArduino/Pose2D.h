class Pose2D {
private:
    double x;
    double y;

public:
    Pose2D(double x = 0, double y = 0) : x(x), y(y) {}

    void set(double x, double y) {
        this->x = x;
        this->y = y;
    }

    void set(const Pose2D &pose) {
        this->x = pose.x;
        this->y = pose.y;
    }

    double getX() const {
        return x;
    }

    double getY() const {
        return y;
    }

    void add(const Pose2D &pose) {
        x += pose.x;
        y += pose.y;
    }

    void add(double x, double y) {
        this->x += x;
        this->y += y;
    }

    void subtract(const Pose2D &pose) {
        x -= pose.x;
        y -= pose.y;
    }

    void subtract(double x, double y) {
        this->x -= x;
        this->y -= y;
    }
};
