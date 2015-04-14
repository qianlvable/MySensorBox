package com.lvable.mysensorbox;

/**
 * Created by Jiaqi Ning on 9/4/2015.
 */
public class Attractor {
    float mass;
    private float G;

    public void setLocation(float x,float y) {
        location.x = x;
        location.y = y;
    }

    PVector location;

    public Attractor(int x,int y){
        location = new PVector(x,y);
        mass = 25;
        G = 8f;
    }

    PVector attract(Mover m) {
        PVector force = PVector.sub(location,m.location);   // Calculate direction of force
        float d = force.magnitude();                              // Distance between objects
        d = constrain(d,5,25);                        // Limiting the distance to eliminate "extreme" results for very close or very far objects
        force.normalize();                                  // Normalize vector (distance doesn't matter here, we just want this vector for direction)
        float strength = (G * mass * m.mass) / (d * d);      // Calculate gravitional force magnitude
        force.mult(strength);                                  // Get force vector --> magnitude * direction
        return force;
    }

    private float constrain(float val,float min,float max){
        if (val < min)
            return min;
        else if (val > max)
            return max;
        else
            return val;
    }
}
