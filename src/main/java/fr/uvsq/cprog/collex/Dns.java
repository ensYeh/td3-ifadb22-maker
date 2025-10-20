package fr.uvsq.cprog.collex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Dns {
  private static final Logger logger = LoggerFactory.getLogger(Dns.class);
  private final List<DnsItem> database;
  private final String dataFilePath;


  public Dns(String dataFilePath) throws IOException {
    this.database = new ArrayList<>();
    this.dataFilePath = dataFilePath;
    loadDatabase();
  }


  private void loadDatabase() throws IOException {
    try (InputStream input = Dns.class.getResourceAsStream(dataFilePath)) {
      if (input == null) {
        throw new IOException("DNS data file not found: " + dataFilePath);
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length == 2) {
          try {
            DnsItem item = new DnsItem(new AdresseIP(parts[1]), new NomMachine(parts[0]));
            database.add(item);
          } catch (IllegalArgumentException e) {
            logger.warn("Invalid entry in DNS data file: {}", line);
          }
        }
      }
    }
  }

  public DnsItem getItem(AdresseIP ip) {
    for (DnsItem item : database) {
      if (item.getIp().equals(ip)) {
        return item;
      }
    }
    return null;
  }


  public DnsItem getItem(NomMachine name) {
    for (DnsItem item : database) {
      if (item.getName().equals(name)) {
        return item;
      }
    }
    return null;
  }


  public List<DnsItem> getItems(String domain, boolean sortByIp) {
    List<DnsItem> items = new ArrayList<>();
    for (DnsItem item : database) {
      if (item.getName().getDomain().equals(domain)) {
        items.add(item);
      }
    }
    if (sortByIp) {
      Collections.sort(items, (a, b) -> a.getIp().getIpAddress().compareTo(b.getIp().getIpAddress()));
    } else {
      Collections.sort(items, (a, b) -> a.getName().getMachineName().compareTo(b.getName().getMachineName()));
    }
    return items;
  }

  public void addItem(AdresseIP ip, NomMachine name) throws IOException {
    if (getItem(ip) != null) {
      throw new IllegalArgumentException("IP address already exists: " + ip);
    }
    if (getItem(name) != null) {
      throw new IllegalArgumentException("Machine name already exists: " + name);
    }
    DnsItem item = new DnsItem(ip, name);
    database.add(item);
    updateDataFile();
  }

  private void updateDataFile() throws IOException {
    Path path = Path.of("dns_data.txt");
    List<String> lines = new ArrayList<>();
    for (DnsItem item : database) {
      lines.add(item.toString());
    }
    Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }
}
