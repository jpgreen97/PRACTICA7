// Derived class FichaTridomino
public class FichaTridomino extends Ficha {
    private int lado3;

    public FichaTridomino(int lado1, int lado2, int lado3) {
        super(lado1, lado2);
        this.lado3 = lado3;
    }

    public int getLado3() {
        return lado3;
    }

    @Override
    public void rotateRight() {
        int temp = getLado1();
        super.rotateRight();
        this.lado3 = temp;
    }

    @Override
    public void rotateLeft() {
        int temp = lado3;
        this.lado3 = getLado2();
        super.rotateLeft();
        super.rotateLeft();
    }

    @Override
    public String toString() {
        return "   " + getLado1() + "\n" +
                " " + getLado2() + "   " + getLado3();
    }
}
