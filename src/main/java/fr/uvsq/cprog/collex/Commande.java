import java.io.IOException;
import java.util.List;

public interface Commande {
    void executer(DnsTUI tui, Dns dns);
}

class CommandeRechercheNom implements Commande {
    private final String nomMachine;
    
    public CommandeRechercheNom(String nomMachine) {
        this.nomMachine = nomMachine;
    }
    
    public String getNomMachine() {
        return nomMachine;
    }
    
    @Override
    public void executer(DnsTUI tui, Dns dns) {
        DnsItem item = dns.getItem(nomMachine);
        tui.affiche(item);
    }
}

class CommandeRechercheIP implements Commande {
    private final String ip;
    
    public CommandeRechercheIP(String ip) {
        this.ip = ip;
    }
    
    public String getIp() {
        return ip;
    }
    
    @Override
    public void executer(DnsTUI tui, Dns dns) {
        DnsItem item = dns.getItem(ip);
        tui.affiche(item);
    }
}

class CommandeListeDomaine implements Commande {
    private final String domaine;
    private final boolean trierParIP;
    
    public CommandeListeDomaine(String domaine, boolean trierParIP) {
        this.domaine = domaine;
        this.trierParIP = trierParIP;
    }
    
    public String getDomaine() {
        return domaine;
    }
    
    public boolean isTrierParIP() {
        return trierParIP;
    }
    
    @Override
    public void executer(DnsTUI tui, Dns dns) {
        List<DnsItem> items = dns.getItems(domaine);
        tui.affiche(items, trierParIP);
    }
}

class CommandeAjout implements Commande {
    private final String ip;
    private final String nomMachine;
    
    public CommandeAjout(String ip, String nomMachine) {
        this.ip = ip;
        this.nomMachine = nomMachine;
    }
    
    public String getIp() {
        return ip;
    }
    
    public String getNomMachine() {
        return nomMachine;
    }
    
    @Override
    public void executer(DnsTUI tui, Dns dns) {
        try {
            dns.addItem(ip, nomMachine);
            tui.affiche("Entrée ajoutée avec succès: " + nomMachine + " -> " + ip);
        } catch (IllegalArgumentException e) {
            tui.affiche(e.getMessage());
        } catch (IOException e) {
            tui.affiche("ERREUR: Impossible de sauvegarder: " + e.getMessage());
        }
    }
}

class CommandeQuit implements Commande {
    @Override
    public void executer(DnsTUI tui, Dns dns) {
        // Traitement fait dans DnsTUI.demarrer()
    }
}

class CommandeInvalide implements Commande {
    private final String messageErreur;
    
    public CommandeInvalide(String messageErreur) {
        this.messageErreur = messageErreur;
    }
    
    public String getMessageErreur() {
        return messageErreur;
    }
    
    @Override
    public void executer(DnsTUI tui, Dns dns) {
        tui.affiche(messageErreur);
    }
}