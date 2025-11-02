package elevatorsystem;
import java.util.*;

public class Elevator
{
    /*
    The Elevator class represents a single elevator operating within a Building,
    processing requests from People and moving between Floors.

    Attributes:
        logger             - a singleton instance of the Logger class used to log elevator movements
                             and passenger activity

        currentFloor       - the current floor number of the elevator

        currentDestination - the current destination floor of the elevator

        currentDirection   - current direction of movement of the elevator

        MAXIMUM_PEOPLE     - maximum number of people allowed in the elevator

        MAXIMUM_WEIGHT     - maximum supported total weight of the elevator

        passengers         - list of passengers currently inside the elevator

        currentWeight      - total weight of all passengers in the elevator

        upCalls            - priority queue of passenger's destination floors located above the current floor

        downCalls          - priority queue of passenger's destination floors located below the current floor

        building           - the building that defines the range of floors the elevator operates within
     */

    private static final Logger logger = Logger.getLogger();

    private int currentFloor;
    private int currentDestination;
    private Direction currentDirection;

    private static final int MAXIMUM_PEOPLE = 10;
    private static final int MAXIMUM_WEIGHT = 1000;

    private final List<Person> passengers;
    private int currentWeight;

    private final PriorityQueue<Integer> upCalls;
    private final PriorityQueue<Integer> downCalls;

    private final Building building;

    // Initializes an elevator at the specified starting floor within the given building.

    public Elevator(int startingFloor, Building building)
    {
        currentFloor = startingFloor;
        currentDirection = Direction.STATIONARY;
        currentDestination = currentFloor;

        currentWeight = 0;
        passengers = new ArrayList<>();

        upCalls = new PriorityQueue<>();
        downCalls = new PriorityQueue<>((a, b) -> b - a);

        this.building = building;

        logger.logElevator(this);
    }

    // Returns the current floor number of the elevator.

    public int getCurrentFloor()
    {
        return currentFloor;
    }

    // Returns the floor number of the current destination of the elevator.

    public int getCurrentDestination()
    {
        return currentDestination;
    }

    // Returns the current direction of the elevator.

    public Direction getCurrentDirection()
    {
        return currentDirection;
    }

    // Returns the current total weight of the elevator.

    public int getCurrentWeight()
    {
        return currentWeight;
    }

    // Returns the current number of passengers in the elevator.

    public int getNumberOfPassengers()
    {
        return passengers.size();
    }

    // Returns the number of passengers whose destination floors are above the current floor.

    public int getNumberOfPassengersGoingUp()
    {
        return upCalls.size();
    }

    // Returns the number of passengers whose destination floors are below the current floor.

    public int getNumberOfPassengersGoingDown()
    {
        return downCalls.size();
    }

    // Determines the direction of the elevator, calculates a route, and processes all floors along the path.

    public void move()
    {
        // Checks for any remaining pending requests, otherwise, the elevator stops.
        if (!shouldContinueOperating())
        {
            currentDirection = Direction.STATIONARY;
            return;
        }

        // Finds the next direction and destination, produces a route given the direction and destination, and
        // iterates through each floor on the route, unloading and loading people along the way.
        findNextDirectionAndDestination();
        TreeSet<Integer> route = calculateRoute();
        travelRoute(route);
    }

    /*
    Determines whether the elevator has pending requests to handle, either from waiting passengers
    or passengers within the elevator.
    */

    public boolean shouldContinueOperating()
    {
        Floor current = building.getFloor(currentFloor);
        Queue<Person> upQueue = current.getPeopleGoingUp();
        Queue<Person> downQueue = current.getPeopleGoingDown();

        // Checks if the current floor has pending requests.
        if (!upQueue.isEmpty() || !downQueue.isEmpty())
        {
            return true;
        }

        // Checks if the passengers on the elevator have pending requests.
        if (!upCalls.isEmpty() || !downCalls.isEmpty())
        {
            return true;
        }

        // Checks if any floors above or below the current floor have pending requests.
        if (building.findNearestWaitingFloor(currentFloor) != Integer.MAX_VALUE)
        {
            return true;
        }

        // There are no remaining pending requests, the simulation is over.
        return false;
    }

    /*
    Determines the next direction and destination by checking for requests on the current floor, then inside
    the elevator, and finally on other floors.
     */

    private void findNextDirectionAndDestination()
    {
        Floor current = building.getFloor(currentFloor);
        Queue<Person> upQueue = current.getPeopleGoingUp();
        Queue<Person> downQueue = current.getPeopleGoingDown();

        // Checks if there are no pending requests on the current floor, then in the elevator, and finally
        // on other floors above or below the current floor. If there are no pending requests remaining,
        // then the elevator is stationary, and the simulation is over. Otherwise, sets the current direction
        // and destination of the elevator.

        if (upQueue.isEmpty() && downQueue.isEmpty())
        {
            if (upCalls.isEmpty() && downCalls.isEmpty())
            {
                int nearestWaitingFloor = building.findNearestWaitingFloor(currentFloor);

                if (nearestWaitingFloor == Integer.MAX_VALUE)
                {
                    currentDirection = Direction.STATIONARY;
                }
                else
                {
                    currentDirection = currentFloor > nearestWaitingFloor ? Direction.DOWN : Direction.UP;
                    currentDestination = nearestWaitingFloor;
                }
            }
            else
            {
                int belowDistance = downCalls.isEmpty() ? Integer.MAX_VALUE : Math.abs(currentFloor - downCalls.peek());
                int aboveDistance = upCalls.isEmpty() ? Integer.MAX_VALUE : Math.abs(currentFloor - upCalls.peek());

                int aboveDest = upCalls.isEmpty() ? Integer.MAX_VALUE : upCalls.peek();
                int belowDest = downCalls.isEmpty() ? Integer.MAX_VALUE : downCalls.peek();

                setNextDirectionAndDestination(belowDistance, aboveDistance, belowDest, aboveDest);
            }
        }
        else
        {
            int belowDistance = downQueue.isEmpty() ? Integer.MAX_VALUE : Math.abs(downQueue.peek().getDestinationFloor() - currentFloor);
            int aboveDistance = upQueue.isEmpty() ? Integer.MAX_VALUE : Math.abs(upQueue.peek().getDestinationFloor() - currentFloor);

            int aboveDest = upQueue.isEmpty() ? Integer.MAX_VALUE : upQueue.peek().getDestinationFloor();
            int belowDest = downQueue.isEmpty() ? Integer.MAX_VALUE : downQueue.peek().getDestinationFloor();

            setNextDirectionAndDestination(belowDistance, aboveDistance, belowDest, aboveDest);
        }
    }

    /*
    Calculates the sequence of floors the elevator will visit in its current direction.
    Returns the result in a TreeSet to ensure floors are unique and sorted.
     */

    private TreeSet<Integer> calculateRoute()
    {
        // Add the current floor and current destination to the route.
        int destination = currentFloor;
        TreeSet<Integer> route = currentDirection == Direction.UP ? new TreeSet<>() : new TreeSet<>((a, b) -> b - a);
        route.add(currentFloor);
        route.add(currentDestination);

        // On the way up (or down), add any floors to the route that have passengers with a destination in the
        // same direction. Also, find the highest (or lowest) floor and set that floor equal to the new destination.

        if (currentDirection == Direction.UP)
        {
            for (Person p : passengers)
            {
                destination = Math.max(destination, p.getDestinationFloor());
                route.add(p.getDestinationFloor());
            }

            for (int f = currentFloor + 1; f < building.getMaxFloor(); f++)
            {
                Floor floor = building.getFloor(f);

                if (!floor.getPeopleGoingUp().isEmpty())
                {
                    destination = Math.max(destination, f);
                    route.add(f);
                }
            }

            if(!building.getFloor(building.getMaxFloor()).getPeopleGoingDown().isEmpty())
            {
                route.add(building.getMaxFloor());
            }
        }
        else
        {
            for (Person p : passengers)
            {
                destination = Math.min(destination, p.getDestinationFloor());
                route.add(p.getDestinationFloor());
            }

            for (int f = currentFloor - 1; f > building.getMinFloor(); f--)
            {
                Floor floor = building.getFloor(f);

                if (!floor.getPeopleGoingDown().isEmpty())
                {
                    destination = Math.min(destination, f);
                    route.add(f);
                }
            }

            if(!building.getFloor(building.getMinFloor()).getPeopleGoingUp().isEmpty())
            {
                route.add(building.getMinFloor());
            }
        }

        currentDestination = destination;
        return route;
    }

    // Sets the current direction and destination based on the nearest floor with pending requests.

    private void setNextDirectionAndDestination(int belowDistance, int aboveDistance, int belowDest, int aboveDest)
    {
        // Find the floor nearest to the current floor and update the current destination
        // and direction of the elevator.
        if (aboveDistance <= belowDistance)
        {
            currentDirection = Direction.UP;
            currentDestination = aboveDest;
        }
        else
        {
            currentDirection = Direction.DOWN;
            currentDestination = belowDest;
        }
    }

    // Moves the elevator along the specified route, unloading and loading passengers at each floor.

    private void travelRoute(TreeSet<Integer> route)
    {
        for(int floor : route)
        {
            // For each floor in the route, log movement when the elevator changes floors, and unload / load
            // passengers as needed.

            if(currentFloor != floor)
            {
                logger.logCurrentDirectionAndFloor(currentDirection, currentFloor, floor);
            }

            currentFloor = floor;
            unloadPeopleAt(currentFloor);
            loadPeopleAt(currentFloor);
        }
    }

    // Unloads all passengers whose destination matches the given floor.

    private void unloadPeopleAt(int floor)
    {
        // Initializes an iterator so that passengers can be safely removed mid-iteration.

        Iterator<Person> iterator = passengers.iterator();

        // Iterates through passengers and removes / logs any whose destination matches the current floor.
        // Their weight is subtracted from the elevator's total capacity and their pending request
        // is removed.

        while (iterator.hasNext())
        {
            Person p = iterator.next();

            if (p.getDestinationFloor() == floor)
            {
                logger.logPersonLeavingFromElevator(p);
                currentWeight -= p.getWeight();
                iterator.remove();

                if(currentDirection == Direction.UP)
                {
                    upCalls.poll();
                }
                else
                {
                    downCalls.poll();
                }
            }
        }
    }

    /*
    Loads passengers from the current floor whose destination matches the elevator's current direction,
    as long as they are within the elevator's remaining weight and space capacity.
     */

    private void loadPeopleAt(int floor)
    {
        Floor currentFloor = building.getFloor(floor);
        Queue<Person> floorQueue = currentDirection == Direction.UP ? currentFloor.getPeopleGoingUp() : currentFloor.getPeopleGoingDown();

        // Loads all passengers that are within the remaining weight and space capacity and
        // adds their weight to the total weight of the elevator. Also adds all 'Person' objects to the list of
        // passengers on the elevator, and removes them from the waiting queue.

        while (!floorQueue.isEmpty())
        {
            Person p = floorQueue.peek();

            if (currentWeight + p.getWeight() <= MAXIMUM_WEIGHT && passengers.size() < MAXIMUM_PEOPLE)
            {
                logger.logPersonEnteringElevator(p);
                currentWeight += p.getWeight();
                passengers.add(p);
                floorQueue.poll();

                if (Direction.UP == currentDirection)
                {
                    upCalls.offer(p.getDestinationFloor());
                }
                else
                {
                    downCalls.offer(p.getDestinationFloor());
                }
            }
            else
            {
                break;
            }
        }
    }
}