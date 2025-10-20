import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class DnsTest {
    private static final String TEST_FILE = "test-dns.txt";
    private Dns dns;

    @Before
    public void setUp() throws IOException {
        // Créer un fichier de test
        Files.write(Paths.get(TEST_FILE), 
            List.of("www.uvsq.fr 193.51.31.90", 
                    "ecampus.uvsq.fr 193.51.25.12",
                    "poste.uvsq.fr 193.51.31.154"));
        
        dns = new Dns(TEST_FILE);
    }

    @Test
    public void testChargementBaseDeDonnees() {
        assertEquals(3, dns.getNombreItems());
    }

    @Test
    public void testGetItemParAdresseIP() {
        AdresseIP ip = new AdresseIP("193.51.31.90");
        DnsItem item = dns.getItem(ip);
        
        assertNotNull(item);
        assertEquals("193.51.31.90", item.getAdresseIP().getAdresse());
        assertEquals("www.uvsq.fr", item.getNomMachine().getNom());
    }

    @Test
    public void testGetItemParNomMachine() {
        NomMachine nom = new NomMachine("www.uvsq.fr");
        DnsItem item = dns.getItem(nom);
        
        assertNotNull(item);
        assertEquals("www.uvsq.fr", item.getNomMachine().getNom());
        assertEquals("193.51.31.90", item.getAdresseIP().getAdresse());
    }

    @Test
    public void testGetItemParStringIP() {
        DnsItem item = dns.getItem("193.51.31.90");
        assertNotNull(item);
        assertEquals("www.uvsq.fr", item.getNomMachine().getNom());
    }

    @Test
    public void testGetItemParStringNom() {
        DnsItem item = dns.getItem("www.uvsq.fr");
        assertNotNull(item);
        assertEquals("193.51.31.90", item.getAdresseIP().getAdresse());
    }
}