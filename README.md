# Testballon zur Anbindung eines Kafka-Servers
Dieses Projekt dient zum Testen einer Kafka-Anbindung.
Für die Integration im Projekt wird die Bibliothek `org.apache.kafka.kafka-clients` verwendet.

## Kurzbeschreibung
Das Projekt besteht aus 2 Anwendungen (mit jeweils einer eigenen Main-Methode).
- KafkaProducerApp.java
- KafkaConsumerApp.java

Die KafkaProducerApp sendet n-mal die gleiche Testnachricht an ein Kafka-Topic (test-topic).
Nach diesem Durchlauf beendet sie sich - fertig.

Die KafkaConsumerApp pollt auf dem Topic (test-topic) nach neuen Nachrichten.
Die Nachrichten werden ausgelesen und als Datei in das Verzeichnis /target/out geschrieben.
Insgesamt pollt die Anwendung 10 mal nach neuen Nachrichten.
Nach diesem Durchlauf beendet sie sich - fertig.

## Voraussetzungen
  - Docker
    - unter /etc/docker liegt ein docker-compose.yml (v2) das die nötigen Container startet.
  - Java 8
  - Maven
  - Webbrowser für die Kafka-UI

## Getting started
1. baue das Projekt mit Maven
  - Command: `mvn clean install`
2. Starte Kafka (und die anderen Container) aus dem lokalen Verzeichnis /etc/docker mit dem Command: `docker compose up` oder `docker compose up -d` um das Terminal nicht zu blockieren (d = detached mode).
  - In dem Verzeichnis liegt die Datei docker-compose.yml welche die Services bzw. Container definiert
  - Es werden folgende Container gestartet
    - Zookeeper - wird intern von Kafka benötigt
    - Kafka
    - Kafka-UI - eine Web-UI, um Kafka zu verwalten
3. Öffne die Kafka-UI http://localhost:8080/ im Browser
  - erstelle ein neues Topic mit dem Namen "test-topic"
  - weise dem Topic die gewünschte Anzahl von Partitionen zu (1 macht in diesem Mini-Setup Sinn)
  - stelle sicher, dass du das Topic über die Kafka-UI gespeichert hast und dass es in der Liste der Topics angezeigt wird
4. Starte die Producer-Anwendung (KafkaProducer.java)
  - Dies kann man z.B. direkt in der IDE (IntelliJ) machen
  - Die Anwendung sollte 10 Nachrichten an den Kafka senden und sich dann beenden
  - Kontrolliere in der Kafka-UI unter dem "test-topic" die Messages und stelle sicher, dass sie angekommen sind
5. Starte die Consumer-Anwendung (KafkaConsumer.java)
  - Dies kann man z.B. direkt in der IDE (IntelliJ) machen
  - Schau die Log-Ausgaben an
  - Kontrolliere das Ausgabeverzeichnis /target/out
  - Die Nachrichten sollten alle konsumiert und in das Ausgangsverzeichnis geschrieben worden sein
6. Um die Docker-Container wieder zu entfernen führe aus dem Verzeichnis /etc/docker das Command: `docker compose down` aus.