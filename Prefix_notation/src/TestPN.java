

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import java.io.*;
import static org.junit.Assert.assertEquals;



public class TestPN {

    private static final String tests_excel = "res/tests.xls";
    private static final double DELTA = 0.00001;

    @Test(expected = java.util.EmptyStackException.class)
    public void testEmptyInput()
    {
        PrefixNotation pn = new PrefixNotation();
        pn.Start("");
    }

    @Test(expected =  java.lang.IllegalArgumentException.class)
    public void testIllegalScope()
    {
        PrefixNotation pn = new PrefixNotation();
        pn.Start("3*(1+2))");
    }
    @Test(expected = java.lang.ArithmeticException.class)
    public void DivByZero()
    {
        PrefixNotation pn = new PrefixNotation();
        pn.Start("3/0");
    }

    @Test
    public void testPolishNotation() throws IOException {
        double result;
        PrefixNotation pn = new PrefixNotation();
        try (HSSFWorkbook wb = new HSSFWorkbook(
                new FileInputStream(tests_excel))) {
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                String input = row.getCell(0).getStringCellValue();
                if (input.isEmpty()) break;
                double output = row.getCell(1).getNumericCellValue();
                result= Double.parseDouble(pn.Start((input)));
                assertEquals(output,result, DELTA);
            }
        }
    }
}
