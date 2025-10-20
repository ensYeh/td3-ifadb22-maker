import java.util.Objects;

public class DnsItem {
    private final AdresseIP adresseIP;
    private final NomMachine nomMachine;

    public DnsItem(AdresseIP adresseIP, NomMachine nomMachine) {
        if (adresseIP == null || nomMachine == null) {
            throw new IllegalArgumentException("L'adresse IP et le nom de machine ne peuvent pas être nuls");
        }
        this.adresseIP = adresseIP;
        this.nomMachine = nomMachine;
    }

    public DnsItem(String ip, String nomComplet) {
        this(new AdresseIP(ip), new NomMachine(nomComplet));
    }

    public AdresseIP getAdresseIP() {
        return adresseIP;
    }

    public NomMachine getNomMachine() {
        return nomMachine;
    }

    public static DnsItem fromLigne(String ligne) {
        if (ligne == null || ligne.trim().isEmpty()) {
            throw new IllegalArgumentException("Ligne vide");
        }
        
        String[] parties = ligne.trim().split("\\s+");
        if (parties.length != 2) {
            throw new IllegalArgumentException("Format de ligne invalide : " + ligne);
        }
        
        return new DnsItem(parties[1], parties[0]);
    }

    public String toLigne() {
        return nomMachine.getNom() + " " + adresseIP.getAdresse();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DnsItem dnsItem = (DnsItem) obj;
        return Objects.equals(adresseIP, dnsItem.adresseIP) && 
               Objects.equals(nomMachine, dnsItem.nomMachine);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(adresseIP, nomMachine);
    }
    
    @Override
    public String toString() {
        return adresseIP.getAdresse() + " " + nomMachine.getNom();
    }
}