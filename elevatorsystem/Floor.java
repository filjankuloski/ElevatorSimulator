package elevatorsystem;
import java.util.LinkedList;
import java.util.Queue;

public class Floor
{
    /*
    The Floor class represents floors with separate queues for people going up or down.

    Attributes:
        floorNumber     - the number associated with this floor
        peopleGoingUp   - a queue of people on the current Floor who want to go to a higher floor
        peopleGoingDown - a queue of people on the current Floor who want to go to a lower floor
     */

    private final int floorNumber;
    private final Queue<Person> peopleGoingUp;
    private final Queue<Person> peopleGoingDown;

    /*
    Initializes a Floor object which initially contains an empty waiting queue for people who want to go
    to a higher floor or a lower floor.
     */

    public Floor(int floorNumber)
    {
        this.floorNumber = floorNumber;
        this.peopleGoingUp = new LinkedList<>();
        this.peopleGoingDown = new LinkedList<>();
    }

    // Returns the current Floor's number.

    public int getFloorNumber()
    {
        return floorNumber;
    }

    // Returns the current queue of people waiting to enter an elevator and move to a higher floor.

    protected Queue<Person> getPeopleGoingUp()
    {
        return peopleGoingUp;
    }

    /*
    Returns the number of people in the current queue waiting to enter an elevator
    and move to a higher floor.
     */

    public int getNumberOfPeopleWaitingGoingUp()
    {
        return peopleGoingUp.size();
    }

    // Returns the current queue of people waiting to enter an elevator and move to a lower floor.

    protected Queue<Person> getPeopleGoingDown()
    {
        return peopleGoingDown;
    }

    /*
    Returns the number of people in the current queue waiting to enter an elevator
    and move to a higher floor.
     */

    public int getNumberOfPeopleWaitingGoingDown()
    {
        return peopleGoingDown.size();
    }

    /*
    Enqueues a person to the correct queue based on the direction in which
    the destination floor is located.
     */

    public void enqueuePerson(Person p)
    {
        if(p.getDirectionToDestination() == Direction.DOWN)
        {
            peopleGoingDown.offer(p);
        }
        else if(p.getDirectionToDestination() == Direction.UP)
        {
            peopleGoingUp.offer(p);
        }
    }
}