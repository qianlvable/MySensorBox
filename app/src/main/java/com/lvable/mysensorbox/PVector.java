package com.lvable.mysensorbox;

/**
 * Created by Jiaqi Ning on 9/4/2015.
 */
public class PVector {
    float x;
    float y;
    public PVector(float x,float y){
        this.x = x;
        this.y = y;
    }

    public void add(PVector v){
        y += v.y;
        x += v.x;
    }

    public void sub(PVector v){
        y -= v.y;
        x -= v.x;
    }

    public void mult(float n){
        x *= n;
        y *= n;
    }

    public void div(float n){
        x /= n;
        y /= n;
    }

    public float magnitude(){
        return (float) Math.sqrt(x*x + y*y);
    }

    public void setMagnitude(float m){
        float scale = m*m / (Math.abs(x)+Math.abs(y));
        mult(scale);
    }

    public void normalize(){
        float m = (float) magnitude();
        if (m != 0)
            div(m);
    }

    // limit the mag of the vector
    public void limit(float lim){
        if (magnitude() > lim)
            setMagnitude(lim);
    }

    public static PVector sub(PVector p1,PVector p2){
        return new PVector(p1.x - p2.x,p1.y-p2.y);
    }

    public PVector get(){
        return new PVector(x,y);
    }

    @Override
    public String toString() {
        return "x:"+x +" y:"+y+" m:"+magnitude();
    }
}
