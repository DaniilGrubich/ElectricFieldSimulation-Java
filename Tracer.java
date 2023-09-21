class Tracer extends Charge{
    Tracer(float x, float y) {
        super(x, y, 1, true);
    }

    @Override
    void applyForce(float Fx, float Fy) {
        ux = (float) (Fx / Math.hypot(Fx, Fy));
        uy = (float) (Fy / Math.hypot(Fx, Fy));
        
        x += ux;
        y += uy; 
    }
}