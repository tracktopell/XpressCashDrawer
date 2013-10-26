/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.ticket;

/**
 * NumeroCastellano - Convierte un n&uacute;mero a castellano Copyright
 *
 * @author alfredo
 *
 * Este programa es Software Libre; usted puede redistribuirlo y/o modificarlo
 * bajo los t?rminos de la "GNU General Public License" como lo publica la "FSF
 * Free Software Foundation", o (a su elecci?n) de cualquier versi?n posterior.
 *
 * Este programa es distribuido con la esperanza de que le ser? ?til, pero SIN
 * NINGUNA GARANTIA; incluso sin la garant?a impl?cita por el MERCADEO o
 * EJERCICIO DE ALGUN PROPOSITO en particular. Vea la "GNU General Public
 * License" para m?s detalles.
 *
 * Usted debe haber recibido una copia de la "GNU General Public License" junto
 * con este programa, si no, escriba a la "FSF Free Software Foundation, Inc.",
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */
public class NumeroCastellano {

    private static String[] _grupos = {"", "millon", "billon", "trillon"};
    private static String[] _unidades = {"", "un", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};
    private static String[] _decena1 = {"", "once", "doce", "trece", "catorce", "quince",
        "dieciseis", "diecisiete", "dieciocho", "diecinueve"};
    private static String[] _decenas = {"", "diez", "veinte", "treinta", "cuarenta", "cincuenta",
        "sesenta", "setenta", "ochenta", "noventa"};
    private static String[] _centenas = {"", "cien", "doscientos", "trescientos", "cuatrocientos",
        "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"};

    public static String millarATexto(int n) {
        if (n == 0) {
            return "";
        }

        int centenas = n / 100;
        n = n % 100;
        int decenas = n / 10;
        int unidades = n % 10;

        String sufijo = "";

        if (decenas == 0 && unidades != 0) {
            sufijo = _unidades[unidades];
        }

        if (decenas == 1 && unidades != 0) {
            sufijo = _decena1[unidades];
        }

        if (decenas == 2 && unidades != 0) {
            sufijo = "veinti" + _unidades[unidades];
        }

        if (unidades == 0) {
            sufijo = _decenas[decenas];
        }

        if (decenas > 2 && unidades != 0) {
            sufijo = _decenas[decenas] + " y " + _unidades[unidades];
        }

        if (centenas != 1) {
            return _centenas[centenas] + " " + sufijo;
        }

        if (unidades == 0 && decenas == 0) {
            return "cien";
        }

        return "ciento " + sufijo;
    }

    public static String numeroACastellano(long n) {
        String resultado = "";
        int grupo = 0;
        while (n != 0 && grupo < _grupos.length) {
            long fragmento = n % 1000000;
            int millarAlto = (int) (fragmento / 1000);
            int millarBajo = (int) (fragmento % 1000);
            n = n / 1000000;

            String nombreGrupo = _grupos[grupo];
            if (fragmento > 1 && grupo > 0) {
                nombreGrupo += "es";
            }


            if ((millarAlto != 0) || (millarBajo != 0)) {
                if (millarAlto > 1) {
                    resultado = millarATexto(millarAlto) + " mil "
                            + millarATexto(millarBajo) + " "
                            + nombreGrupo + " "
                            + resultado;
                }


                if (millarAlto == 0) {
                    resultado = millarATexto(millarBajo) + " "
                            + nombreGrupo + " "
                            + resultado;
                }

                if (millarAlto == 1) {
                    resultado = "mil " + millarATexto(millarBajo) + " "
                            + nombreGrupo + " "
                            + resultado;
                }
            }
            grupo++;
        }
        return resultado;
    }

    public static void main(String[] args) {
        //System.out.println(numeroACastellano(Long.parseLong(args[0])));
        System.out.println(numeroACastellano(213897747213423L));
    }
}