package fr.uvsq.cprog.collex;

import java.util.List;
import java.util.stream.Collectors;

public class CommandeListeDomaine implements Commande {
    private final Dns dns;
    private final String domaine;
    private final boolean trierParIP;

    public CommandeListeDomaine(Dns dns, String domaine, boolean trierParIP) {
        this.dns = dns;
        this.domaine = domaine;
        this.trierParIP = trierParIP;
    }

    @Override
    public String execute() {
        List<DnsItem> items = dns.getItems(domaine);
        if (items == null || items.isEmpty()) {
            return "Aucun résultat trouvé pour le domaine: " + domaine;
        }

        List<DnsItem> itemsTries;
        if (trierParIP) {
            itemsTries = items.stream()
                .sorted((i1, i2) -> i1.getAdresseIP().getAdresse().compareTo(i2.getAdresseIP().getAdresse()))
                .collect(Collectors.toList());
        } else {
            itemsTries = items.stream()
                .sorted((i1, i2) -> i1.getNomMachine().getNom().compareTo(i2.getNomMachine().getNom()))
                .collect(Collectors.toList());
        }

        StringBuilder result = new StringBuilder();
        for (DnsItem item : itemsTries) {
            result.append(item.getAdresseIP().getAdresse())
                  .append(" ")
                  .append(item.getNomMachine().getNom())
                  .append("\n");
        }
        return result.toString().trim();
    }
}