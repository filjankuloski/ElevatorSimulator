package elevatorsystem;
import java.util.HashMap;
import java.util.Map;

public class Building
{
    /*
    The Building class represents a range of floors in which the Elevator can move.

    Attributes:
        logger   - a singleton instance of the Logger class used to log the creation
                   of Building objects

        floors   - a hashmap which stores key-value pairs where the integer is a floor number
                   and the value is the associated Floor object

        minFloor - the lowest Floor that an Elevator can travel to within a particular Building

        maxFloor - the highest Floor that an Elevator can travel to within a particular Building
     */

    private static final Logger logger = Logger.getLogger();
    private final Map<Integer, Floor> floors;
    private final int minFloor;
    private final int maxFloor;

    // Initializes a Building object with Floors with numbers ranging from 'minFloor' to 'maxFloor'.

    public Building(int minFloor, int maxFloor)
    {
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        floors = new HashMap<>();

        for(int i = minFloor; i <= maxFloor; i++)
        {
            floors.put(i, new Floor(i));
        }

        logger.logBuilding(this);
    }

    // Retrieves the Floor object for a given floor number.

    public Floor getFloor(int floor)
    {
        return floors.get(floor);
    }

    // Returns the highest floor number in a Building.

    public int getMaxFloor()
    {
        return maxFloor;
    }

    // Returns the lowest floor number in a Building.

    public int getMinFloor()
    {
        return minFloor;
    }

    /*
    Returns the closest floor number, given a starting floor, with a pending request.
     */

    public int findNearestWaitingFloor(int startingFloor)
    {
        // Store the first floors directly above and below the starting floor.
        int belowNumber = startingFloor - 1;
        int aboveNumber = startingFloor + 1;

        while(aboveNumber <= getMaxFloor() && belowNumber >= getMinFloor())
        {
            // Retrieve the Floor object for the given floor numbers for access to their respective waiting queues.
            Floor aboveFloor = getFloor(aboveNumber);
            Floor belowFloor = getFloor(belowNumber);

            // Look for pending requests in either the higher, or the lower floor.
            // If requests are found in both floors, the higher floor will always receive priority.
            if(!aboveFloor.getPeopleGoingUp().isEmpty() || !aboveFloor.getPeopleGoingDown().isEmpty())
            {
                return aboveNumber;
            }

            if(!belowFloor.getPeopleGoingUp().isEmpty() || !belowFloor.getPeopleGoingDown().isEmpty())
            {
                return belowNumber;
            }

            // Expand the search by one floor.
            aboveNumber++;
            belowNumber--;
        }

        // If there are unexplored floors above the starting floor, continue searching.
        while(aboveNumber <= getMaxFloor())
        {
            Floor aboveFloor = getFloor(aboveNumber);

            if(!aboveFloor.getPeopleGoingUp().isEmpty() || !aboveFloor.getPeopleGoingDown().isEmpty())
            {
                return aboveNumber;
            }

            aboveNumber++;
        }

        // If there are unexplored floors below the starting floor, continue searching.
        while(belowNumber >= getMinFloor())
        {
            Floor belowFloor = getFloor(belowNumber);

            if(!belowFloor.getPeopleGoingUp().isEmpty() || !belowFloor.getPeopleGoingDown().isEmpty())
            {
                return belowNumber;
            }

            belowNumber--;
        }

        // Return sentinel value, there are no pending requests remaining.
        return Integer.MAX_VALUE;
    }
}