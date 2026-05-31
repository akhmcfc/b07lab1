import java.io.File;
import java.io.IOException;

public class Driver {
    public static void main(String [] args) {
        Polynomial p = new Polynomial();
        System.out.println(p.evaluate(3));
        
        double [] c1 = {6,0,0,5};
        Polynomial p1 = new Polynomial(c1);
        double [] c2 = {0,-2,0,0,-9};
        Polynomial p2 = new Polynomial(c2);
        Polynomial s = p1.add(p2);
        System.out.println("s(0.1) = " + s.evaluate(0.1));
        
        if(s.hasRoot(1))
            System.out.println("1 is a root of s");
        else
            System.out.println("1 is not a root of s");
        
        Polynomial product = p1.multiply(p2);
        System.out.println("Product at x=1: " + product.evaluate(1));
        
        try {
            File testFile = new File("test_poly.txt");
            java.io.FileWriter writer = new java.io.FileWriter(testFile);
            writer.write("5-3x2+7x8");
            writer.close();
            
            Polynomial p3 = new Polynomial(testFile);
            System.out.println("Loaded polynomial: " + p3);
            System.out.println("p3(1) = " + p3.evaluate(1));
            
            p3.saveToFile("output_poly.txt");
            System.out.println("Saved polynomial to output_poly.txt");
            
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}