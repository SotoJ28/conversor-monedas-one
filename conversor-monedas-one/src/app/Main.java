package app;

import domain.ConversionRequest;
import domain.ConversionResult;
import exception.ApiException;
import service.CurrencyConverter;
import service.HistoryService;
import service.HttpExchangeRateClient;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    private static final String ENDPOINT = "https://v6.exchangerate-api.com/v6/%s/latest/%s";

    public static void main(String[] args) {

        String apiKey = loadApiKey();

        var rateClient = new HttpExchangeRateClient(ENDPOINT, apiKey);
        var converter = new CurrencyConverter(rateClient);
        var history = new HistoryService("historial.txt");

        try (Scanner sc = new Scanner(System.in)) {
            int option = -1;

            while (option != 7) {
                printMenu();
                option = readInt(sc);

                try {
                    switch (option) {
                        case 1:
                            convert(sc, converter, history, "CRC", "USD");
                            break;
                        case 2:
                            convert(sc, converter, history, "USD", "CRC");
                            break;
                        case 7:
                            System.out.println("¡Gracias por usar el conversor!");
                            break;
                        default:
                            System.out.println("Opción inválida. Intente de nuevo.\n");
                    }
                } catch (ApiException e) {
                    System.out.println("Error al consultar la API: " + e.getMessage() + "\n");
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage() + "\n");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("*************************************************");
        System.out.println("Sea bienvenido/a al Conversor de Moneda =]");
        System.out.println();
        System.out.println("1) Colón costarricense (CRC) => Dólar (USD)");
        System.out.println("2) Dólar (USD) => Colón costarricense (CRC)");
        System.out.println("7) Salir");
        System.out.print("Elija una opción válida: ");
    }

    private static void convert(Scanner sc, CurrencyConverter converter, HistoryService history,
                                String from, String to) {

        BigDecimal amount = readAmount(sc);

        ConversionResult r = converter.convert(new ConversionRequest(from, to, amount));

        System.out.printf("El valor %.2f [%s] corresponde al valor final de =>>> %.2f [%s]%n%n",
                r.getAmount(), r.getFrom(),
                r.getConverted(), r.getTo()
        );

        history.append(r);
    }

    private static int readInt(Scanner sc) {
        while (true) {
            String input = sc.next().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    private static BigDecimal readAmount(Scanner sc) {
        System.out.print("Ingrese el valor que desea convertir: ");
        while (true) {
            String input = sc.next().trim().replace(",", ".");
            try {
                BigDecimal amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.print("El monto debe ser mayor a 0. Intente de nuevo: ");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                System.out.print("Monto inválido. Intente de nuevo: ");
            }
        }
    }

    private static String loadApiKey() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config.properties"));
            String key = props.getProperty("API_KEY");
            if (key == null || key.trim().isEmpty()) {
                throw new RuntimeException("API_KEY no configurada en config.properties");
            }
            return key.trim();
        } catch (Exception e) {
            throw new RuntimeException(
                    "No se pudo leer config.properties. Cree el archivo basado en config.properties.example", e
            );
        }
    }
}

