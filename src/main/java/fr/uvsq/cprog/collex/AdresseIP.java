public class AdresseIP {
    private final String ip;

    public AdresseIP(String ip) {
        // Validation du format IPv4 simple
         if (ip == null) {
        throw new IllegalArgumentException("L'adresse IP ne doit pas être nulle");
     }
        if (!ip.matches("\\d{1,3}(\\.\\d{1,3}){3}")) {
            throw new IllegalArgumentException("Format d'adresse IP invalide : " + ip);
        }
        this.ip = ip;
    }

    public String getAdresse() {
        return ip;
    }
}