package parking.facility;

import java.util.ArrayList;
import parking.ParkingLot;
import vehicle.Car;
import vehicle.Size;

public class Gate 
{
    private final ArrayList<Car> cars;
    private final ParkingLot parkingLot;

    private static int ticketCounter = 1; // ez fogja adni a tickedID egyedi tagjat


    public Gate(ParkingLot parkingLot) 
    {
        this.parkingLot = parkingLot;
        this.cars = new ArrayList<Car>();
    }

 
    private Space findTakenSpaceByCar(Car c) 
    {
        Space[][] floorPlan = parkingLot.getFloorPlan();
        Space space;

        for (int i = 0; i < floorPlan.length; i++) 
        {
            for (int j = 0; j < floorPlan[i].length; j++) 
            {
                space = floorPlan[i][j];
                if (space.isTaken() && space.getCarLicensePlate().equals(c.getLicensePlate())) // aobjektum osszehasonlitasat nem == vegezzuk hanem equlas al
                {
                    return space;
                }
            }
        }

        return null;
    }

    private Space findAvailableSpaceOnFloor(int floor, Car c) 
    {
        Space[][] floorPlan = parkingLot.getFloorPlan();
        
        if (floor < 0 || floor >= floorPlan.length) 
        {
            return null;
        }
        
        if (c.getSpotOccupation() == Size.LARGE) 
        {
            for (int i = 0; i < floorPlan[floor].length - 1; i++) 
            {
                if (!floorPlan[floor][i].isTaken() && !floorPlan[floor][i + 1].isTaken()) 
                {
                    return floorPlan[floor][i];
                }
            }
        } 
        else 
        {
            for (Space space : floorPlan[floor]) 
            {
                if (!space.isTaken()) 
                {
                    return space;
                }
            }
        }

        return null;
    }

    public Space findAnyAvailableSpaceForCar(Car c) 
    {
        Space[][] floorPlan = parkingLot.getFloorPlan();
        
        for (int i = 0; i < floorPlan.length; i++) 
        {
            Space space = findAvailableSpaceOnFloor(i, c);

            if (space != null) // ugye a findAvailableSpaceOnFloor nullt dob ha nincs hely tehat ha space itt null akkor menjen tovabb a kereses
            {
                return space;
            }
        }

        return null;
    }

    public Space findPreferredAvailableSpaceForCar(Car c) 
    {
        Space[][] floorPlan = parkingLot.getFloorPlan();
        int preference = c.getPreferredFloor();
        
        if (preference >= 0 && preference < floorPlan.length) 
        {
            Space space = findAvailableSpaceOnFloor(preference, c);

            if (space != null) 
            {
                return space;
            }

            int lower;
            int higher;
            
            for (int asAboveAsBelow = 1; asAboveAsBelow < floorPlan.length; asAboveAsBelow++)
            {
                lower = preference - asAboveAsBelow;

                if (lower >= 0) 
                {
                    space = findAvailableSpaceOnFloor(lower, c);
                    if (space != null) 
                    {
                        return space;
                    }
                }
                
                higher = preference + asAboveAsBelow;

                if (higher < floorPlan.length) 
                {
                    space = findAvailableSpaceOnFloor(higher, c);
                    if (space != null) 
                    {
                        return space;
                    }
                }
            }
        }

        return null;
    }

    public boolean registerCar(Car c) 
    {
        if (cars.contains(c)) 
        {
            return false;
        }
        
        Space space = findPreferredAvailableSpaceForCar(c);

        if (space == null) 
        {
            return false;
        }
        
        if (c.getSpotOccupation() == Size.LARGE) 
        {
            Space[][] floorPlan = parkingLot.getFloorPlan();
            int floor = space.getFloorNumber();
            int spaceLoc = space.getSpaceNumber();
            
            if (spaceLoc + 1 < floorPlan[floor].length && !floorPlan[floor][spaceLoc + 1].isTaken()) 
            {   
                space.addOccupyingCar(c);
                floorPlan[floor][spaceLoc + 1].addOccupyingCar(c);
            } 
            else 
            {
                return false;
            }
        } 
        else 
        {
            space.addOccupyingCar(c);
        }

        String ticketId = "ALMA" + ticketCounter++;
        c.setTicketId(ticketId);
            
        cars.add(c);
            
        return true;
    }


    public void registerCars(Car... cars) 
    {
        for (Car car : cars) 
        {
            if (!registerCar(car)) 
            {
                System.out.println(car.getLicensePlate() + "rendszamu autot nem sikerult leparkolni.");
            }
        }
    }

    public void deRegisterCar(String ticketId) 
    {
        Car lookingToRemove = null;
        
        for (Car car : cars) {
            if (ticketId.equals(car.getTicketId()))
            {
                lookingToRemove = car;
                break;
            }
        }
        
        if (lookingToRemove != null) 
        {
            Space space = findTakenSpaceByCar(lookingToRemove);
            
            if (space != null) 
            {
                space.removeOccupyingCar();
                
                if (lookingToRemove.getSpotOccupation() == Size.LARGE) //ha nagy a kocsi akkor a mellete levo helyet is ki kell uriteni
                {
                    Space[][] floorPlan = parkingLot.getFloorPlan();
                    int floor = space.getFloorNumber();
                    int spaceLoc = space.getSpaceNumber();
                    
                    floorPlan[floor][spaceLoc + 1].removeOccupyingCar();
                }

                lookingToRemove.setTicketId(null); // ha kiparkol az auto akkor nullra allitom a ticketid jat es igy tesztelesnel tudom nezni hogy kiallt
                cars.remove(lookingToRemove);
            }
        }
    }
}