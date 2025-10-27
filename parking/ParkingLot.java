package parking; 

import parking.facility.Space;
import vehicle.Size;

public class ParkingLot
{
    private final Space[][] floorPlan;

    public ParkingLot(int floorNumber, int spaceNumber) 
    {
        if(floorNumber <= 0 || spaceNumber <= 0) 
        {
            throw new IllegalArgumentException();
        }

        floorPlan = new Space[floorNumber][spaceNumber];

        for (int i = 0; i < floorNumber; i++) 
        {
            for (int j = 0; j < spaceNumber; j++) 
            {
                floorPlan[i][j] = new Space(i, j);
            }
        }
    }

    public Space[][] getFloorPlan() 
    {
        return this.floorPlan;
    }

    @Override
    public String toString() 
    {
        String map = "";
        
        for(int i = 0; i < floorPlan.length; i++)
        {
            for(int j = 0; j < floorPlan[i].length; j++)
            {
                if (!floorPlan[i][j].isTaken()) 
                {
                    map += "X ";
                } 
                else 
                {
                    if (floorPlan[i][j].getOccupyingCarSize() == Size.SMALL) 
                    {
                        map += "S ";
                    } 
                    else 
                    {
                        map += "L ";
                    }
                }
            }
            map = map.strip();
            map += "\n";
        }
        
        return map.strip();
    }
}
