package parking;

import parking.ParkingLot;
import parking.facility.Gate;
import vehicle.Car;
import vehicle.Size;
import parking.ParkingLot;
import static check.CheckThat.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import check.*;

public class ParkingLotTest 
{
    @Test 
    public void testConstructorWithInvalidValues() 
    {
        //IllegalArgumentException dobnak 
        assertThrows(IllegalArgumentException.class, () -> new ParkingLot(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> new ParkingLot(3, -2));
        assertThrows(IllegalArgumentException.class, () -> new ParkingLot(0, 10));

        //ok nem
        assertDoesNotThrow(() -> new ParkingLot(1, 1));
        assertDoesNotThrow(() -> new ParkingLot(10, 20));
        assertDoesNotThrow(() -> new ParkingLot(2, 20));
    }

    @Test
    public void testTextualRepresentation() 
    {
        ParkingLot parkingLot = new ParkingLot(3, 3);
        Gate gate = new Gate(parkingLot);

        //ures parkolo 
        String emptyParkingLot = "X X X\nX X X\nX X X";
        
        assertEquals(emptyParkingLot,parkingLot.toString());

        Car car1 = new Car("Alma123", Size.SMALL, 0);
        Car car2 = new Car("Korte123", Size.LARGE, 1);
        Car car3 = new Car("Barack123", Size.SMALL, 2);

        //feltoljuk autokkal
        gate.registerCar(car1);
        gate.registerCar(car2);
        gate.registerCar(car3);

        String notEmptyParkingLot = "S X X\nL L X\nS X X";

        assertEquals(notEmptyParkingLot,parkingLot.toString());
        //kiparkolunk 1et
        gate.deRegisterCar(car1.getTicketId());

        String car1Rem = "X X X\nL L X\nS X X";

        assertEquals(car1Rem,parkingLot.toString());
        //majd mindent
        gate.deRegisterCar(car2.getTicketId());
        gate.deRegisterCar(car3.getTicketId());

        //ismet ures lett a parkolo
        assertEquals(emptyParkingLot,parkingLot.toString());
    }
}