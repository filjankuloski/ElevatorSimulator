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
    - While moving, the elevator may extend its current destination to accomodate passengers whose destinations are further in the current direction of travel.
- Requests are served in first-come, first-served fashion.
- If there are no passengers in the elevator, and no requests from the current floor of the elevator, the elevator should move to the closest floor containing people with requests.
    - Preference is given to people on higher floors if two floors have an equal distance from the current floor.
- If the elevator is incapable of carrying additional passengers, passengers must wait for the next available trip.

These assumptions simplify the simulation logic by avoiding invalid input handling, concurrent requests, or scenarios with multiple buildings and elevators, allowing the focus to remain on the elevator’s behavior.

## Improvements and New Features
### Time Complexity Improvement
- The current time complexity of the program is O(N log N), where 'N' is the number of floors, because of the utilization of Priority Queue and TreeSets.
- Realistically, the number of floors will be capped at some number below 200, which allows us to use other alternatives for sorting floors.
- Instead of using a Priority Queue to sort the closest destination floor numbers to the current floor, we can use a boolean array.
    - The index is the floor number and the boolean value indicates whether or not a passenger needs to leave on that floor.
        - E.g., `boolean[] map = new boolean[160];`
        - E.g., `map[4] = true;`
    - By starting at the Elevator's current floor, we can extend our search in both directions until we find the closest index where the value at that index is 'true'.
- The same idea for the Priority Queue can be used for our TreeSet as well.
    - Using a boolean array ensures that all floors on a precomputed route will be unique.
    - To ensure the route is in sorted order:
        - Iterate from the starting floor up to the top floor if the elevator is moving upwards.
        - Iterate from the starting floor down to the bottom floor if the elevator is moving downwards.
- The time complexity will be reduced from O(N log N) to O(N) since we only need to iterate through 'N' floors in the worst case.
- Boolean arrays are also much more space-efficient and simpler to manage.

### Floor Mapping
- As convenient as it would be for all floor numbers to be labeled as integers, this frequently is NOT the case.
- Floor labels will need to be converted into integers, ranging from 0 to N - 1 where N is the total number of floors.
- A mapping between floor labels and integers will also need to be made.
    - E.g., `Map<String, Integer> floorMap = new HashMap<>();`
    - E.g., `map.put("B3", 0)`;
- In order to be able to convert an index back to its respective label, it will also be necessary to have an array of Strings to store the floor labels.
    - E.g., `String[] labels = new String[N - 1];`
    - E.g., `labels[0] = "B3";`
- Floor mapping will also enable us to easily handle negative numbers or non-consecutive numbers.

### Access Levels
- By using an enumeration for access levels, we can successfully restrict people with insufficient access from entering certain floors.
- We can give each enumeration a corresponding 'rank' attribute, where the lowest access level has the lowest number, and the highest access level has the highest number.
    - E.g., PUBLIC(1), EMPLOYEE(2), ADMINISTRATOR(3), EXECUTIVE(4)
- A passenger will only be permitted entry to a certain floor if their access level is higher than the floor's access level.
- Passengers may stop at a restricter floor, but they will not be allowed entry, and they will need to re-enter the elevator to move to a permitted floor.

### Improved Logging
- The current logging is basic and is only meant to help debug and make it simple to follow the elevator simulation, ensuring that it follows the correct path.
- We can add timestamps so that exact datetimes cna be associated with all occurring events.
- Multiple states can also be added to the simulation to represent events as enumerations, such as:
    - BUTTON_PRESSED
    - DOOR_OPEN
    - DOOR_CLOSED
- We can also include additional information, such as:
    - Current weight of the elevator (e.g., 140 pounds)
    - Current passengers in the elevator (e.g., [Person A, Person C])
- By enabling thorough logging of events, debugging becomes increasingly simplified and reduces the time needed to locate problems with the code.

### Event Queue
- Currently, all events that occur within the elevator system are completely hardcoded.
- Using the aforementioned timestamps, we can use a Queue to process events in chronological order.
    - E.g., Person A leaves Floor 3 at 2:30 PM, Person B presses for Floor 10 inside the elevator at 2:31 PM.

### Unit Testing
- To ensure that the elevator system functions exactly as intended, we can add unit testing to validate different functionalities, such as precomputing routes and direction logic.

### Error Handling and Validation
- Currently, it is assumed that simulation will ensure input validity.
- Instead, error handling and validation should be used to handle edge cases, such as:
    - Invalid floor numbers
    - Illegal access attempts
