/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alessio.finamore
 */
public class CalcoloKm {

    static public double calcolaSpesa(int capienza_serba,
            double costo_lit,
            int tot_km,
            int autonomia_km
    ) {

        double costo_pieno = capienza_serba * costo_lit;
        int n_pieni = (tot_km / autonomia_km);
        Double costo_tot = n_pieni * costo_pieno;

        return Math.round(costo_tot);

    }

    public CalcoloKm() {
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
    public void hello() {
        int capienza_serba_ncx = 14;
        int autonomia_km_ncx = 350;
        int capienza_serba_xrv = 23;
        int autonomia_km_xrv = 300;
        
        int n_mesi = 1; 
        double costo_lit = 1.650;
        int km_mensili = 350;

        int tot_km = km_mensili * n_mesi;

        double costo_tot_ncx = CalcoloKm.calcolaSpesa(capienza_serba_ncx, costo_lit, tot_km, autonomia_km_ncx);
        double costo_tot_xrv = CalcoloKm.calcolaSpesa(capienza_serba_xrv, costo_lit, tot_km, autonomia_km_xrv);

        System.out.println("Spesa tot. per " + tot_km + " km in  " + n_mesi+" mesi");
        System.out.println(" NCX " + costo_tot_ncx + " Euro");
        System.out.println(" XRV " + costo_tot_xrv + " Euro");

    }
}
