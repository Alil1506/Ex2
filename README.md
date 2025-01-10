# Ex2 Spreadsheet Implementation Project

## About Me
Hi! I'm Karylle Labao, a first-year Computer Science student at Ariel University. After completing my first project (Ex1),
I'm excited to share my second project which implements a spreadsheet system.
This project has helped me better understand object-oriented programming concepts and Java development.

## Project Overview
This project creates a functional spreadsheet system that can handle three main types of cell content:
- Text (like "Hello World")
- Numbers (like "42" or "3.14")
- Formulas (like "=A1+B2" or "=(1+2)*3")

I had to implement various components to make this work, including formula parsing, cell referencing, and error handling. 
It was challenging but really helped me understand how real spreadsheet applications work!

## Code Structure
The project consists of several key components:
- **SCell.java**: Handles individual cell behavior (like storing values and determining cell types)
- **Ex2Sheet.java**: Manages the entire spreadsheet grid and coordinates cell interactions
- **Formtools.java**: Contains all the formula processing logic
- **CellEntry.java**: Manages cell coordinates and references

## Formula Features
The spreadsheet can handle various formula types:
1. Simple numbers: =1, =1.2
2. Cell references: =A1, =B2
3. Basic arithmetic: =1+2, =2*3
4. Complex expressions: =(A1+B2)*3

## Error Handling
I implemented error checking for:
- Invalid formulas
- Circular references (like when A1 references B1 which references A1)
- Invalid cell references

## How to use
Here's a simple example of using the spreadsheet:
```java
Sheet mySheet = new Ex2Sheet(9, 17);  // Creates a new spreadsheet

// Adding some values
mySheet.set(0, 0, "5");        // Puts 5 in cell A1
mySheet.set(0, 1, "Hello");    // Puts "Hello" in cell A2
mySheet.set(1, 0, "=A1*2");    // Formula that doubles A1's value

## Testing
I created comprehensive tests to ensure everything works correctly. The tests cover:

Number validation
Text handling
Formula processing
Error detection

This project taught me a lot about Java programming and how to build complex systems with multiple interacting components!
