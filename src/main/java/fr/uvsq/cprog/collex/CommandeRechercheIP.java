package fr.uvsq.cprog.collex;

public class CommandeRechercheIP implements Commande {
    private final Dns dns;
    private final String ip;

    public CommandeRechercheIP(Dns dns, String ip) {
        this.dns = dns;
        this.ip = ip;
    }

    @Override
    public String execute() {
        DnsItem item = dns.getItem(ip);
        if (item != null) {
            return item.getNomMachine().getNom();
        } else {
            return "Aucun résultat trouvé pour: " + ip;
        }
    }
}