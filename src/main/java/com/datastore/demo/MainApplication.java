package com.datastore.demo;

import com.google.common.base.Splitter;
import com.opencsv.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.cli.*;

public class MainApplication {

    // Set the cmd, and header as globals to be used across the program
    final static Options options  = new Options();
    private static CommandLine cmd;
    private static String[] header = ("STB|TITLE|PROVIDER|DATE|REV|VIEW_TIME").split("\\|");

    public static void main(String[] args) throws IOException {
        queryTool(args);
    }

    /**
     * Method sets up the Apache Commons CLI for parsing command line arguments
     * @param args
     * @throws IOException
     */
    private static void queryTool(String[] args) throws IOException{
        // ---------- Read file args option ----------
        Option readFile = new Option("r", "read", true, "Read psv file to import into datastore.");
        readFile.isRequired();

        // ---------- Select fields args option ----------
        Option selectFields = new Option("s", "select", true, "Select fields to search");
        selectFields.isRequired();

        // ----------- Order fields args option -----------
        Option orderFields = new Option("o", "order", true, "Order fields");

        // ---------- Filter fields args option ----------
        Option filterFields = new Option("f", "filter", true, "Filter fields");


        options.addOption(readFile).addOption(selectFields).addOption(orderFields).addOption(filterFields);

        CommandLineParser parser = new DefaultParser();

        try {
            // parse the command line arguments here
            cmd = parser.parse(options, args);

            // if String is fileName
            String filePath = "./dataset.psv";

            // if command line has argument r
            if (cmd.hasOption("r")) {
                String valueRead = cmd.getOptionValue("r");
                List<String> valuesList = new ArrayList<>(Splitter.on(",").splitToList(valueRead));
                valuesList.add(filePath);
                if (valuesList.contains(filePath)) {
                    readAndParseFile(filePath);
                    System.out.println("this works");
                }
            }

            // if command line has args select and order
            if (cmd.getOptions().length == 2) {
                List<List<Object>> read = new ArrayList<>();
                if (cmd.hasOption("s") && cmd.hasOption("o")) {
                    String valueRead = cmd.getOptionValue("s");
                    List<String> valuesList = new ArrayList<>(Splitter.on(",").splitToList(valueRead));
                    List<DatastoreEntity> lists = selectData(args);
                    orderData(args, lists);
                    SelectDesiredFields(read, valuesList, lists);
                }
            }

            // if command line has args select and filter
            if (cmd.getOptions().length == 2) {
                if (cmd.hasOption("s") && cmd.hasOption("f")) {
                    List<List<Object>> readFilter = new ArrayList<>();
                    String valueRead = cmd.getOptionValue("s");
                    List<String> valuesListFilter = new ArrayList<>(Splitter.on(",").splitToList(valueRead));
                    List<DatastoreEntity> lists = selectData(args);
                    List<DatastoreEntity> filterList = filterData(args, lists);
                    SelectDesiredFields(readFilter, valuesListFilter, filterList);
                }
            }

        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }

    }

    /**
     * Select which fields to show the user after you filter or order
     * @param readFilter
     * @param valuesListFilter
     * @param filterList
     */
    private static void SelectDesiredFields(List<List<Object>> readFilter, List<String> valuesListFilter, List<DatastoreEntity> filterList) {
        for (DatastoreEntity l : filterList) {
            List<Object> readString = new ArrayList<>();
            if (valuesListFilter.contains("stb")) {readString.add(l.getStb());}
            if (valuesListFilter.contains("title")) {readString.add(l.getTitle());}
            if (valuesListFilter.contains("provider")) {readString.add(l.getProvider());}
            if (valuesListFilter.contains("date")) {readString.add(l.getDate());}
            if (valuesListFilter.contains("rev")) {readString.add(l.getRev());}
            if (valuesListFilter.contains("time")) {readString.add(l.getViewTime());}
            readFilter.add(readString);
        }
        for (Object l : readFilter) {
            System.out.println(l);
        }
    }

    /**
     * Select data from the Datastore
     * @param args
     * @return DatastoreEntity
     * @throws IOException
     */
    private static List<DatastoreEntity> selectData(String[] args) throws IOException{
        String[] line;
        List<DatastoreEntity> listOfReturns = new ArrayList<>();

        // Check if file doesnt exist
        File dir = new File("./datastore");
        if (!dir.isDirectory()) {
            System.out.println(listOfReturns);
            return listOfReturns;
        }

        // Check if file exists and then create a reader to read from the csv files
        // and pass each line through the POJO class
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile()) {
                Path path = Paths.get(file.toString());
                CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
                try (BufferedReader br = Files.newBufferedReader(path,
                        StandardCharsets.UTF_8);
                     CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser)
                             //skip header
                             .withSkipLines(1)
                             .build()) {
                    while ((line = reader.readNext()) != null) {
                        DatastoreEntity dt = dataEntity(line);
                        listOfReturns.add(dt);
                    }
                }
            }
        }
        return listOfReturns;
    }

    /**
     * Filter data selected from the Datastore
     * @param args
     * @param lists
     * @return a List of DatastoreEntities
     */
    private static  List<DatastoreEntity> filterData(String[] args, List<DatastoreEntity> lists) {
        List<DatastoreEntity> listOfReturns = new ArrayList<>();
        if (cmd.hasOption("f")) {
            String valuesRead = cmd.getOptionValue("f");
            List<String> valueList = new ArrayList<>(Splitter.on("=").splitToList(valuesRead));
            Object keyword = " ";

            // Check for different cases you can get for the first part of the argument following
            // the -f flag. e.g., -f stb=stb1 where valueList.get(0) would be stb
            // and valueList.get(1) would be the argument after the = separator.
            for (DatastoreEntity l : lists) {
                if (valueList.get(0).equalsIgnoreCase("stb")) {
                    keyword = l.getStb();
                }

                if (valueList.get(0).equalsIgnoreCase("title")) {
                    keyword = l.getTitle();
                }

                if (valueList.get(0).equalsIgnoreCase("provider")) {
                    keyword = l.getProvider();
                }

                if (valueList.get(0).equalsIgnoreCase("date")) {
                    keyword = l.getDate();
                }

                if (valueList.get(0).equalsIgnoreCase("rev")) {
                    keyword = l.getRev();
                }

                if (valueList.get(0).equalsIgnoreCase("view_time")) {
                    keyword = l.getViewTime();
                }
                if (valueList.get(1).contains(keyword.toString())) {
                    listOfReturns.add(l);
                }

            }
        }
        return listOfReturns;
    }

    /**
     * Order data method. Allows for ordering of a single field as well as multiple fields.
     * @param args
     * @param lists
     */
    private static void orderData(String[] args, List<DatastoreEntity> lists) {

        if (cmd.hasOption("o")) {
            //split the arguments
            String valueRead = cmd.getOptionValue("o");
            List<String> valueList = new ArrayList<>(Splitter.on(",").splitToList(valueRead));

            Map<String, Comparator<DatastoreEntity>> map = new HashMap<>();
            map.put("stb", new STBComparator());
            map.put("title", new TitleComparator());
            map.put("provider", new ProviderComparator());
            map.put("date", new DateComparator());
            map.put("rev", new RevComparator());
            map.put("time", new ViewTimeComparator());

            // Loop through orderBy and add each comparator in the correct order
            List<Comparator<DatastoreEntity>> comparators = valueList.stream()
                    .map(map::get)
                    .collect(Collectors.toList());

            // Chain comparators and sort the list
            lists.sort(new ChainedComparator(comparators));
        }
    }

    /**
     * Read PSV file and import to Datastore where it saves data in CSV files
     * @param myFilePath
     * @throws IOException
     */
    private static void readAndParseFile(String myFilePath) throws IOException {
        DatastoreEntity dt;
        //  ---------- Import data from the file ----------
        final String filePath = myFilePath;
        Path myPath = Paths.get(myFilePath);
        String[] datastoreLine;

        // Parse the psv file to be imported into the Datastore
        CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8);
             CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser)
                     //skip header
                     .withSkipLines(1)
                     .build()) {
            // ---------- Read all values from the psv file to the Datastore ----------
            String[] nextLine;
            CSVWriter writer;

            // Parse the PSV file until you run out of next lines.
            // And add lines to the datastore here
            while ((nextLine = reader.readNext()) != null) {
                dt = dataEntity(nextLine);
                String[] str = dt.getDate().split("-");
                String year = str[0];
                String month = str[1];

                //set up a Composite Key
                List<Object> compositeKey = new ArrayList<>();
                compositeKey.add(dt.getStb());
                compositeKey.add(dt.getTitle());
                compositeKey.add(dt.getDate());

                // if a file with that name doesnt exist, make it
                if (!doesDatastoreExist(year, month)) {
                    writer = new CSVWriter(new FileWriter("./datastore/" + year + "-" + month +".csv", true));
                    File p = new File ("./datastore/" + year + "-" + month +".csv");
                    if (isDatastoreEmpty(p)) {
                        writer.writeNext(header);
                        writer.writeNext(nextLine);
                        // if datastore has a file with the name needed then write the line there
                    } else if (!isDatastoreEmpty(p)) {
                       //read the csv files
                        CSVReader read = new CSVReader(new FileReader(p));

                        //iterates over csv file currently being considered for the line until you find null
                        while ((datastoreLine = read.readNext()) != null) {
                            //set up next composite key so that you can get the composite key of the
                            // line we are comparing to the lines in the csv files
                            List<Object> fileCompositeKey = new ArrayList<>();
                            // add stb to composite key
                            compositeKey.add(datastoreLine[0]);
                            // add title to composite key
                            compositeKey.add(datastoreLine[1]);
                            // add date to composite key
                            compositeKey.add(datastoreLine[3]);

                            // compare them to check for equality.If they are equal at their composite key
                            // then break and go to next line.
                             if (dt.getTitle().matches(datastoreLine[1])) {
                                break;
                            }
                        }
                        if (datastoreLine == null) {
                            writer.writeNext(nextLine);
                        }
                    }
                    writer.close();

                }
            }
        }
        System.out.println("Succesfully stored data into datastore");
    }

    /**
     * Checks if Datastore exists
     * @return boolean exists
     */
    private static boolean doesDatastoreExist(String y, String m) {
        // initially it's false
        boolean exists;
        final String pathFile = "./datastore/" + y + m + ".csv";
        File datastoreFile = new File(pathFile);

        // checks if datastore is a file
        exists = datastoreFile.isFile();

        return exists;
    }

    /**
     * Check if Datastore file is empty
     * @return boolean isEmpty
     */
    private static boolean isDatastoreEmpty(File datastorePath) throws IOException {
        boolean isEmpty;
        BufferedReader reader = new BufferedReader(new FileReader(datastorePath));
        String firstLine;

        // if datastore exists, check if it's empty
            if ((firstLine = reader.readLine()) == null ) {
                isEmpty = true;
            } else {
                isEmpty = false;
            }
        return isEmpty;
    }

    /**
     * DatastoreEntity method
     * @param movieEntry
     * @return
     */
    private static DatastoreEntity dataEntity(String[] movieEntry){

        String pattern = movieEntry[3];
        SimpleDateFormat formatSimple = new SimpleDateFormat(pattern);
        String stb = movieEntry[0];
        String title = movieEntry[1];
        String provider = movieEntry[2];
        String date = formatSimple.format(new Date());
        double rev = Double.parseDouble(movieEntry[4]);
        String view_time = movieEntry[5];

        return new DatastoreEntity(stb, title, provider,  date, rev,  view_time);
    }
}

