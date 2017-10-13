package efcee.calculator;

/**
 * Includes advanced mathematics not found in the java Math class
 */

public final class MathPlus {

    public static double asinh(double x)
    {
        return Math.log(x + Math.sqrt(x*x + 1.0));
    }

    public static double acosh(double x)
    {
        return Math.log(x + Math.sqrt(x*x - 1.0));
    }

    public static double atanh(double x)
    {
        return 0.5*Math.log( (x + 1.0) / (x - 1.0) );
    }

    public static int fact(int x) {
        if (x == 0) {
            return 1;
        }
        int fact = 1; // this  will be the result_edit_text
        for (int i = 1; i <= x; i++) {
            fact *= i;
        }
        return fact;
    }
}
