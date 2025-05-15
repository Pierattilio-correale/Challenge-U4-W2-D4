package esercizio;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ProvaMain {
    public static void main(String[] args) {
//categoria dei libri
        Prodotto p1 = new Prodotto(35614L, "Twisted Games", "Books", 110);
        Prodotto p2 = new Prodotto(35224L, "Twisted Love", "Books", 20);
        Prodotto p3 = new Prodotto(38614L, "Twisted Hate", "Books", 102);
        Prodotto p3prov = new Prodotto(18314L, "Twisted Lies", "Books", 41);

        //categoria baby
        Prodotto p4 = new Prodotto(35614434L, "Ciuccio", "Baby", 21);
        Prodotto p5 = new Prodotto(15614141L, "Dionasauro", "Baby", 50);
        Prodotto p6 = new Prodotto(35631314L, "Pinguino", "Baby", 37);

        //categoria Boys
        Prodotto p7 = new Prodotto(12235614L, "Maglia", "Boys", 41);
        Prodotto p8 = new Prodotto(24535614L, "Pantaloni", "Boys", 61);
        Prodotto p9 = new Prodotto(97535614L, "Jeans", "Boys", 12);


        Cliente cliente1 = new Cliente(2347474L, "Mario", 2);
        Cliente cliente2 = new Cliente(4567474L, "Francesco", 2);
        Cliente cliente3 = new Cliente(8348474L, "Riccardo", 1);
        Cliente cliente4 = new Cliente(1245474L, "Luca", 3);
        Cliente cliente5 = new Cliente(6347474L, "Gennaro", 2);
        Cliente cliente6 = new Cliente(4347474L, "Orazio", 3);
        Cliente cliente7 = new Cliente(1789744L, "Osvaldo", 3);

        ArrayList<Cliente> clienti = new ArrayList<>(Arrays.asList(cliente1, cliente2, cliente3, cliente4, cliente5, cliente6, cliente7));

        ArrayList<Prodotto> prodotti = new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p3prov));

        ArrayList<Prodotto> op1 = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
        ArrayList<Prodotto> op2 = new ArrayList<>(Arrays.asList(p4, p5, p6));
        ArrayList<Prodotto> op3 = new ArrayList<>(Arrays.asList(p8, p9));


        Ordine o1 = new Ordine(247535L, "In arrivo", LocalDate.of(2000, 3, 10), LocalDate.of(2000, 3, 13), op1, cliente1);
        Ordine o2baby = new Ordine(247535L, "In arrivo", LocalDate.of(2021, 2, 3), LocalDate.of(2021, 2, 11), op2, cliente2);
        Ordine o2baby1 = new Ordine(247535L, "In arrivo", LocalDate.of(1999, 4, 7), LocalDate.of(1999, 2, 22), op2, cliente4);
        Ordine o2baby2 = new Ordine(247535L, "In arrivo", LocalDate.of(2010, 8, 10), LocalDate.of(2010, 7, 30), op2, cliente3);
        Ordine o3 = new Ordine(247535L, "In arrivo", LocalDate.of(2021, 2, 18), LocalDate.of(2021, 2, 22), op3, cliente2);


        ArrayList<Ordine> ordiniProdBaby = new ArrayList<>(Arrays.asList(o2baby1, o2baby, o2baby2, o1, o3));
        //Esercizio 1
        Map<Cliente, List<Ordine>> ordinemap = ordiniProdBaby.stream().collect(Collectors.groupingBy(Ordine::getCliente));
        System.out.println(ordinemap);

        //Esercizio 2
        System.out.println("____________");
        Map<Cliente, Double> venditaTotalePerOgniCliente = ordiniProdBaby.stream().collect(Collectors.groupingBy(Ordine::getCliente, Collectors.summingDouble(ordine -> ordine.getProdotto().stream().mapToDouble(Prodotto::getPrezzo).sum())));

        System.out.println(venditaTotalePerOgniCliente);

        //esercizio 3
        System.out.println("____________");
        OptionalDouble prodottopiucostoso = prodotti.stream().mapToDouble(Prodotto::getPrezzo).max();

        List<Prodotto> prodottiPiuCostosi = prodotti.stream().filter(prodotto -> prodotto.getPrezzo() == prodottopiucostoso.getAsDouble()).toList();
        System.out.println(prodottiPiuCostosi);

        //esercizio 4
        System.out.println("____________");
        OptionalDouble mediaPrezzo = ordiniProdBaby.stream().flatMap(ordine -> ordine.getProdotto().stream()).mapToDouble(Prodotto::getPrezzo).average();
        System.out.println(mediaPrezzo);

        //esercizio 5
        System.out.println("____________");
        Map<String, Double> prodottiPrezzoPerCategoria = prodotti.stream().collect(Collectors.groupingBy(Prodotto::getCategoria, Collectors.summingDouble(Prodotto::getPrezzo)));
        System.out.println(prodottiPrezzoPerCategoria);

        //esercizio 6


        String prodottiStringati = prodotti.stream().map(prodotto -> prodotto.getId() + "@" + prodotto.getName() + "@" + prodotto.getCategoria() + "@" + prodotto.getPrezzo()).collect(Collectors.joining("#"));
        ProvaMain filePower = new ProvaMain();
        filePower.salvaProdottiSuDisco(prodottiStringati);


        //esercizio 7
        System.out.println("____________");

        filePower.leggiProdottiDaDisco();
    }

    public void salvaProdottiSuDisco(String str) {
        File file = new File("file.txt");
        try {
            FileUtils.writeStringToFile(file, str, "UTF-8", false);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void leggiProdottiDaDisco() {
        File file = new File("file.txt");
        try {

            String stringaDaFile = FileUtils.readFileToString(file, "UTF-8");


            List<Prodotto> prodottiList = Arrays.stream(stringaDaFile.split("#"))
                    .map(prodottoStringaAsterisco -> {
                        String[] prodottoSeparatoDaChiocciola = prodottoStringaAsterisco.split("@");


                        Long id = Long.parseLong(prodottoSeparatoDaChiocciola[0]);
                        String name = prodottoSeparatoDaChiocciola[1];
                        String categoria = prodottoSeparatoDaChiocciola[2];
                        double prezzo = Double.parseDouble(prodottoSeparatoDaChiocciola[3]);
                        return new Prodotto(id, name, categoria, prezzo);
                    })
                    .collect(Collectors.toList());


            prodottiList.forEach(System.out::println);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}