import org.junit.Test;
import java.time.LocalDate;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RedundantMethodsTest {
    RedundantMethods redundantMethods = new RedundantMethods();
    DatabaseHelper databaseHelper = new DatabaseHelper();

    /* --------------------------------
    * VIN TEST
    * */
    @Test
    public void validVin() {
        assertTrue(redundantMethods.isVinValid("WAUZZZ4G0HN031091"));
    }

    @Test
    public void invalidVin() {
        assertFalse(redundantMethods.isVinValid("WAUZZZ4G0HN03109X"));
    }

    @Test
    public void testVinWithWrongLength() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            redundantMethods.isVinValid("1234567890123456"); // 16 characters instead of 17
        });
        assertEquals("VIN number must be 17 characters", exception.getMessage());
    }
    /* --------------------------------
    * DAYS LEFT TEST
    */
    @Test
    public void futureDays() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        assertEquals(10, redundantMethods.daysLeft(futureDate));
    }

    @Test
    public void pastDays() {
        LocalDate pastDate = LocalDate.now().minusDays(10);
        assertEquals(-10, redundantMethods.daysLeft(pastDate));
    }

    @Test
    public void currentDay() {
        LocalDate currentDate = LocalDate.now();
        assertEquals(0, redundantMethods.daysLeft(currentDate));
    }

    /* --------------------------------
    * INSERT CAR
    * */
    @Test
    public void insertOneCar() {
        String plate = "Test plate";
        String vin = "WBACR61050LJ33384";
        LocalDate rcaDate = LocalDate.now().plusDays(10);
        LocalDate insDate = LocalDate.now().plusDays(15);

        databaseHelper.insertValue(plate, vin, rcaDate, insDate, 2, "4500", "1200");
        assertTrue(databaseHelper.getValue(plate));
    }

    @Test
    public void deleteOneCar() {
        String plate = "Test plate";

        databaseHelper.removeValue(plate);
        assertFalse(databaseHelper.getValue(plate));
    }

    @Test
    public void insert100Cars() {
        String plate = "Test plate";
        String vin = "WBACR61050LJ33384";
        LocalDate rcaDate = LocalDate.now().plusDays(10);
        LocalDate insDate = LocalDate.now().plusDays(15);

        for(int i = 0; i < 100; i++) {
            databaseHelper.insertValue(plate + i, vin, rcaDate, insDate, 2, "4500", "1200");
        }
        for(int i = 0; i < 100; i++) {
            assertTrue(databaseHelper.getValue(plate + i));
        }
    }

    @Test
    public void delete100Cars() {
        String plate = "Test plate";
        for(int i = 0; i < 100; i++) {
            databaseHelper.removeValue(plate + i);
        }
        for(int i = 0; i < 100; i++) {
            assertFalse(databaseHelper.getValue(plate + i));
        }
    }
}
