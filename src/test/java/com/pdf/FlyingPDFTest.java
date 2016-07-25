/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdf;

import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 *
 * @author ax.finamore
 */
public class FlyingPDFTest {

    public FlyingPDFTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() throws IOException, DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        String yourXhtmlContentAsString = "<html><head></head><body><h1>Hi</h1></body></html>";

        renderer.setDocumentFromString(yourXhtmlContentAsString);
        renderer.layout();

        String fileNameWithPath = "C:\\Users\\a.finamore\\PDF-FromHtmlString.pdf";
        FileOutputStream fos = new FileOutputStream(fileNameWithPath);

        renderer.createPDF(fos);

        fos.close();

        System.out.println("File 2: '" + fileNameWithPath + "' created.");
    }
}
