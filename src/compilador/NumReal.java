/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

/**
 *
 * @author ryane
 */
public class NumReal extends Token{
    
    public final double value;
    
    public NumReal(double value) {
        super(Tag.REAL);
        this.value = value;
    }

    public double getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return "" + value;
    }
}
