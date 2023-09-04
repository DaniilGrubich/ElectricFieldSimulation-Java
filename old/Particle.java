public class Particle {
    float x, y;
    float vx, vy;

    int historyDepth = 1;
    float[] xHistory;
    float[] yHistory;

    float q;
    boolean stationary;

    Particle(float x, float y, float q, boolean stationary, int tailLength){
        this.x = x;
        this.y = y;
        this.q = q;
        this.stationary = stationary;

        historyDepth = tailLength;
        xHistory = new float[historyDepth];
        yHistory = new float[historyDepth];
        for (int i = 0; i < historyDepth; i++) {
            xHistory[i] = x;
            yHistory[i] = y;
        }

        vx = 0; vy = 0;
    }
    void applyForce(float Fx, float Fy){
        vx += Fx;
        vy += Fy;

        x += vx;
        y += vy;

        for (int i = 1; i < historyDepth; i++) {
            xHistory[i-1] = xHistory[i];
            yHistory[i-1] = yHistory[i];
        }

        xHistory[historyDepth-1] = x;
        yHistory[historyDepth-1] = y;
    }

    public void setTailLength(int TailLength){
        historyDepth = TailLength;

        xHistory = new float[historyDepth];
        yHistory = new float[historyDepth];
        for (int i = 0; i < historyDepth; i++) {
            xHistory[i] = x;
            yHistory[i] = y;
        }
    }


}
