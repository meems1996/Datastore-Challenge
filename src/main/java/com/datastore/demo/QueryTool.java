package com.datastore.demo;

import java.io.IOException;

public class QueryTool {

    public static void main(String[] args) {

    }

    /**
     * Need to pass it a string of fields it will search for
     */
    public static void select (String[] fields) {

        /**
         * Select is ran after the command line receives the -s flag as an argument.
         * The flag must be followed by at least one field to search through.
         *
         * Take the string array and divide it.
         * If TITLE is contained then show that
         * If something else plus title is contained show those results.
         */

    }

    public static void orderBy (String[] fields) {

        /**
         * Order is ran after the command line recieves the -o flag.
         * The flag must follow after a select has been chosen and is present in the command line query.
         *
         * Take the string array and divide each word
         * If a field is a number or a double, then order it from smallest to largest
         * If a field is a String, then order it alphabetically
         * If a field is a date, then order it from earliest to oldest date
         */

    }

    public static void filter (String[] fields) {
        /**
         * Filter is ran after the command line has received the -f flag.
         * The flag must follow after at least a -s has been ran in the command line query passed by the user.
         *
         * The string array of fields must be passed after the flag e.g., -f DATE=2014-04-01
         * if the field that is being filtered is numerical or date, then it must be followed by an operator (=, <, >, !=).
         *      If it's not, then give an error message saying "Invalid operation. Must pass an operator."
         * If the field that is being filtered is a String, then it must be followed by = sign and then in parenthesis a
         * string that will be searched for, e.g., -f TITLE="Harry Potter".
         */
    }
}

//