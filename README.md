# ElevatorSimulator

## Overview
This program simulates a single elevator operating within a building.
It demonstrates how the elevator processes passenger requests and moves between floors.

## Instructions

1. **Create a new project** in your IDE.
2. Add the `elevatorsystem` package folder and the `Simulation.java` file to your `src` folder.
3. **Compile and run** the program.
4. The simulation is **hardcoded** and does **not take user input**.
5. To modify the simulation (e.g., range of floors, elevator starting floor), **edit** the `Simulation.java` file directly.
6. The program will print the **start of the simulation**, all **events** that occur during the simulation, and the **end of the simulation**.
7. **Repeat Step 3** to run additional simulations.

## Assumptions

- Floor numbers are represented as integers.
- The range of floor numbers in a building will always be valid. 
- No duplicate floor numbers.
- The lowest floor number will always be less than the highest.
- All people waiting in a queue or traveling in the elevator are located on valid floors within the building.
- The building contains a single elevator operating within a valid range of floors.
- The number of people is non-negative, and all requests are processed sequentially.
- The elevator enforces a maximum weight capacity and a maximum number of passengers.
- Time progresses in steps rather than timestamps.
- The elevator always completes its current direction of travel before reversing directions.
    - For this reason, while traveling in some direction, the elevator will only pick up passengers which will travel in the same direction.
    - The elevator's final destination also changes if a new passenger has a higher (or lower) destination than the current destination.
- Requests are served in first-come, first-server fashion.
- If the elevator is incapable of carrying additional passengers, passengers must wait for the next available trip.

These assumptions simplify the simulation logic by avoiding invalid input handling, concurrent requests, or scenarios with multiple buildings and elevators, allowing the focus to remain on the elevator’s behavior.

