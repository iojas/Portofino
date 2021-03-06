/*
 * Copyright (C) 2005-2013 ManyDesigns srl.  All rights reserved.
 * http://www.manydesigns.com/
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.manydesigns.elements.forms;

import com.manydesigns.elements.AbstractElementsTest;
import com.manydesigns.elements.fields.search.SelectSearchField;
import com.manydesigns.elements.fields.search.TextSearchField;
import com.manydesigns.elements.options.DefaultSelectionProvider;
import com.manydesigns.elements.xml.XhtmlBuffer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.StringWriter;

import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;

/*
* @author Paolo Predonzani     - paolo.predonzani@manydesigns.com
* @author Angelo Lupo          - angelo.lupo@manydesigns.com
* @author Giampiero Granatella - giampiero.granatella@manydesigns.com
* @author Alessio Stalla       - alessio.stalla@manydesigns.com
*/
@Test
public class SearchFormTest extends AbstractElementsTest {
    public static final String copyright =
            "Copyright (c) 2005-2013, ManyDesigns srl";

    private SearchForm form;
    private StringWriter writer = null;
    XhtmlBuffer buffer;


    @Override
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        writer = new StringWriter();
        SearchFormBuilder builder =
                new SearchFormBuilder(AnnotatedBean3.class);
        form = builder.build();
        buffer = new XhtmlBuffer(writer);
    }

    public void testForm1(){

        SelectSearchField field = (SelectSearchField) form.get(0);
        //Controllo l'html prodotto
        field.toXhtml(buffer);
        writer.flush();
        String result = writer.toString();
        assertEquals(
                "<div class=\"form-group\"><label for=\"field1\" class=\"control-label\">Field1</label><select id=\"field1\" name=\"field1\" class=\"form-control\"><option value=\"\" selected=\"selected\">-- Select field1 --</option><option value=\"__notset__\">-- Not set --</option><option value=\"1\">a</option><option value=\"2\">b</option></select></div>",
                result);

    }

    //Lettura corretta da request
    public void testForm2(){
        SelectSearchField field = (SelectSearchField) form.get(0);
        req.setParameter("field1", "1");
        field.readFromRequest(req);
        Object[] value = (Object[]) field.getSelectionModel().getValue(0);
        assertEquals("1", (String) value[0]);
        field.toXhtml(buffer);
        String result = writer.toString();
        assertEquals(
                "<div class=\"form-group\"><label for=\"field1\" class=\"control-label\">Field1</label><select id=\"field1\" name=\"field1\" class=\"form-control\"><option value=\"\">-- Select field1 --</option><option value=\"__notset__\">-- Not set --</option><option value=\"1\" selected=\"selected\">a</option><option value=\"2\">b</option></select></div>",
                result);
        StringBuilder sb = new StringBuilder();
        field.toSearchString(sb, "UTF-8");
        result = sb.toString();
        assertEquals("field1=1", result);
    }

    public void testReadFromRequestWithEscape(){
        TextSearchField field = (TextSearchField) form.get(1);
        req.setParameter("field2", "a=b");
        field.readFromRequest(req);
        assertEquals("a=b", field.getValue());
        StringBuilder sb = new StringBuilder();
        field.toSearchString(sb, "UTF-8");
        String result = sb.toString();
        assertEquals("field2=a%3Db", result);
    }

    //Lettura multipla da request con checkbox
    public void testForm2a(){

        SelectSearchField field = (SelectSearchField) form.get(4);
        String[] field5Values = {"1", "2"};
        req.setParameter("field5", field5Values);
        field.readFromRequest(req);
        Object[] value = (Object[]) field.getSelectionModel().getValue(0);
        assertEquals("1", (String) value[0]);
        field.toXhtml(buffer);
        String result = writer.toString();
        assertEquals(
                "<div class=\"form-group\"><label for=\"field5\" class=\"control-label\">Field5</label><div class=\"form-control checkbox\"><input id=\"field5_0\" type=\"checkbox\" name=\"field5\" value=\"1\" checked=\"checked\" /><label for=\"field5_0\">a</label></div><div class=\"form-control checkbox\"><input id=\"field5_1\" type=\"checkbox\" name=\"field5\" value=\"2\" checked=\"checked\" /><label for=\"field5_1\">b</label></div></div>",
                result);
        StringBuilder sb = new StringBuilder();
        field.toSearchString(sb, "UTF-8");
        result = sb.toString();
        assertEquals("field5=1,field5=2", result);
    }

    //Lettura multipla da request con multipleselect
    public void testForm2b(){

        SelectSearchField field = (SelectSearchField) form.get(5);
        String[] field6Values = {"1", "2"};
        req.setParameter("field6", field6Values);
        field.readFromRequest(req);
        Object[] value = (Object[]) field.getSelectionModel().getValue(0);
        assertEquals("1", (String) value[0]);
        field.toXhtml(buffer);
        String result = writer.toString();
        assertEquals(
                "<div class=\"form-group\"><label for=\"field6\" class=\"control-label\">Field6</label><div class=\"form-control multiple-select\"><select id=\"field6\" name=\"field6\" multiple=\"multiple\" size=\"5\"><option value=\"1\" selected=\"selected\">a</option><option value=\"2\" selected=\"selected\">b</option></select></div></div>",
                result);
        StringBuilder sb = new StringBuilder();
        field.toSearchString(sb, "UTF-8");
        result = sb.toString();
        assertEquals("field6=1,field6=2", result);
    }

    //Lettura dato non esitente da request
    public void testForm3(){

        SelectSearchField field = (SelectSearchField) form.get(0);
        req.setParameter("field1", "3");
        field.readFromRequest(req);
        field.toXhtml(buffer);
        String result = writer.toString();

        //Il dato non è nel modello
        assertNull(field.getSelectionModel().getValue(0));

        //Devo avere il campo vuoto selezionato
        assertEquals(
                "<div class=\"form-group\"><label for=\"field1\" class=\"control-label\">Field1</label><select id=\"field1\" name=\"field1\" class=\"form-control\"><option value=\"\" selected=\"selected\">-- Select field1 --</option><option value=\"__notset__\">-- Not set --</option><option value=\"1\">a</option><option value=\"2\">b</option></select></div>",
                result);

    }

    //Lettura da request vuota
    public void testForm4(){

        SelectSearchField field = (SelectSearchField) form.get(0);

        field.readFromRequest(req);
        field.toXhtml(buffer);
        String result = writer.toString();

        //Il dato non è nel modello
        assertNull(field.getSelectionModel().getValue(0));

        //Devo avere il campo vuoto selezionato
        assertEquals(
                "<div class=\"form-group\"><label for=\"field1\" class=\"control-label\">Field1</label><select id=\"field1\" name=\"field1\" class=\"form-control\"><option value=\"\" selected=\"selected\">-- Select field1 --</option><option value=\"__notset__\">-- Not set --</option><option value=\"1\">a</option><option value=\"2\">b</option></select></div>",
                result);


    }

    //testo il form builder aggiungendo un selection provider su field2
    public void testForm5(){
        DefaultSelectionProvider provider = new DefaultSelectionProvider("provider");
        provider.appendRow("v1", "ll", true);
        provider.appendRow("v2", "l2", true);
        provider.appendRow("v3", "l3", true);

        SearchFormBuilder builder =
            new SearchFormBuilder(AnnotatedBean3.class);
        builder.configSelectionProvider(provider, "field2");
        form = builder.build();
        SelectSearchField field = (SelectSearchField) form.get(1);

        //Controllo l'html prodotto
        field.toXhtml(buffer);
        writer.flush();
        String result = writer.toString();
        assertEquals(
                "<div class=\"form-group\"><label for=\"field2\" class=\"control-label\">Field2</label><select id=\"field2\" name=\"field2\" class=\"form-control\"><option value=\"\" selected=\"selected\">-- Select field2 --</option><option value=\"__notset__\">-- Not set --</option><option value=\"v1\">ll</option><option value=\"v2\">l2</option><option value=\"v3\">l3</option></select></div>",
                result);
    }

    //**************************************************************************
    // test DISPLAYMODE
    //**************************************************************************
    public void testForm6(){
        SelectSearchField field = (SelectSearchField) form.get(2);
        //Controllo l'html prodotto
        field.toXhtml(buffer);
        writer.flush();
        String result = writer.toString();
        assertEquals("<div class=\"form-group\"><label for=\"field3\" class=\"control-label\">Field3</label><input type=\"hidden\" id=\"field3\" name=\"field3\" /><input id=\"field3_autocomplete\" type=\"text\" name=\"field3_autocomplete\" class=\"form-control\" /><script type=\"text/javascript\">setupAutocomplete('#field3_autocomplete', 'field3', 0, 'jsonAutocompleteSearchOptions', '#field3');</script></div>",
            result);
    }
    public void testForm7(){
        SelectSearchField field = (SelectSearchField) form.get(3);
        //Controllo l'html prodotto
        field.toXhtml(buffer);
        writer.flush();
        String result = writer.toString();
        assertEquals(
                "<div class=\"form-group\"><label for=\"field4\" class=\"control-label\">Field4</label><div class=\"form-control radio\"><input type=\"radio\" id=\"field4_0\" name=\"field4\" value=\"\" checked=\"checked\" /><label class=\"radio\" for=\"field4_0\">Not filtered</label><input type=\"radio\" id=\"field4_1\" name=\"field4\" value=\"__notset__\" /><label class=\"radio\" for=\"field4_1\">(not set)</label><input type=\"radio\" id=\"field4_2\" name=\"field4\" value=\"1\" /><label class=\"radio\" for=\"field4_2\">a</label><input type=\"radio\" id=\"field4_3\" name=\"field4\" value=\"2\" /><label class=\"radio\" for=\"field4_3\">b</label></div></div>",
                result);
    }
    public void testForm8(){

        SelectSearchField field = (SelectSearchField) form.get(4);
        //Controllo l'html prodotto
        field.toXhtml(buffer);
        writer.flush();
        String result = writer.toString();
        assertEquals(
                "<div class=\"form-group\"><label for=\"field5\" class=\"control-label\">Field5</label><div class=\"form-control checkbox\"><input id=\"field5_0\" type=\"checkbox\" name=\"field5\" value=\"1\" /><label for=\"field5_0\">a</label></div><div class=\"form-control checkbox\"><input id=\"field5_1\" type=\"checkbox\" name=\"field5\" value=\"2\" /><label for=\"field5_1\">b</label></div></div>",
                result);

    }
    public void testForm9(){

        SelectSearchField field = (SelectSearchField) form.get(5);
        //Controllo l'html prodotto
        field.toXhtml(buffer);
        writer.flush();
        String result = writer.toString();
        assertEquals(
                "<div class=\"form-group\"><label for=\"field6\" class=\"control-label\">Field6</label><div class=\"form-control multiple-select\"><select id=\"field6\" name=\"field6\" multiple=\"multiple\" size=\"5\"><option value=\"1\">a</option><option value=\"2\">b</option></select></div></div>",
                result);
    }

    @Override
    @AfterMethod
    public void tearDown() throws Exception {
        super.tearDown();
        if (writer!=null){
            writer.close();
        }
    }

    //testo il form builder aggiungendo un selection provider a cascata su field2
    public void testForm10(){
        DefaultSelectionProvider provider = new DefaultSelectionProvider("provider");
        provider.appendOption("v1", "ll", true);
        provider.appendOption("v2", "l2", true);
        provider.appendOption("v3", "l3", true);

        SearchFormBuilder builder =
            new SearchFormBuilder(AnnotatedBean3.class);
        builder.configSelectionProvider(provider, "field2");
        form = builder.build();
        SelectSearchField field = (SelectSearchField) form.get(1);

        //Controllo l'html prodotto
        field.toXhtml(buffer);
        writer.flush();
        String result = writer.toString();
        assertEquals(
                "<div class=\"form-group\"><label for=\"field2\" class=\"control-label\">Field2</label><select id=\"field2\" name=\"field2\" class=\"form-control\"><option value=\"\" selected=\"selected\">-- Select field2 --</option><option value=\"__notset__\">-- Not set --</option><option value=\"v1\">ll</option><option value=\"v2\">l2</option><option value=\"v3\">l3</option></select></div>",
                result);
    }
}
