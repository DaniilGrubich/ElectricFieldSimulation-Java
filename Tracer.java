// Inheritance is used to create a tracer class that is a subclass of the Charge class.
// The tracer class is used to create a grid of tracers that follow the electric field lines.

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