package parking.facility;

import parking.ParkingLot;
import vehicle.Size;
import vehicle.Car;
import parking.facility.Gate;
import static check.CheckThat.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

public class GateTest 
{
    @Test 
    public void testFindAnyAvailableSpaceForCar() 
    {
        //tenylegesen nem parkoltatjuk le oket mert itt csak a hlykerseset ellenorrziuk
        ParkingLot parkingLot = new ParkingLot(2, 2);
        Gate gate = new Gate(parkingLot);
        // kis autora megnezzuk hogy le tud e parkolni
        Car car1 = new Car("Alma123", Size.SMALL, 1);
        assertNotNull(gate.findAnyAvailableSpaceForCar(car1));

        // nagy autora is
        Car car2 = new Car("kor123", Size.LARGE, 2);
        assertNotNull(gate.findAnyAvailableSpaceForCar(car2));

        //feltoltjuk a parkolot mert igy nullt kell dobnia a keresonek es ezt is teszteljuk
        Car placeHolder1 = new Car("Alma123",Size.LARGE,1);
        Car placeHolder2 = new Car("kor123",Size.LARGE,1);

        gate.registerCar(placeHolder1);
        gate.registerCar(placeHolder2);

        assertEquals(gate.findAnyAvailableSpaceForCar(car1),null);
        assertEquals(gate.findAnyAvailableSpaceForCar(car2),null);


    }

    @ParameterizedTest
    @CsvSource

    ({
        "Alma123, SMALL, 0",
        "Korte123, LARGE, 1",
        "Barack123, SMALL, 2"

    })
    public void testFindPreferredAvailableSpaceForCar(String plate, Size size, int preferredFloor) 
    {
        //letrehozok egy 3 emeletes emeletenkent 2 parkolohelyes parkolot(azert 2 hogy a nagy kocsik is elferjenek)
        ParkingLot parkingLot = new ParkingLot(3, 2);
        Gate gate = new Gate(parkingLot);
        // a parameterek alapjan letrehozza a kocsit
        Car car = new Car(plate, size, preferredFloor);
        //mivel ures a parkolo mert nincsen regisztralas ezert biztosan talal helyet
        Space space = gate.findPreferredAvailableSpaceForCar(car);
        //osszehasonlitom a tenylegesen foglalt helyet a kivant helyel
        assertEquals(preferredFloor, space.getFloorNumber());
        //megnezzuk ha a kedvenc helyet nem talalja akkor ugye elindul lefelees folfele is es hogy ez mukodik e

        Car car2 = new Car("Teszt123", Size.LARGE,preferredFloor); //letrehozunk egy uj autot ugyanazzal a kedvenc szintel es le is parkoltatjuk
        gate.registerCar(car2);

        space = gate.findPreferredAvailableSpaceForCar(car); //ekkor mar nem tud a kedvenc helyere menni ezert lefele kezd el e nezni(ha tud)

        if(preferredFloor > 0) //ha a 0 szint a kedvence akkor csak folfele tud nezni
        {
            assertEquals(space.getFloorNumber(),preferredFloor - 1); // azert +1 es -1 mert a parkolo meg mindig ures a tobbi szinten es igy biztos hogy le tud parkolni egyel alatta vagy folotte
        }
        else
        {
            assertEquals(space.getFloorNumber(),preferredFloor + 1);
        }
        

    }

    @ParameterizedTest
    @CsvSource
    ({
        "Alma123, SMALL, 0",
        "Korte123, LARGE, 1",
        "Barack123, SMALL, 1"
    })

    public void testRegisterCar(String plate, Size size, int preferredFloor) 
    {
        ParkingLot parkingLot = new ParkingLot(2, 2);
        Gate gate = new Gate(parkingLot);
        
        Car car = new Car(plate, size, preferredFloor);
        
        assertTrue(gate.registerCar(car)); //mivel hely van boven es a registercar a preferd spacet hasznalja ezert biztosan lesz helyuk es mivel egy booleannal ter vissza ezert rogton tudjuk nezni hogy tenyleg talalt e
        gate.deRegisterCar(car.getTicketId());

        Car placeHolder1 = new Car("Alma123",Size.LARGE,1);
        Car placeHolder2 = new Car("kor123",Size.LARGE,1);
        //feltoljuk a parkolot igy hamisat kell visszahoznia he most megprobaljuk leparkolni
        gate.registerCar(placeHolder1);
        gate.registerCar(placeHolder2);

        assertFalse(gate.registerCar(car));



    }

    @ParameterizedTest
    @CsvSource
    
    ({
        "Alma123, SMALL, 0",
        "Korte123, LARGE, 1",
        "Barack123, SMALL, 2"
    })
    public void testDeRegisterCar(String plate, Size size, int preferredFloor) 
    {
        ParkingLot parkingLot = new ParkingLot(3, 2);
        Gate gate = new Gate(parkingLot);
        
        Car car = new Car(plate, size, preferredFloor);
        gate.registerCar(car);
        assertNotNull(car.getTicketId());

        gate.deRegisterCar(car.getTicketId());

        assertEquals(car.getTicketId(),null);
    }
}