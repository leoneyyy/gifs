# gifs

Java‑Programm (Java 21), das aus **einem Video** eine **GIF-Datei** erstellt.  
Beim Start öffnet sich ein **Fenster**, in dem du ein Video auswählst – danach wird das GIF erzeugt.

## Voraussetzungen

- **Java 21**
- **Maven**
- (Falls benötigt) **FFmpeg** installiert und im `PATH`

## Build

```bash
mvn clean package
```

## Start

Je nach Build-Art (Jar mit/ohne Dependencies) kannst du es typischerweise so starten:

```bash
java -jar target/gifs.jar
```

> Falls deine erzeugte Datei anders heißt (z.B. `gifs-1.0.0.jar`), nimm diesen Namen aus `target/`.

## Verwendung

1. Programm starten (öffnet ein Fenster)
2. Video-Datei auswählen (z.B. `.mp4`, `.mov`, …)
3. Das Programm erstellt daraus eine `.gif` Datei

**Ausgabe:** Das GIF wird gespeichert **(Ausgabeort bitte anpassen, z.B. im gleichen Ordner wie das Video / im Projektordner / frei wählbar)**.

## Lizenz

MIT License — siehe [LICENSE](LICENSE).
