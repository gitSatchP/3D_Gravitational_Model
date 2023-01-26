/*
* A class for modeling a planet
*/
class Planet {
  // Declare the variables that are properties of a Planet
  PVector position;
  PVector velocity;
  PVector acceleration;
  float mass;
  String companyName;
  float colorChoice;
  double marketCap;
  int tSize;
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
  void applyForce(PVector force) {
    PVector f = PVector.div(force, mass);
    acceleration.add(f);
  }
  /*
   * Method that updates the planet's velocity and position based on its acceleration
   */
  void update() {
    velocity.add(acceleration); 
    position.add(velocity); 
    // Reset the acceleration after the position has been updated
    acceleration.mult(0);
  }
 /*
  * Method for drawing the Planet
  */
  void display() {
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
    sphere(mass*8);
    // Pop off of the matrix stack
    popMatrix();
  }
}
