import static org.junit.Assert.*;
import org.junit.Test;

public class DnsItemTest {
    @Test
    public void testValidDnsItem() {
        AdresseIP ip = new AdresseIP("192.168.0.1");
        NomMachine nom = new NomMachine("serveur.domain.com");
        DnsItem dns = new DnsItem(ip, nom);
        assertEquals("serveur.domain.com", dns.getNomMachine().getNom());
        assertEquals("192.168.0.1", dns.getAdresseIP().getAdresse());
    }
}