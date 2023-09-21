class Charge {
    float q;

    float x = 0;
    float y = 0;

    float Vx = 0;
    float Vy = 0;

    float ux = 0;
    float uy = 0;

    boolean needsToBeRemoved = false;
    boolean isTest = false;

    Charge(float x, float y, int charge, boolean isTest) {
        this.x = x;
        this.y = y;

        this.q = charge;
        this.isTest = isTest;
    }

    void applyForce(float Fx, float Fy) {
        Vx += Fx;
        Vy += Fy;

        x += Vx;
        y += Vy;
        
    }

    void applyVelocity(float x, float y) {
        float V = 2;

        this.x += V * ux;
        this.y += V * uy;
    }

    boolean equals(Charge c) {
        return c.x == x && c.y == y;
    }

}
