import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;
import java.lang.Math;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
/*
 * This program is a 3D sketch visualizing price change today relative to over
 * the past ten days (represented by speed & brightness) as well as market cap
 * (represented by mass)
 *
 * It is an implementation of "GravitationalAttraction3D" by Daniel
 * Shiffman.
 *
 * Edited, adapted, and additional code written by Satchel Peterson
 */
public class Gravitational_Attraction_Model extends PApplet {
    // An arrraylist to hold all of the companies (planets)
    ArrayList<Planet> planets = new ArrayList<Planet>();
    // Create a sun object
    Sun s;
    // Create two variables to hold the JSON data
    JSONObject jsonCompanies;
    JSONObject jsonQuotes;
    // Create variables to hold value of the specified data points
    float today;
    float oneWeek;
    float tenDay;
    float last;
    String name;
    String pace;
    // Create variables for the properties of each icon (speed, mass, text size,
    // brightness), while initializing speed to 1
    int tSize_;
    float cap;
    float speed = 1;
    float brightness;
    public void setup() {
        // Create window dimensions
        size(1500, 800, P3D)
        // Create planets
        for (int i = 0; i < 30; i++) {
            // Load JSON data
            jsonCompanies = loadJSONObject("dow30_companies.json");
            jsonQuotes = loadJSONObject("dow30_quotes.json");
            // Get company information from JSON files
            name = jsonCompanies.getJSONObject("data").getJSONArray("companies").getJSONObject(i).getString("symbol");
            today = jsonQuotes.getJSONObject("data").getJSONArray("quotes").getJSONObject(i).getJSONObject("data").getJSONObject("changePercent").getFloat("today");
            oneWeek = jsonQuotes.getJSONObject("data").getJSONArray("quotes").getJSONObject(i).getJSONObject("data").getJSONObject("changePercent").getFloat("oneWeek");
            tenDay = jsonQuotes.getJSONObject("data").getJSONArray("quotes").getJSONObject(i).getJSONObject("data").getJSONObject("volume").getFloat("average10Day");
            last = jsonQuotes.getJSONObject("data").getJSONArray("quotes").getJSONObject(i).getJSONObject("data").getJSONObject("volume").getFloat("last");
            pace = jsonQuotes.getJSONObject("data").getJSONArray("quotes").getJSONObject(i).getJSONObject("data").getJSONObject("volume").getString("pace");
            cap = jsonQuotes.getJSONObject("data").getJSONArray("quotes").getJSONObject(i).getJSONObject("data").getFloat("marketCap");
            // Logic for computing brightness
            // Get absolute value of today and one week data points
            today = Math.abs(today);
            oneWeek = Math.abs(oneWeek);
            // Calculate the percentage of brightness a company has by calculating
            // the change of price today over the change of price for the week
            float percentage = min((today/oneWeek), 1);
            brightness = percentage * 100;
            // If brightness is less than 10 the icon will not be visible
            if(brightness < 10){
                brightness = 10;
            }
            // Logic for calculating the speed of each moving icon
            if(tenDay/last > 1){
                speed = 0.5f;
            }
            if(tenDay/last > 0.9f && tenDay/last <= 1){
                speed = 1;
            }
            if(tenDay/last > 0.8f && tenDay/last <= 0.9f){
                speed = 1.5f;
            }
            if(tenDay/last > 0.7f && tenDay/last <= 0.8f){
                speed = 2;
            }
            if(tenDay/last > 0.6f && tenDay/last <= 0.7f){
                speed = 2.5f;
            }
            if(tenDay/last > 0.5f && tenDay/last <= 0.6f){
                speed = 3;
            }
            if(tenDay/last > 0.4f && tenDay/last <= 0.5f){
                speed = 3.5f;
            }
            if(tenDay/last > 0.3f && tenDay/last <= 0.4f){
                speed = 4;
            }
            if(tenDay/last > 0.2f && tenDay/last <= 0.3f){
                speed = 4.5f;
            }
            if(tenDay/last > 0.1f && tenDay/last <= 0.2f){
                speed = 5;
            }
            if(tenDay/last > 0 && tenDay/last <= 0.1f){
                speed = 5.5f;
            }
            // Translating the market cap into an actual size of the icon
            cap = cap/40500000000.0f;
            if(cap > 15){
                tSize_ = 30;
            }else if(cap > 6 && cap < 10){
                tSize_ = 15;
            }else if(cap < 3){
                tSize_ = 10;
            }
            // Limiting the sketch to include only companies in the median range of
            // market cap, otherwise it would be impractical to include icons on
            // either end of the range (as they would either be massive or tiny)
            if(cap < 25 && cap > 2.7f){
                Planet p = new Planet(cap, random(-width/2, width/2), random(-height/2, height/2), random(-100, 100), speed, name, brightness, tSize_);
                planets.add(p);
            }
            print(brightness);
        }
        // A single sun
        s = new Sun();
    }

    public void draw() {
        // Set the background to black
        background(0);
        // Set the sphere detail
        sphereDetail(2);
        // Sets default ambient and directional light
        lights();
        // Translate the origin point to the center of the screen
        translate(width/2, height/2);
        // Display the sun
        s.display();
        // Loop through each planet in the "planets" array
        for (Planet planet : planets) {
            // Calculate the force of attraction between the sun and the current planet
            PVector force = s.attract(planet);
            // If the mouse is not pressed
            if(!mousePressed){
                // Apply the force of simulated gravity
                planet.applyForce(force);
                // Update the planet's position
                planet.update();
            }
            // Display it
            planet.display();
        }
    }
    /*
     * A class for modeling a planet
     */
    class Planet {
        // Declare the variables that are properties of a Planet
        PVector position;
        PVector velocity;
        PVector acceleration;
        float mass;
        float colorChoice;
        double marketCap;
        int tSize;
        String companyName;
        /*
         * Constructor that sets the initial properties of the planet
         */
        Planet(float m, float x, float y, float z, float s, String n, float colors, int textSize) {
            // Initialize variables
            colorChoice = colors;
            mass = m;
            companyName = n;
            tSize = textSize;
            // Initialize position, velocity, and acceleration vectors
            position = new PVector(x, y, z);
            velocity = new PVector(s, 0);
            acceleration = new PVector(0, 0);
        }
        /*
         * Method that applies a force to the planet and updates its acceleration
         */
        public void applyForce(PVector force) {
            PVector f = PVector.div(force, mass);
            acceleration.add(f);
        }
        /*
         * Method that updates the planet's velocity and position based on its acceleration
         */
        public void update() {
            velocity.add(acceleration);
            position.add(velocity);
            // Reset the acceleration after the position has been updated
            acceleration.mult(0);
        }
        /*
         * Method for drawing the Planet
         */
        public void display() {
            // Set color setting and hue
            colorMode(HSB, 360, 100, 100);
            fill(200, 43, 100); //#FFF000
            // Set icon text settings
            textAlign(CENTER);
            textSize(20);
            // Translate the text according to the position calculated by the update() method
            text(companyName, position.x, position.y, position.z);
            // Create the details for the company icon
            stroke(182, 100, colorChoice);
            strokeWeight(2);
            noFill();
            pushMatrix();
            // Translate the icon according to the position calculated by the update() method
            translate(position.x, position.y, position.z);
            // Draw a sphere with the settings defined above
            sphere(mass*8);
            // Pop off of the matrix stack
            popMatrix();
        }
    }
    class Sun {
        // Declare variables
        float mass;
        PVector position;
        // Used to calculate gravitational force of attraction between the planets
        float G;
        /*
         * Constructor that sets the initial properties of the Sun
         */
        Sun() {
            // Initialize variables
            position = new PVector(0, 0);
            mass = 20;
            G = 0.4f;
        }
        /*
         * Method for calculating force of attraction between the sun and a planet
         */
        public PVector attract(Planet m) {
            // Calculates the force vector as the difference between the positions of the sun and planet
            PVector force = PVector.sub(position, m.position);
            // Calculates the distance between the sun and planet
            float d = force.mag();
            // Constrains the distance between a minimum and maximum value
            d = constrain(d, 5.0f, 15.0f);
            // Calculates the strength of the attraction as the product of G, mass of sun, mass of planet,
            // and the inverse square of the distance
            float strength = (G * mass * m.mass) / (d * d);
            // sets the magnitude of the force vector to the calculated strength
            force.setMag(strength);
            return force;
        }
        /*
         * Method for drawing the Sun
         */
        public void display() {
            // Set color setting and hue
            colorMode(RGB);
            fill(255);
            // Set icon text settings
            textAlign(CENTER);
            textSize(15);
            // Translate the text according to the position calculated by the update() method
            text("DOW 30", position.x, position.y, position.z);
            // Create the details for the Sun's shape
            stroke(255);
            noFill();
            // Push onto the matrix stack
            pushMatrix();
            // Translate the icon according to the position calculated by the update() method
            translate(position.x, position.y, position.z);
            // Draw a sphere with the settings defined above
            sphere(mass*2);
            // Pop off of the matrix stack
            popMatrix();
        }
    }
    public void settings() { size(1500, 800, P3D); }
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "GravitationalAttraction3D_edited" };
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
