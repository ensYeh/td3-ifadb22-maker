import static org.junit.Assert.*;
import org.junit.Test;

public class AdresseIPTest {

    @Test
    public void testValidAdresseIP() {
        AdresseIP ip = new AdresseIP("192.168.1.10");
        assertEquals("192.168.1.10", ip.getAdresse());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAdresseIP_Null() {
        new AdresseIP(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAdresseIP_Format() {
        new AdresseIP("192.168.1");
    }
    }
    
