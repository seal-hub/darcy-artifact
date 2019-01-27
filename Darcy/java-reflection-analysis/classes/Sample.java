

public class Sample {

    int hei;
    public Sample(){
        hei = 2;
    }

    public String concat(String a, String b){
        String ab = a + b;
        return ab;
    }

    public static String doSomething(String a){
        return a + a;
    }

    public static void main(String[] args) {
        String a = "salam";
        String b = "hello";
        Sample sample = new Sample();
        String ab = sample.concat(a,b);
        System.out.println(ab);
    }
}
