import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CommandeTest {
    private Dns dns;
    private DnsTUI tui;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        dns = new Dns("test-dns.txt");
        tui = new DnsTUI(dns);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testCommandeRechercheNom() {
        Commande cmd = new CommandeRechercheNom("www.uvsq.fr");
        cmd.executer(tui, dns);
        String output = outputStream.toString();
        assertTrue(output.contains("193.51.31.90") && output.contains("www.uvsq.fr"));
    }

    @Test
    public void testCommandeRechercheIP() {
        Commande cmd = new CommandeRechercheIP("193.51.31.90");
        cmd.executer(tui, dns);
        String output = outputStream.toString();
        assertTrue(output.contains("193.51.31.90") && output.contains("www.uvsq.fr"));
    }

    @Test
    public void testCommandeListeDomaine() {
        Commande cmd = new CommandeListeDomaine("uvsq.fr", false);
        cmd.executer(tui, dns);
        String output = outputStream.toString();
        assertTrue(output.contains("uvsq.fr"));
    }

    @Test
    public void testCommandeInvalide() {
        Commande cmd = new CommandeInvalide("Message d'erreur test");
        cmd.executer(tui, dns);
        assertTrue(outputStream.toString().contains("Message d'erreur test"));
    }

    @Test
    public void testGettersCommandeListeDomaine() {
        CommandeListeDomaine cmd = new CommandeListeDomaine("test.fr", true);
        assertEquals("test.fr", cmd.getDomaine());
        assertTrue(cmd.isTrierParIP());
    }

    @Test
    public void testGettersCommandeAjout() {
        CommandeAjout cmd = new CommandeAjout("192.168.1.1", "test.domaine.fr");
        assertEquals("192.168.1.1", cmd.getIp());
        assertEquals("test.domaine.fr", cmd.getNomMachine());
    }
}