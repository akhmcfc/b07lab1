public class Polynomial {
    double[] coefficients;

    public Polynomial() {
        coefficients = new double[]{0};
    }

    public Polynomial(double[] coeffs) {
        coefficients = coeffs;
    }

    public Polynomial add(Polynomial other) {
        int maxLen = Math.max(this.coefficients.length, other.coefficients.length);
        double[] result = new double[maxLen];
        for (int i = 0; i < this.coefficients.length; i++)
            result[i] += this.coefficients[i];
        for (int i = 0; i < other.coefficients.length; i++)
            result[i] += other.coefficients[i];
        return new Polynomial(result);
    }

    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++)
            result += coefficients[i] * Math.pow(x, i);
        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}