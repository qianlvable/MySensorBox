package com.lvable.mysensorbox;

import android.util.Log;

/**
 * Created by Jiaqi Ning on 9/4/2015.
 */
public class Attractor {
    float mass;
    private float G;
    PVector acceleration;
    PVector location;
    PVector velocity;

    public void setLocation(float x,float y) {
        location.x = x;
        location.y = y;
    }



    public Attractor(int x,int y){
        location = new PVector(x,y);
        velocity = new PVector(0, 0);
        acceleration = new PVector(0, 0);
        mass = 55;
        G = 4f;
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

    void applyForce(PVector force){
       // acceleration = force;
        velocity = force;
        Log.d("wtf",acceleration.toString());
    }

    void update(){
        //velocity.add(acceleration);
        location.add(velocity);
        Log.d("speed",velocity.toString());
       // acceleration.mult(0);
    }
}
