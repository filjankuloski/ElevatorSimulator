import elevatorsystem.Building;
import elevatorsystem.Direction;
import elevatorsystem.Elevator;
import elevatorsystem.Person;

public class Simulation
{
    // Simulation class is used to set up and execute a simulation of a single Elevator
    // taking requests from Person objects to move between Floors inside a Building.

    public static void main(String[] args)
    {
        // Creates a building object with a range of floors, and an elevator object with a starting floor.
        Building building = new Building(1, 5);
        Elevator elevator = new Elevator(4, building);

        // Enqueues people to their respective waiting queues on each floor. First come, first served.
        building.getFloor(1).enqueuePerson(new Person(1,70, 1, 4));
        building.getFloor(2).enqueuePerson(new Person(2,60, 2, 5));
        building.getFloor(3).enqueuePerson(new Person(3,80, 3, 1));
        building.getFloor(5).enqueuePerson(new Person(4,90, 5, 2));

        // As long as the elevator isn't stationary, or there are still people to transport, the
        // elevator should continue operating.
        while(elevator.getCurrentDirection() != Direction.STATIONARY || elevator.shouldContinueOperating())
        {
            elevator.move();
        }
    }
}