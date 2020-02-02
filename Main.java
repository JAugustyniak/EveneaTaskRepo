import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        //reading file
        Reader in = new FileReader("happiness.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

        //creating maps
        Map<String, Integer> positiveAffectMap = new HashMap<>();
        Map<String, Integer> freedomMap = new HashMap<>();
        Map<String, Integer> corruptionMap = new HashMap<>();

        for (CSVRecord r : records) {
            //System.out.println(r.get(0));
            try {
                //reading values from file and putting them to the map
                Integer positiveValue = Integer.parseInt(r.get("Positive affect"));
                positiveAffectMap.put(r.get(0), positiveValue);

                Integer freedomValue = Integer.parseInt(r.get("Freedom"));
                freedomMap.put(r.get(0), freedomValue);

                Integer corruptionValue = Integer.parseInt(r.get("Corruption"));
                corruptionMap.put(r.get(0), corruptionValue);
            }
            catch (NumberFormatException e) {
                System.out.println("Błędna wartość");
            }

        }

        //sorting positive affect map
        LinkedHashMap<String, Integer> sortedPositiveAffectMap = new LinkedHashMap<>();
        positiveAffectMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedPositiveAffectMap.put(x.getKey(), x.getValue()));
        //System.out.println("Positive affect: " + sortedPositiveAffectMap);

        //sorting freedom map
        LinkedHashMap<String, Integer> sortedFreedomMap = new LinkedHashMap<>();
        freedomMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(5)
                .forEachOrdered(x -> sortedFreedomMap.put(x.getKey(), x.getValue()));
        //System.out.println("Freedom: " + sortedFreedomMap);

        //sorting corruption map
        LinkedHashMap<String, Integer> sortedCorruptionMap = new LinkedHashMap<>();
        corruptionMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(5)
                .forEachOrdered(x -> sortedCorruptionMap.put(x.getKey(), x.getValue()));
        //System.out.println("Corruption: " + sortedCorruptionMap);

        //filter countries (more happiness than Polish)
        Integer positiveAffectInPoland = positiveAffectMap.get("Poland");
        sortedPositiveAffectMap.entrySet().removeIf( e -> e.getValue().compareTo(Integer.valueOf(positiveAffectInPoland)) <= 0);
        //System.out.println(sortedPositiveAffectMap.size());

        //writing to file "result.txt"
        PrintWriter printWriter = new PrintWriter("results.txt");

        printWriter.print("5 państw, w których jest największa wolność:" + "\n");
        printWriter.print(sortedFreedomMap + "\n" + "\n");

        printWriter.print("5 państw, w których jest najmniejsza korupcja:" + "\n");
        printWriter.print(sortedCorruptionMap + "\n" + "\n");

        printWriter.print("wszystkie kraje, w których ludzie są szczęśliwsi niż ludzie mieszkający w Polsce: " + "\n");
        printWriter.print(sortedPositiveAffectMap);

        printWriter.close();
    }
}
