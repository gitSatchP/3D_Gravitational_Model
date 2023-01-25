# 3D Gravitational Model of Stock Data
### Summary
A 3D visualization of data from companies from the Dow Jones Industrial Average (Dow 30), with their relationships being simulated by gravitational 
attraction. 

### Full Description
This program is an implementation of the "GravitationalAttraction3D" sketch by Daniel Shiffman, which I edited and adapted to create a visual model of 
stock data from companies in the Dow Jones. The sketch visualizes price change data for 30 companies from the Dow Jones Industrial Average (Dow 30) over 
the past ten days. The data is loaded from two JSON files: "dow30_companies.json" and "dow30_quotes.json". The program creates an ArrayList of "Planet" 
objects to represent each company, and assigns properties to each planet based on the JSON data. These properties include mass (based on market cap), speed 
(based on the ratio of average 10-day volume to last volume), and brightness (based on the ratio of today's change percentage to one week's change 
percentage). The sketch also includes a Sun object which the planets revolve around, and allows the user to pause the screen by pressing the mouse.

I created this model in the summer of 2022 as part of my internship with S&P Global. 
<br>
## [Demonstration](https://youtu.be/iNdMn4_pfMk)
[![Usage Example](https://img.youtube.com/vi/iNdMn4_pfMk/0.jpg)](https://youtu.be/iNdMn4_pfMk)

Above is a demonstration I created of how I use this model to understand more about the a company's current performance on the market. In this 
demonstration I'm particularly focused on examining the mass, speed, and brightness of certain icons. To use this model with real time data, I would 
recommend leaving the program running in the background, and taking notice to icons that are especially bright, moving especially fast, or especially large 
compared to other icons (in that order of precedence). This alerts you to companies that are behaving differently than normal. In this demonstration I 
pause the moving icons a few times to identify the companies that are moving fast.

If you are interested in using this model, feel free to download the .pde files if you have Processing 4 or later installed on your computer. You just need 
to change the data source which should be in the format of a JSON file. In the *setup()* method of the code this is the lines: 
```
jsonCompanies = loadJSONObject("dow30_companies.json");
jsonQuotes = loadJSONObject("dow30_quotes.json");
```
The file name should be inside the quotes within the parenthesis. *jsonQuotes* is the variable that refers to the file that contains the stock data points, 
and *jsonCompanies* is the variable that refers to the corresponding company name to get the correct label for each moving icon.
