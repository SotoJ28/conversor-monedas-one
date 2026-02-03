package service;

import domain.ConversionResult;

import java.io.FileWriter;
import java.io.IOException;

public class HistoryService {

    private final String filename;

    public HistoryService(String filename) {
        this.filename = filename;
    }

    public void append(ConversionResult r) {
        String line = String.format(
                "%s | %s %.2f -> %s %.2f | rate=%s%n",
                r.getTimestamp(),
                r.getFrom(), r.getAmount(),
                r.getTo(), r.getConverted(),
                r.getRate().toPlainString()
        );

        try (FileWriter fw = new FileWriter(filename, true)) {
            fw.write(line);
        } catch (IOException e) {
            System.out.println("No se pudo guardar historial: " + e.getMessage());
        }
    }
}


