import static org.junit.Assert.*;
import org.junit.Test;

public class NomMachineTest {
    @Test
    public void testValidNomMachine() {
        NomMachine nm = new NomMachine("serveur.domain.com");
        assertEquals("serveur.domain.com", nm.getNom());
    }
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNomMachine_Null() {
        new NomMachine(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNomMachine_Format() {
        new NomMachine("serveur01");
    }


}