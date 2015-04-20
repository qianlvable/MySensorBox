package com.lvable.mysensorbox;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by Jiaqi Ning on 9/4/2015.
 */
public class Mover {
    PVector location;
    PVector velocity;
    PVector acceleration;
    int color;
    float mass;



    Mover(float m, float x, float y) {
        mass = m;
        location = new PVector(x, y);
        velocity = new PVector(0.3f, 0);
        acceleration = new PVector(0, 0);
        color = Color.RED;

    }

    void applyForce(PVector force){
        PVector f = force.get();
        f.div(mass);
        acceleration.add(f);

    }

    void update(){
        velocity.add(acceleration);
        location.add(velocity);

        acceleration.mult(0);
    }
}
