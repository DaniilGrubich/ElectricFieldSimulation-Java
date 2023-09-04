import java.util.ArrayList;

public class ParticleSystem {

    private static ArrayList<Particle> particles = new ArrayList<>();
    private static float minDistanceBetweenParticles = 2;

    public static float nonStationaryChargeMuliplyer = 1;
    public static int tailLength = 1;


    public static void integrateNonStationaryParticles(){

        //integrate though non stationary particles
        for (int i = 0; i < particles.size(); i++) {
            Particle nonstParticle = particles.get(i);
            if(nonstParticle.stationary) continue;

            float Fx = 0; float Fy = 0;
            //integrate though stationary particles
            for (int j = 0; j < particles.size(); j++) {
                Particle stParticle = particles.get(j);
                if(!stParticle.stationary) continue;

                float r = (float) Math.hypot(nonstParticle.x - stParticle.x, nonstParticle.y - stParticle.y);
                float F;
                if (r <= minDistanceBetweenParticles) {
                    //remove non stationary particle and break out of the loop. At the same time, decreasing i counter by one
                    particles.remove(i--);
                    break;
                }else
                    F = nonstParticle.q * stParticle.q * nonStationaryChargeMuliplyer / r / r;

                Fx += F * (nonstParticle.x - stParticle.x) / r;
                Fy += F * (nonstParticle.y - stParticle.y) / r;

            }
            nonstParticle.applyForce(Fx, Fy);
        }

    }

    public static void clearDistantParticles(int w, int h){
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            float x = p.x; float y = p.y;
            if(x>w || x < 0) {particles.remove(i--); continue;}
            if(y>h || y < 0) {particles.remove(i--); continue;}
        }
    }

    public static void setTailLength(int tailLengthP){
        tailLength = tailLengthP;
        for(int i = 0; i < particles.size(); i++){
            Particle particle = particles.get(i);
            particle.setTailLength(tailLength);
        }
    }
    public static void addNewParticle(int x, int y, float charge, boolean stationary){
        particles.add(new Particle(x, y, charge, stationary, tailLength));
    }
    public static ArrayList<Particle> getParticles() {
        return particles;
    }
}
