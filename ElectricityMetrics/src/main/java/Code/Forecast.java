package Code;


import java.util.*;

public class Forecast {
    /* String[] Inverno = {"12/21","3/20"};
     String[] Outono = {"9/22","12/21"};
     String[] Primavera = {"3/21","6/21"};
     String[] Verao = {"6/21","9/22"};*/
    ReadData rd = new ReadData();
    private int[] Inverno = {12, 3};
    private int[] Primavera = {3, 6};
    private int[] Verao = {6, 9};
    private int[] Outono = {9, 12};

    /*
    String[] HVerao = {"3/25"};
    String[] HInverno = {"10/25"};*/
    private int HVerao = 3;
    private int HInverno = 10;

    private double pesoInverno   = 0.2;
    private double pesoVerao     = pesoInverno+0.1;
    private double pesoPrimavera = -0.1;
    private double pesoOutono    = -0.1;

    private Map<Integer,Double> horasPicos = new TreeMap<Integer, Double>();

    public double previsao(String date) {
        double pesoHora=0.04;
        double pesoEstacao=0;
        boolean hora=false; // caso tenha pico na hora de verao deve ser dado mais peso
        if(horasPicos.size()==0){
            CalculaHorasPicos();
        }

        //averiguar se as horas apos o por do sol são picos se forem a hora de verao tem maior peso nessa hora ficando
        // mais barato na hora de verao

        List<Integer> horasMaiorUtilizacao = getMaxValues();
        List<Integer> horasSol = new ArrayList<Integer>();
        horasSol.add(17);horasSol.add(18);horasSol.add(19);horasSol.add(20);
        if(!Collections.disjoint(horasMaiorUtilizacao, horasSol)){
            //maior peso na hora de inverno menor na de verao
            hora=true;
        }

        if(isHorarioVerao(date)){
            if(hora){
                pesoHora=-0.091;
            }else{
                pesoHora=-0.046;
            }
        }

        pesoEstacao = getPesoEstacao(date);

        Map<String,Double> mediaMes = rd.getMediaMes();
        double media=0;
        for(String s : mediaMes.keySet()){
            media+=mediaMes.get(s);
        }

        media = media/mediaMes.size();


        return media+(pesoEstacao*media)+(pesoHora*media);

    }



    private List<Integer> getMaxValues() {
        List<Integer> res = new ArrayList<Integer>();
        List<Double> values = new ArrayList<Double>(horasPicos.values());
        Collections.sort(values, Collections.reverseOrder());
        for(int i=0 ; i< 7;i++) {
            for (Map.Entry<Integer, Double> entry : horasPicos.entrySet()) {
                if (entry.getValue()==values.get(i)) {
                    res.add(entry.getKey());
                }
            }
        }
        return res;
    }


    public void CalculaHorasPicos( ){

        Map<String,Double> picos = rd.getPicos();
        int cont=0;
        String date = " ";
        double max=0;
        for(String s : picos.keySet()){

            if(!s.contains(date)) {
                date = s.split("[.]")[0];
                String[] anoMesDia = date.split("/");
                max = rd.getKwMaximum(anoMesDia[0],anoMesDia[1],anoMesDia[2]);
            }

            if(picos.get(s)>=max-5) {
                int hora = Integer.parseInt(s.split("[.]")[1]);
                if(horasPicos.containsKey(hora)) {
                    horasPicos.put(hora, horasPicos.get(hora) + 1);
                }else{
                    horasPicos.put(hora, 1.0);
                }
                cont++;
            }
        }
        for(int h : horasPicos.keySet()){
            horasPicos.put(h,(horasPicos.get(h)/cont)*100);
        }

    }


    public double getPesoEstacao(String s1) {
        String[] spl = s1.split("/");
        int mes = Integer.parseInt(spl[1]);

        if(mes==Inverno[0] || mes<=Inverno[1])
            return pesoInverno;
        if(mes>=Verao[0]&& mes<=Verao[1])
            return pesoVerao;
        if(mes>=Outono[0]&& mes<=Outono[1])
            return pesoOutono;
        else
            return pesoPrimavera;

    }

    public boolean isHorarioVerao(String s1) {
        String[] spl = s1.split("/");
        int mes = Integer.parseInt(spl[1]);

        if(mes>=HVerao && mes<=HInverno)
            return true;
        else
            return false;
    }
}
