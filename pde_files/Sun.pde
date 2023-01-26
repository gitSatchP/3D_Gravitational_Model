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
    G = 0.4;
  }
  /*
   * Method for calculating force of attraction between the sun and a planet
   */
  PVector attract(Planet m) {
    // Calculates the force vector as the difference between the positions of the sun and planet
    PVector force = PVector.sub(position, m.position); 
    // Calculates the distance between the sun and planet
    float d = force.mag();
    // Constrains the distance between a minimum and maximum value
    d = constrain(d, 5.0, 15.0);    
    // Calculates the strength of the attraction as the product of G, mass of sun, mass of planet,
    // and the inverse square of the distance
    float strength = (G * mass * m.mass) / (d * d);   
    // Sets the magnitude of the force vector to the calculated strength
    force.setMag(strength);                              
    return force;
  }
  /*
   * Method for drawing the Sun
   */
  void display() {
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
