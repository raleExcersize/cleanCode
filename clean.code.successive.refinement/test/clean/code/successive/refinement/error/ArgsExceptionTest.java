/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clean.code.successive.refinement.error;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nevena
 */
public class ArgsExceptionTest {

    public ArgsExceptionTest() {
    }

    @Test
    public void testUnexpectedMessage() throws Exception {
        ArgsException e = new ArgsException('x', null, ArgsException.ErrorCode.UNEXPECTED_ARGUMENT);
        assertEquals("Argument -x unexpected.", e.errorMessage());
    }

    @Test
    public void testMissingStringMessage() throws Exception {
        ArgsException e = new ArgsException('x', null, ArgsException.ErrorCode.MISSING_STRING);
        assertEquals("Could not find string parameter for -x.", e.errorMessage());
    }

    @Test
    public void testInvalidIntegerMessage() throws Exception {
        ArgsException e
                = new ArgsException('x', "Forty two", ArgsException.ErrorCode.INVALID_INTEGER);
        assertEquals("Argument -x expects an integer but was 'Forty two'.",
                e.errorMessage());
    }

    @Test
    public void testMissingIntegerMessage() throws Exception {
        ArgsException e
                = new ArgsException('x', null, ArgsException.ErrorCode.MISSING_INTEGER);
        assertEquals("Could not find integer parameter for -x.", e.errorMessage());
    }

    @Test
    public void testInvalidDoubleMessage() throws Exception {
        ArgsException e = new ArgsException('x', "Forty two", ArgsException.ErrorCode.INVALID_DOUBLE);
        assertEquals("Argument -x expects a double but was 'Forty two'.",
                e.errorMessage());
    }

    @Test
    public void testMissingDoubleMessage() throws Exception {
        ArgsException e = new ArgsException('x', null, ArgsException.ErrorCode.MISSING_DOUBLE);
        assertEquals("Could not find double parameter for -x.", e.errorMessage());
    }

}
