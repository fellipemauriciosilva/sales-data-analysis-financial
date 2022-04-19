package br.com.agibank.sale.fixture;

import org.assertj.core.util.Strings;

public class DataFileFixture {

    public static String loadDataToFileTests(){

        return Strings.concat(
                "001ç1234567891234çPedroç50000\n" +
                        "001ç3245678865434çPauloç40000.99\n" +
                        "002ç2345675434544345çJose da SilvaçRural\n" +
                        "002ç2345675433444345çEduardo PereiraçRural\n" +
                        "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro\n" +
                        "003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo"
        );
    }

    public static String loadDataToFileWithNumberNotExistsTests(){

        return Strings.concat(
                "001ç1234567891234çPedroç50000\n" +
                        "001ç3245678865434çPauloç40000.99\n" +
                        "002ç2345675434544345çJose da SilvaçRural\n" +
                        "002ç2345675433444345çEduardo PereiraçRural\n" +
                        "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro\n" +
                        "003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo\n" +
                        "004ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo"
        );
    }

    public static String loadDataToFileWithFourSalesMan(){
        return Strings.concat(
                "001ç1234567891234çPedroç50000\n" +
                        "001ç3245678865434çPauloç40000.99\n" +
                        "001ç32456788654341çMarcosç120000.99\n" +
                        "001ç324567886543422çDavidç130000.99\n" +
                        "002ç2345675434544345çJose da SilvaçRural\n" +
                        "002ç2345675433444345çEduardo PereiraçRural\n" +
                        "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro\n" +
                        "003ç22ç[1-11-300,2-31-5.50,3-41-3.25]çMarcos\n" +
                        "003ç35ç[1-1-0.50,2-1-1.50,3-1-3.25]çDavid\n" +
                        "003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo"
        );
    }

}
