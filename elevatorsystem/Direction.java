package elevatorsystem;

public enum Direction
{
    /*
    The Direction enumeration is used to enumerate the different directions that an Elevator can move in.
    The directions include: moving up, moving down, and staying stationary.

    Attributes:
        representation - a String representation of the different directions, which is used for
                         logging purposes. Set to 'final' because it should not be changed
     */

    UP("up"),
    DOWN("down"),
    STATIONARY("stationary");

    private final String representation;

    // Constructor of the Direction class. 'Direction' objects are not meant to be instantiated.

    private Direction(String representation)
    {
        this.representation = representation;
    }

    // Returns the corresponding representation of one of the directions.

    public String getRepresentation()
    {
        return representation;
    }
}