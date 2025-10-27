package parking.facility;

import vehicle.Car;
import vehicle.Size;

public class Space 
{
    private final int floorNumber;

    public int getFloorNumber() 
    {
        return floorNumber;
    }

    private final int spaceNumber;

    public int getSpaceNumber() 
    {
        return this.spaceNumber;
    }

    private Car occupyingCar;

    public Space(int floorNumber, int spaceNumber) 
    {
        this.floorNumber = floorNumber;
        this.spaceNumber = spaceNumber;
    }

    public boolean isTaken() 
    {
        return this.occupyingCar != null;
    }

    public void addOccupyingCar(vehicle.Car c) 
    {
        this.occupyingCar = c;
    }

    public void removeOccupyingCar() 
    {
        this.occupyingCar = null;
    }

    public String getCarLicensePlate() 
    {
        return occupyingCar.getLicensePlate();
    }

    public vehicle.Size getOccupyingCarSize() 
    {
        return this.occupyingCar.getSpotOccupation();
    }
}