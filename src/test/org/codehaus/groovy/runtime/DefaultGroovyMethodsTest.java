/*
 $Id$

 Copyright 2003 (C) James Strachan and Bob Mcwhirter. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "groovy" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "groovy"
    nor may "groovy" appear in their names without prior written
    permission of The Codehaus. "groovy" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://groovy.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import groovy.util.GroovyTestCase;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @author Marc Guillemot
 * @author Brad Long
 */
public class DefaultGroovyMethodsTest extends GroovyTestCase {

    public void testPrint() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("bob", "drools");
        map.put("james", "geronimo");
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list.add(map);
        assertEquals("[[james:geronimo, bob:drools]]", InvokerHelper.toString(list));
    }

    public void testIncrementString() throws Exception {
        String original = "z";
        String answer = DefaultGroovyMethods.next(original);

        assertEquals("{", answer);
        assertTrue(answer.compareTo(original) > 0);
    }

    public void testDecrementString() throws Exception {
        String original = "a";
        String answer = DefaultGroovyMethods.previous(original);

        assertEquals("`", answer);
        assertTrue(ScriptBytecodeAdapter.compareLessThan(answer, original));
    }
    
    public void testFloatRounding() throws Exception {
        Float f = 1000.123456f;

        assertEquals(DefaultGroovyMethods.round(f), 1000);
        assertEquals(DefaultGroovyMethods.round(f, 0), 1000.0f);
        assertEquals(DefaultGroovyMethods.round(f, 1), 1000.1f);
        assertEquals(DefaultGroovyMethods.round(f, 2), 1000.12f);
        assertEquals(DefaultGroovyMethods.round(f, 3), 1000.123f);
        assertEquals(DefaultGroovyMethods.round(f, 4), 1000.1235f);
        assertEquals(DefaultGroovyMethods.round(f, 5), 1000.12346f);
        assertEquals(DefaultGroovyMethods.round(f, 6), 1000.123456f);
    }

    public void testDoubleRounding() throws Exception {
        Double d = 1000.123456;

        assertEquals(DefaultGroovyMethods.round(d), 1000L);
        assertEquals(DefaultGroovyMethods.round(d, 0), 1000.0);
        assertEquals(DefaultGroovyMethods.round(d, 1), 1000.1);
        assertEquals(DefaultGroovyMethods.round(d, 2), 1000.12);
        assertEquals(DefaultGroovyMethods.round(d, 3), 1000.123);
        assertEquals(DefaultGroovyMethods.round(d, 4), 1000.1235);
        assertEquals(DefaultGroovyMethods.round(d, 5), 1000.12346);
        assertEquals(DefaultGroovyMethods.round(d, 6), 1000.123456);
    }

    public void testFloatTruncate() throws Exception {
        Float f = 1000.123456f;

        assertEquals(DefaultGroovyMethods.trunc(f), 1000.0f);
        assertEquals(DefaultGroovyMethods.trunc(f, 0), 1000.0f);
        assertEquals(DefaultGroovyMethods.trunc(f, 1), 1000.1f);
        assertEquals(DefaultGroovyMethods.trunc(f, 2), 1000.12f);
        assertEquals(DefaultGroovyMethods.trunc(f, 3), 1000.123f);
        assertEquals(DefaultGroovyMethods.trunc(f, 4), 1000.1234f);
        assertEquals(DefaultGroovyMethods.trunc(f, 5), 1000.12345f);
        assertEquals(DefaultGroovyMethods.trunc(f, 6), 1000.123456f);
    }

    public void testDoubleTruncate() throws Exception {
        Double d = 1000.123456;

        assertEquals(DefaultGroovyMethods.trunc(d), 1000.0);
        assertEquals(DefaultGroovyMethods.trunc(d, 0), 1000.0);
        assertEquals(DefaultGroovyMethods.trunc(d, 1), 1000.1);
        assertEquals(DefaultGroovyMethods.trunc(d, 2), 1000.12);
        assertEquals(DefaultGroovyMethods.trunc(d, 3), 1000.123);
        assertEquals(DefaultGroovyMethods.trunc(d, 4), 1000.1234);
        assertEquals(DefaultGroovyMethods.trunc(d, 5), 1000.12345);
        assertEquals(DefaultGroovyMethods.trunc(d, 6), 1000.123456);
    }

    public void testToMethods() throws Exception {
        Number n = 7L;

        assertEquals(DefaultGroovyMethods.toInteger("1"), new Integer(1));
        assertEquals(DefaultGroovyMethods.toInteger(n), new Integer(7));
        assertEquals(DefaultGroovyMethods.toLong("1"), new Long(1));
        assertEquals(DefaultGroovyMethods.toLong(n), new Long(7));

        assertEquals(DefaultGroovyMethods.toFloat("1"), new Float(1));
        assertEquals(DefaultGroovyMethods.toFloat(n), new Float(7));
        assertEquals(DefaultGroovyMethods.toDouble("1"), new Double(1));
        assertEquals(DefaultGroovyMethods.toDouble(n), new Double(7));

        assertEquals(DefaultGroovyMethods.toBigInteger("1"), new BigInteger("1"));
        assertEquals(DefaultGroovyMethods.toBigInteger(n), new BigInteger("7"));
        assertEquals(DefaultGroovyMethods.toBigDecimal("1"), new BigDecimal("1"));
        assertEquals(DefaultGroovyMethods.toBigDecimal(n), new BigDecimal("7"));

        // The following is true starting with 1.6 (GROOVY-3171):
        assertEquals(new BigDecimal("0.1"), DefaultGroovyMethods.toBigDecimal(0.1));

        assertEquals(DefaultGroovyMethods.toURL("http://example.org/"), new URL("http://example.org/"));
        assertEquals(DefaultGroovyMethods.toURI("http://example.org/"), new URI("http://example.org/"));

        assertEquals(DefaultGroovyMethods.toBoolean("True"), Boolean.TRUE);
        assertEquals(DefaultGroovyMethods.toBoolean("Y"), Boolean.TRUE);
        assertEquals(DefaultGroovyMethods.toBoolean(" y "), Boolean.TRUE);
        assertEquals(DefaultGroovyMethods.toBoolean("1"), Boolean.TRUE);
        assertEquals(DefaultGroovyMethods.toBoolean("false"), Boolean.FALSE);
        assertEquals(DefaultGroovyMethods.toBoolean("n"), Boolean.FALSE);
        assertEquals(DefaultGroovyMethods.toBoolean("0"), Boolean.FALSE);

        assertEquals(DefaultGroovyMethods.toBoolean(Boolean.FALSE), Boolean.FALSE);
        assertEquals(DefaultGroovyMethods.toBoolean(Boolean.TRUE), Boolean.TRUE);
    }

    public void testIsMethods() throws Exception {
        String intStr = "123";
        String floatStr = "1.23E-1";
        String nonNumberStr = "ONE";

        assertTrue(DefaultGroovyMethods.isInteger(intStr));
        assertFalse(DefaultGroovyMethods.isInteger(floatStr));
        assertFalse(DefaultGroovyMethods.isInteger(nonNumberStr));
        assertTrue(DefaultGroovyMethods.isLong(intStr));
        assertFalse(DefaultGroovyMethods.isLong(floatStr));
        assertFalse(DefaultGroovyMethods.isLong(nonNumberStr));

        assertTrue(DefaultGroovyMethods.isFloat(intStr));
        assertTrue(DefaultGroovyMethods.isFloat(floatStr));
        assertFalse(DefaultGroovyMethods.isFloat(nonNumberStr));
        assertTrue(DefaultGroovyMethods.isDouble(intStr));
        assertTrue(DefaultGroovyMethods.isDouble(floatStr));
        assertFalse(DefaultGroovyMethods.isDouble(nonNumberStr));

        assertTrue(DefaultGroovyMethods.isBigInteger(intStr));
        assertFalse(DefaultGroovyMethods.isBigInteger(floatStr));
        assertFalse(DefaultGroovyMethods.isBigInteger(nonNumberStr));
        assertTrue(DefaultGroovyMethods.isBigDecimal(intStr));
        assertTrue(DefaultGroovyMethods.isBigDecimal(floatStr));
        assertFalse(DefaultGroovyMethods.isBigDecimal(nonNumberStr));
        assertTrue(DefaultGroovyMethods.isNumber(intStr));
        assertTrue(DefaultGroovyMethods.isNumber(floatStr));
        assertFalse(DefaultGroovyMethods.isNumber(nonNumberStr));
    }

    public void testGetBytes() {
        byte[] bytes = {42,45,47,14,10,84};
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        try {
            byte[] answer = DefaultGroovyMethods.getBytes(is);
            assertEquals(bytes.length, answer.length);
            for (int i = 0; i < bytes.length; i++) {
                assertEquals(bytes[i], answer[i]);       
            }
        } catch (IOException e) {
            fail();
        }
    }

    public void testSetBytes() {
        byte[] bytes = {42,45,47,14,10,84};
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            DefaultGroovyMethods.setBytes(os, bytes);
            byte[] answer = os.toByteArray();
            assertEquals(bytes.length, answer.length);
            for (int i = 0; i < bytes.length; i++) {
                assertEquals(bytes[i], answer[i]);
            }
        } catch (IOException e) {
            fail();
        }
    }

    public void testDownto() {
        final int[] count = new int[]{0};
        final Closure closure = new Closure(null) {
            public Object doCall(final Object params) {
                count[0]++;
                return null;
            }
        };

        DefaultGroovyMethods.downto(new BigInteger("1"), new BigDecimal("0"), closure);
        assertEquals(count[0], 2);

        count[0] = 0;

        DefaultGroovyMethods.downto(new BigInteger("1"), new BigDecimal("0.123"), closure);
        assertEquals(count[0], 1);
    }

    public void testBulkCollectionForArrayOperations() {
        List<String> list = new ArrayList<String>();
        assertTrue(DefaultGroovyMethods.addAll(list, "abcd".split("")));
        assertTrue(DefaultGroovyMethods.removeAll(list, "def".split("")));
        assertTrue(DefaultGroovyMethods.retainAll(list, "bcd".split("")));
        List<String> bAndC = Arrays.asList("b", "c");
        assertTrue(DefaultGroovyMethods.containsAll(list, bAndC.toArray()));
        assertEquals(list, bAndC);
        assertTrue(DefaultGroovyMethods.addAll(list, 1, Arrays.asList("a", "s", "i").toArray(new String[3])));
        assertEquals(list, Arrays.asList("b", "a", "s", "i", "c"));
    }
}
