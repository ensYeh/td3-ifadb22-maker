package fr.uvsq.cprog.collex;

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
        dns = new Dns("test-dns.txt");
        tui = new DnsTUI(dns);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testNextCommandeRechercheNom() {
        Commande cmd = testerCommande("www.uvsq.fr");
        assertTrue(cmd instanceof CommandeRechercheNom);
    }

    @Test
    public void testNextCommandeRechercheIP() {
        Commande cmd = testerCommande("193.51.31.90");
        assertTrue(cmd instanceof CommandeRechercheIP);
    }

    @Test
    public void testNextCommandeLs() {
        Commande cmd = testerCommande("ls uvsq.fr");
        assertTrue(cmd instanceof CommandeListeDomaine);
    }

    @Test
    public void testNextCommandeLsAvecTri() {
        Commande cmd = testerCommande("ls -a uvsq.fr");
        assertTrue(cmd instanceof CommandeListeDomaine);
    }

    @Test
    public void testNextCommandeAdd() {
        Commande cmd = testerCommande("add 192.168.1.1 test.domaine.fr");
        assertTrue(cmd instanceof CommandeAjout);
    }

    @Test
    public void testNextCommandeQuit() {
        Commande cmd = testerCommande("quit");
        assertTrue(cmd instanceof CommandeQuit);
    }

    @Test
    public void testNextCommandeInvalide() {
        Commande cmd = testerCommande("commande_invalide");
        assertTrue(cmd instanceof CommandeInvalide);
    }

    @Test
    public void testNextCommandeLigneVide() {
        Commande cmd = testerCommande("");
        assertNull(cmd);
    }

    @Test
    public void testAffiche() {
        tui.affiche("Test message");
        assertTrue(outputStream.toString().contains("Test message"));
    }

    @Test
    public void testExecuteCommandeRechercheNom() {
        CommandeRechercheNom cmd = new CommandeRechercheNom(dns, "www.uvsq.fr");
        String result = cmd.execute();
        assertTrue(result.contains("193.51.31.90"));
    }

    @Test
    public void testExecuteCommandeRechercheIP() {
        CommandeRechercheIP cmd = new CommandeRechercheIP(dns, "193.51.31.90");
        String result = cmd.execute();
        assertTrue(result.contains("www.uvsq.fr"));
    }

    @Test
    public void testExecuteCommandeListeDomaine() {
        CommandeListeDomaine cmd = new CommandeListeDomaine(dns, "uvsq.fr", false);
        String result = cmd.execute();
        assertTrue(result.contains("uvsq.fr"));
    }

    @Test
    public void testExecuteCommandeInvalide() {
        CommandeInvalide cmd = new CommandeInvalide("Test erreur");
        String result = cmd.execute();
        assertTrue(result.contains("ERREUR: Test erreur"));
    }

    @Test
    public void testExecuteCommandeQuit() {
        CommandeQuit cmd = new CommandeQuit();
        String result = cmd.execute();
        assertEquals("quit", result);
    }

    private Commande testerCommande(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream((input + "\n").getBytes());
        System.setIn(in);
        return tui.nextCommande();
    }
}