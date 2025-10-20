import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DnsTUITest {
    private Dns dns;
    private DnsTUI tui;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        // Créer un DNS de test
        dns = new Dns("test-dns.txt");
        tui = new DnsTUI(dns);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testAnalyserCommandeRechercheNom() {
        Commande cmd = analyserCommande("www.uvsq.fr");
        assertTrue(cmd instanceof CommandeRechercheNom);
    }

    @Test
    public void testAnalyserCommandeRechercheIP() {
        Commande cmd = analyserCommande("193.51.31.90");
        assertTrue(cmd instanceof CommandeRechercheIP);
    }

    @Test
    public void testAnalyserCommandeLs() {
        Commande cmd = analyserCommande("ls uvsq.fr");
        assertTrue(cmd instanceof CommandeListeDomaine);
        
        CommandeListeDomaine cmdLs = (CommandeListeDomaine) cmd;
        assertEquals("uvsq.fr", cmdLs.getDomaine());
        assertFalse(cmdLs.isTrierParIP());
    }

    @Test
    public void testAnalyserCommandeLsAvecTri() {
        Commande cmd = analyserCommande("ls -a uvsq.fr");
        assertTrue(cmd instanceof CommandeListeDomaine);
        
        CommandeListeDomaine cmdLs = (CommandeListeDomaine) cmd;
        assertEquals("uvsq.fr", cmdLs.getDomaine());
        assertTrue(cmdLs.isTrierParIP());
    }

    @Test
    public void testAnalyserCommandeAdd() {
        Commande cmd = analyserCommande("add 192.168.1.1 test.domaine.fr");
        assertTrue(cmd instanceof CommandeAjout);
        
        CommandeAjout cmdAdd = (CommandeAjout) cmd;
        assertEquals("192.168.1.1", cmdAdd.getIp());
        assertEquals("test.domaine.fr", cmdAdd.getNomMachine());
    }

    @Test
    public void testAnalyserCommandeQuit() {
        Commande cmd = analyserCommande("quit");
        assertTrue(cmd instanceof CommandeQuit);
    }

    @Test
    public void testAfficheString() {
        tui.affiche("Test message");
        assertTrue(outputStream.toString().contains("Test message"));
    }

    @Test
    public void testAfficheItemNull() {
        tui.affiche((DnsItem) null);
        assertTrue(outputStream.toString().contains("Aucun résultat trouvé"));
    }

    // Méthode helper pour analyser les commandes
    private Commande analyserCommande(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream((input + "\n").getBytes());
        System.setIn(in);
        DnsTUI testTui = new DnsTUI(dns);
        return testTui.nextCommande();
    }
}