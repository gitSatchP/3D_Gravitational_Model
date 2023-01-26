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
float brightness;
float speed = 1;
int index;
int tSize_;
float cap;





void setup() {
  // Create window dimensions
  size(1500, 800, P3D);
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
      speed = 0.5;
    }
    if(tenDay/last > 0.9 && tenDay/last <= 1){
      speed = 1;
    }
    if(tenDay/last > 0.8 && tenDay/last <= 0.9){
      speed = 1.5;
    }
    if(tenDay/last > 0.7 && tenDay/last <= 0.8){
      speed = 2;
    }
    if(tenDay/last > 0.6 && tenDay/last <= 0.7){
      speed = 2.5;
    }
    if(tenDay/last > 0.5 && tenDay/last <= 0.6){
      speed = 3;
    }
    if(tenDay/last > 0.4 && tenDay/last <= 0.5){
      speed = 3.5;
    }
    if(tenDay/last > 0.3 && tenDay/last <= 0.4){
      speed = 4;
    }
    if(tenDay/last > 0.2 && tenDay/last <= 0.3){
      speed = 4.5;
    }
    if(tenDay/last > 0.1 && tenDay/last <= 0.2){
      speed = 5;
    }
    if(tenDay/last > 0 && tenDay/last <= 0.1){
      speed = 5.5;
    }
    // Translating the market cap into an actual size of the icon
    cap = cap/40500000000.0;
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
    if(cap < 25 && cap > 2.7){
    Planet p = new Planet(cap, random(-width/2, width/2), random(-height/2, height/2), random(-100, 100), speed, name, brightness, tSize_);
    planets.add(p);
    }
    print(brightness);
}
  // A single sun
  s = new Sun();
}

void draw() {
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
