#include <math.h>
#include <Arduino.h>  // Include this for the Arduino String class

class Vector2D {
public:
    double x;
    double y;

    Vector2D(double x = 0, double y = 0) : x(x), y(y) {}

    void set(double x, double y) {
        this->x = x;
        this->y = y;
    }

    void set(const Vector2D &v) {
        this->x = v.x;
        this->y = v.y;
    }

    void setZero() {
        x = 0;
        y = 0;
    }

    double getLength() const {
        return sqrt(x * x + y * y);
    }
    
    double getLengthSq() const {
        return (x * x + y * y);
    }

    double getAngle() const {
        return atan2(y, x);
    }

    void add(const Vector2D &v) {
        this->x += v.x;
        this->y += v.y;
    }

    void subtract(const Vector2D &v) {
        this->x -= v.x;
        this->y -= v.y;
    }

    static Vector2D toCartesian(double magnitude, double angle) {
        return Vector2D(magnitude * cos(angle), magnitude * sin(angle));
    }

    String toString() const {
        return "Vector2d[" + String(x) + ", " + String(y) + "]";
    }
};
