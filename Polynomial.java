import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Polynomial {
    private double[] coefficients;  // non-zero coefficients
    private int[] exponents;         // corresponding exponents
    
    // No-argument constructor - polynomial zero
    public Polynomial() {
        this.coefficients = new double[]{0};
        this.exponents = new int[]{0};
    }
    
    // Constructor with arrays (for backward compatibility)
    public Polynomial(double[] coeffs) {
        ArrayList<Double> coeffList = new ArrayList<>();
        ArrayList<Integer> expList = new ArrayList<>();
        
        for (int i = 0; i < coeffs.length; i++) {
            if (coeffs[i] != 0) {
                coeffList.add(coeffs[i]);
                expList.add(i);
            }
        }
        
        if (coeffList.isEmpty()) {
            this.coefficients = new double[]{0};
            this.exponents = new int[]{0};
        } else {
            this.coefficients = new double[coeffList.size()];
            this.exponents = new int[expList.size()];
            for (int i = 0; i < coeffList.size(); i++) {
                this.coefficients[i] = coeffList.get(i);
                this.exponents[i] = expList.get(i);
            }
        }
    }
    
    // Constructor from File
    public Polynomial(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        String polynomialStr = scanner.nextLine();
        scanner.close();
        
        ArrayList<Double> coeffList = new ArrayList<>();
        ArrayList<Integer> expList = new ArrayList<>();
        
        String[] terms = polynomialStr.split("(?=[+-])");
        
        for (String term : terms) {
            double coeff;
            int exp;
            
            if (term.contains("x")) {
                String[] parts = term.split("x");
                String coeffPart = parts[0];
                if (coeffPart.equals("+") || coeffPart.equals("")) {
                    coeff = 1.0;
                } else if (coeffPart.equals("-")) {
                    coeff = -1.0;
                } else {
                    coeff = Double.parseDouble(coeffPart);
                }
                
                if (parts.length > 1 && !parts[1].isEmpty()) {
                    exp = Integer.parseInt(parts[1]);
                } else {
                    exp = 1;
                }
            } else {
                coeff = Double.parseDouble(term);
                exp = 0;
            }
            
            coeffList.add(coeff);
            expList.add(exp);
        }
        
        this.coefficients = new double[coeffList.size()];
        this.exponents = new int[expList.size()];
        for (int i = 0; i < coeffList.size(); i++) {
            this.coefficients[i] = coeffList.get(i);
            this.exponents[i] = expList.get(i);
        }
    }
    
    // Add two polynomials
    public Polynomial add(Polynomial other) {
        ArrayList<Double> resultCoeffs = new ArrayList<>();
        ArrayList<Integer> resultExps = new ArrayList<>();
        
        for (int i = 0; i < this.exponents.length; i++) {
            resultExps.add(this.exponents[i]);
            resultCoeffs.add(this.coefficients[i]);
        }
        
        for (int j = 0; j < other.exponents.length; j++) {
            int exp = other.exponents[j];
            double coeff = other.coefficients[j];
            int index = resultExps.indexOf(exp);
            if (index != -1) {
                double newCoeff = resultCoeffs.get(index) + coeff;
                if (newCoeff != 0) {
                    resultCoeffs.set(index, newCoeff);
                } else {
                    resultCoeffs.remove(index);
                    resultExps.remove(index);
                }
            } else {
                resultExps.add(exp);
                resultCoeffs.add(coeff);
            }
        }
        
        if (resultCoeffs.isEmpty()) {
            return new Polynomial();
        }
        
        sortByExponent(resultCoeffs, resultExps);
        
        double[] coeffArray = new double[resultCoeffs.size()];
        int[] expArray = new int[resultExps.size()];
        for (int i = 0; i < resultCoeffs.size(); i++) {
            coeffArray[i] = resultCoeffs.get(i);
            expArray[i] = resultExps.get(i);
        }
        
        Polynomial result = new Polynomial();
        result.coefficients = coeffArray;
        result.exponents = expArray;
        return result;
    }
    
    // Multiply two polynomials
    public Polynomial multiply(Polynomial other) {
        ArrayList<Double> resultCoeffs = new ArrayList<>();
        ArrayList<Integer> resultExps = new ArrayList<>();
        
        for (int i = 0; i < this.exponents.length; i++) {
            for (int j = 0; j < other.exponents.length; j++) {
                double coeff = this.coefficients[i] * other.coefficients[j];
                int exp = this.exponents[i] + other.exponents[j];
                
                int index = resultExps.indexOf(exp);
                if (index != -1) {
                    double newCoeff = resultCoeffs.get(index) + coeff;
                    if (newCoeff != 0) {
                        resultCoeffs.set(index, newCoeff);
                    } else {
                        resultCoeffs.remove(index);
                        resultExps.remove(index);
                    }
                } else {
                    resultExps.add(exp);
                    resultCoeffs.add(coeff);
                }
            }
        }
        
        if (resultCoeffs.isEmpty()) {
            return new Polynomial();
        }
        
        sortByExponent(resultCoeffs, resultExps);
        
        double[] coeffArray = new double[resultCoeffs.size()];
        int[] expArray = new int[resultExps.size()];
        for (int i = 0; i < resultCoeffs.size(); i++) {
            coeffArray[i] = resultCoeffs.get(i);
            expArray[i] = resultExps.get(i);
        }
        
        Polynomial result = new Polynomial();
        result.coefficients = coeffArray;
        result.exponents = expArray;
        return result;
    }
    
    private void sortByExponent(ArrayList<Double> coeffs, ArrayList<Integer> exps) {
        for (int i = 0; i < exps.size() - 1; i++) {
            for (int j = i + 1; j < exps.size(); j++) {
                if (exps.get(i) > exps.get(j)) {
                    int tempExp = exps.get(i);
                    exps.set(i, exps.get(j));
                    exps.set(j, tempExp);
                    double tempCoeff = coeffs.get(i);
                    coeffs.set(i, coeffs.get(j));
                    coeffs.set(j, tempCoeff);
                }
            }
        }
    }
    
    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, exponents[i]);
        }
        return result;
    }
    
    public boolean hasRoot(double x) {
        return Math.abs(evaluate(x)) < 1e-10;
    }
    
    public void saveToFile(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            double coeff = coefficients[i];
            int exp = exponents[i];
            
            if (i > 0 && coeff > 0) {
                sb.append("+");
            }
            
            if (coeff == -1 && exp > 0) {
                sb.append("-");
            } else if (coeff != 1 || exp == 0) {
                sb.append(coeff);
            }
            
            if (exp > 0) {
                sb.append("x");
                if (exp > 1) {
                    sb.append(exp);
                }
            }
        }
        writer.write(sb.toString());
        writer.close();
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            if (i > 0 && coefficients[i] > 0) {
                sb.append("+");
            }
            sb.append(coefficients[i]);
            if (exponents[i] > 0) {
                sb.append("x");
                if (exponents[i] > 1) {
                    sb.append(exponents[i]);
                }
            }
        }
        return sb.toString();
    }
}