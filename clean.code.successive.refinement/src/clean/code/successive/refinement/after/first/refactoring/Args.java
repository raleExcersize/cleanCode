package clean.code.successive.refinement.after.first.refactoring;

import clean.code.successive.refinement.error.*;
import java.util.*;

public class Args {

    private final String schema;
    private final Map<Character, ArgumentMarshaler> marshalers = new HashMap<Character, ArgumentMarshaler>();
    private final Set<Character> argsFound = new HashSet<Character>();
    private Iterator<String> currentArgument;
    private final List<String> argsList;

    public Args(String schema, String[] args) throws ArgsException {
        this.schema = schema;
        this.argsList = Arrays.asList(args);
        parse();
    }

    private void parse() throws ArgsException {
        parseSchema();
        parseArguments();
    }

    private boolean parseSchema() throws ArgsException {
        for (String element : schema.split(",")) {
            if (element.length() > 0) {
                parseSchemaElement(element.trim());
            }
        }
        return true;
    }

    private void parseSchemaElement(String element) throws ArgsException {
        char elementId = element.charAt(0);
        String elementTail = element.substring(1);
        validateSchemaElementId(elementId);
        if (elementTail.length() == 0) {
            marshalers.put(elementId, new BooleanArgumentMarshaler());
        } else if (elementTail.equals("*")) {
            marshalers.put(elementId, new StringArgumentMarshaler());
        } else if (elementTail.equals("#")) {
            marshalers.put(elementId, new IntegerArgumentMarshaler());
        } else if (elementTail.equals("##")) {
            marshalers.put(elementId, new DoubleArgumentMarshaler());
        } else {
            throw new ArgsException(elementId, ArgsException.ErrorCode.INVALID_ARGUMENT_FORMAT);
        }
    }

    private void validateSchemaElementId(char elementId) throws ArgsException {
        if (!Character.isLetter(elementId)) {
            throw new ArgsException(elementId, ArgsException.ErrorCode.INVALID_ARGUMENT_NAME);
        }
    }

    private boolean parseArguments() throws ArgsException {
        for (currentArgument = argsList.iterator(); currentArgument.hasNext();) {
            String arg = currentArgument.next();
            parseArgument(arg);
        }
        return true;
    }

    private void parseArgument(String arg) throws ArgsException {
        if (arg.startsWith("-")) {
            parseElements(arg);
        }
    }

    private void parseElements(String arg) throws ArgsException {
        for (int i = 1; i < arg.length(); i++) {
            parseElement(arg.charAt(i));
        }
    }

    private void parseElement(char argChar) throws ArgsException {
        if (setArgument(argChar)) {
            argsFound.add(argChar);
        } else {
            throw new ArgsException(argChar, ArgsException.ErrorCode.UNEXPECTED_ARGUMENT);
        }
    }

    private boolean setArgument(char argChar) throws ArgsException {
        ArgumentMarshaler m = marshalers.get(argChar);
        if (m == null) {
            return false;
        }
        try {
            m.set(currentArgument);
        } catch (ArgsException e) {
            e.errorArgumentId(argChar);
            throw e;
        }
        return true;
    }

    public int cardinality() {
        return argsFound.size();
    }

    public String usage() {
        if (schema.length() > 0) {
            return "-[" + schema + "]";
        } else {
            return "";
        }
    }

    public boolean getBoolean(char arg) {
        Args.ArgumentMarshaler am = marshalers.get(arg);
        boolean b = false;
        try {
            b = am != null && (Boolean) am.get();
        } catch (ClassCastException e) {
            b = false;
        }
        return b;
    }

    public String getString(char arg) {
        Args.ArgumentMarshaler am = marshalers.get(arg);
        try {
            return am == null ? "" : (String) am.get();
        } catch (ClassCastException e) {
            return "";
        }
    }

    public int getInt(char arg) {
        Args.ArgumentMarshaler am = marshalers.get(arg);
        try {
            return am == null ? 0 : (Integer) am.get();
        } catch (Exception e) {
            return 0;
        }
    }

    public double getDouble(char c) {
        Args.ArgumentMarshaler m = marshalers.get(c);
        return (Double) (m != null ? m.get() : 0.00);
    }

    public boolean has(char arg) {
        return argsFound.contains(arg);
    }

    private interface ArgumentMarshaler {

        public abstract void set(Iterator<String> currentArgument) throws ArgsException;

        public abstract Object get();
    }

    private class BooleanArgumentMarshaler implements ArgumentMarshaler {

        private boolean booleanValue = false;

        public Object get() {
            return booleanValue;
        }

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            booleanValue = true;
        }
    }

    private class StringArgumentMarshaler implements ArgumentMarshaler {

        private String stringValue = "";

        public Object get() {
            return stringValue;
        }

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            try {
                stringValue = currentArgument.next();
            } catch (NoSuchElementException e) {
                throw new ArgsException(ArgsException.ErrorCode.MISSING_STRING);
            }
        }
    }

    private class IntegerArgumentMarshaler implements ArgumentMarshaler {

        private int intValue = 0;

        public Object get() {
            return intValue;
        }

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            String parameter = null;
            try {
                parameter = currentArgument.next();
                intValue = Integer.parseInt(parameter);
            } catch (NoSuchElementException e) {
                throw new ArgsException(ArgsException.ErrorCode.MISSING_INTEGER);
            } catch (NumberFormatException e) {
                throw new ArgsException(parameter, ArgsException.ErrorCode.INVALID_INTEGER);
            }

        }
    }

    private class DoubleArgumentMarshaler implements ArgumentMarshaler {

        double doubleValue = 0.00;

        public void set(Iterator<String> currentArgument) throws ArgsException {
            String parameter = null;
            try {
                parameter = currentArgument.next();
                doubleValue = Double.parseDouble(parameter);
            } catch (NoSuchElementException e) {
                throw new ArgsException(ArgsException.ErrorCode.MISSING_DOUBLE);
            } catch (NumberFormatException e) {
                throw new ArgsException(parameter, ArgsException.ErrorCode.INVALID_DOUBLE);
            }
        }

        public Object get() {
            return doubleValue;
        }

    }

}
