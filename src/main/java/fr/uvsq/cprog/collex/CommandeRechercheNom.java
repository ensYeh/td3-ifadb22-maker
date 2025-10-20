package fr.uvsq.cprog.collex;

public class CommandeRechercheNom implements Commande {
    private final Dns dns;
    private final String nomMachine;

    public CommandeRechercheNom(Dns dns, String nomMachine) {
        this.dns = dns;
        this.nomMachine = nomMachine;
    }

    @Override
    public String execute() {
        DnsItem item = dns.getItem(nomMachine);
        if (item != null) {
            return item.getAdresseIP().getAdresse();
        } else {
            return "Aucun résultat trouvé pour: " + nomMachine;
        }
    }
}