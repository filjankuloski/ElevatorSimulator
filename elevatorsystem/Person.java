package elevatorsystem;

public class Person
{
    /*
    The Person class represents people which use and engage with the Elevator.

    Attributes:
        id               - Unique identifier associated with the current Person.

        weight           - Weight associated with the current Person, used to ensure that an
                           Elevator does not exceed capacity.

        startingFloor    - The Floor in which the current Person enters the waiting queue.

        destinationFloor - The Floor in which the current Person leaves the Elevator
     */

    private final int id;
    private final int weight;
    private final int startingFloor;
    private final int destinationFloor;

    /*
    Initializes a Person object which contains a Person's id, weight, starting floor, and desired destination
    floor.
     */

    public Person(int id, int weight, int currentFloor, int destinationFloor)
    {
        this.id = id;
        this.weight = weight;
        this.startingFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }

    // Returns the ID of a Person object

    public int getID()
    {
        return id;
    }

    // Returns the weight of a Person object

    public int getWeight()
    {
        return weight;
    }

    // Returns the floor that a Person object starts on before entering an Elevator

    public int getStartingFloor()
    {
        return startingFloor;
    }

    // Returns the floor that a Person object leaves when leaving an Elevator

    public int getDestinationFloor()
    {
        return destinationFloor;
    }

    // Returns the Direction that an Elevator must move to get a Person object to their destination.

    public Direction getDirectionToDestination()
    {
        if(startingFloor == destinationFloor)
        {
            return Direction.STATIONARY;
        }
        else if(startingFloor > destinationFloor)
        {
            return Direction.DOWN;
        }

        return Direction.UP;
    }
}