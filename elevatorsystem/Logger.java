package elevatorsystem;

public class Logger
{
    /*
    The Logger class is used to log states / events throughout the Elevator simulation.

    Attributes:
        instance - a singleton instance of the Logger class which is shared by all Classes within
                   the 'elevatorsystem' package
     */

    private static final Logger instance = new Logger();

    /*
    Default constructor of the Logger class. Access modifier set to 'private' since
    we only want a single shared instance of Logger.
     */

    private Logger() {}

    // Returns the singleton instance of the Logger class.

    public static Logger getLogger()
    {
        return instance;
    }

    // Logs the creation of a Building object and its associated details.

    public void logBuilding(Building b)
    {
        System.out.println("\nCreated a Building with bottom floor at Floor "+b.getMinFloor()
                +" and top floor at Floor "+b.getMaxFloor()+".\n");
    }

    // Logs the creation of an Elevator object and its associated details.

    public void logElevator(Elevator e)
    {
        System.out.println("Created an Elevator which starts at Floor "+e.getCurrentFloor()+".\n");
    }

    // Logs the current direction of an elevator and the starting and destination floor of the current trip.

    public void logCurrentDirectionAndFloor(Direction currentDirection, int currentFloor, int nextFloor)
    {
        System.out.println("Elevator moving "+currentDirection.getRepresentation()
                +" from Floor "+currentFloor
                +" to Floor "+nextFloor+".\n");
    }

    // Logs the Person leaving the elevator and the corresponding destination floor.

    public void logPersonLeavingFromElevator(Person p)
    {
        System.out.println("Person "+p.getID()+" has left the elevator at Floor "+p.getDestinationFloor()+".\n");
    }

    // Logs the Person entering the elevator and the corresponding starting floor.

    public void logPersonEnteringElevator(Person p)
    {
        System.out.println("Person "+p.getID()+" has entered the elevator at Floor "+p.getStartingFloor()+".\n");
    }
}