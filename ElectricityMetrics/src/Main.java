import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ReadData rd = new ReadData();
        Map<String,Double> medias = rd.getMediaDia();
        for(String s : medias.keySet()){
            System.out.println(s+"   :"+ medias.get(s));
        }

        System.out.println("-------------------------------");
        Map<String,Double> desvio = rd.getDesvioDia();
        for(String s : desvio.keySet()){
            System.out.println(s+"   :"+ desvio.get(s));
        }
        System.out.println("-------------------------------");
        Map<String,Double> mediaM = rd.getMediaMes();
        for(String s : mediaM.keySet()){
            System.out.println(s+"   :"+ mediaM.get(s));
        }

        System.out.println("-------------------------------");
        Map<String,Double> desvioM = rd.getDesvioMes();
        for(String s : desvioM.keySet()){
            System.out.println(s+"   :"+ desvioM.get(s));
        }
    }
}
