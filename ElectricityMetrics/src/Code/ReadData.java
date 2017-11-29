package Code;

import java.io.*;
import java.sql.ResultSet;
import java.util.*;

public class ReadData {

    private Database db = new Database();
    String csvFile = "Test.csv";
    String cvsSplitBy = ";";
    BufferedReader br ;
    Map<String,Double> mediaDia = new TreeMap<String, Double>();
    Map<String,Double> desvioDia = new TreeMap<String, Double>();
    Map<String,Double> mediaMes = new TreeMap<String, Double>();
    Map<String,Double> desvioMes = new TreeMap<String, Double>();


    public ReadData(){
        db = new Database();
        try {
            db.connect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> getMonthByYear(String year){
        String query_getMonth = "select distinct month from energy_history where year = " + year +";";

        ResultSet rs = db.getData(query_getMonth);
        ArrayList<String> m = new ArrayList<String>();
        m.add("Mês");
        try{
            while(rs.next()){
                m.add(String.valueOf(rs.getInt(1)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return m;
    }

    public ArrayList<String> getDayByMonthYear(String year, String month){
        String query = "select distinct day from energy_history where year = " + year +" and month = " + month + ";";

        ResultSet rs = db.getData(query);
        ArrayList<String> d = new ArrayList<String>();
        d.add("Dias");
        try{
            while(rs.next()){
                d.add(String.valueOf(rs.getInt(1)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return d;
    }

    public ArrayList<String> years(){
        String query_getYears = "select distinct year from energy_history group by year;";
        ResultSet rs = db.getData(query_getYears);
        ArrayList<String> y = new ArrayList<String>();
        y.add("Ano");
        try{
            while(rs.next()){
                y.add(String.valueOf(rs.getInt(1)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return y;
    }

    public Map<String,Double> getMediaDia(){
        if(mediaDia.size()==0){
            CalcMediaDia();
            return mediaDia;
        }else{
            return mediaDia;
        }
    }

    public Map<String,Double> getMediaDia(String year, String month, String day){
        String date = year + "/" + month + "/" + day;
        Map<String,Double> res = new TreeMap<>();

        res.put(date,mediaDia.get(date));

        return res;
    }

    public Map<String,Double> getDesvioDia(){
        if(desvioDia.size()==0){
            CalcDesvioDia();
            return desvioDia;
        }else{
            return desvioDia;
        }
    }

    public Map<String,Double> getDesvioDia(String year, String month, String day){
        String date = year + "/" + month + "/" + day;
        Map<String,Double> res = new TreeMap<>();

        res.put(date,desvioDia.get(date));

        return res;
    }

    public Map<String,Double> getMediaMes(){
        if(mediaMes.size()==0){
            CalcMediaMes();
            return mediaMes;
        }else{
            return mediaMes;
        }
    }


    public Map<String,Double> getDesvioMes(){
        if(desvioMes.size()==0){
            CalcDesvioMes();
            return desvioMes;
        }else{
            return desvioMes;
        }
    }

    public void CalcMediaDia(){
        String query = "select year,month,day,avg(ch1_kw_avg) from energy_history group by month,year,day;";
        try{
            ResultSet rs = db.getData(query);
            while (rs.next()) {
                String date = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getString(3);

                if(!mediaDia.containsKey(date)){
                    mediaDia.put(date, Double.parseDouble(rs.getString(4)));
                }else{
                    mediaDia.put(date,Double.parseDouble(rs.getString(4)));
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*public void CalcMediaDia(){

        Map<String, Integer> cont  =new HashMap<String, Integer>();

        try{
            br= new BufferedReader(new FileReader(csvFile));
            String line=br.readLine();
            while ((line = br.readLine()) != null) {
                String[] dados = line.split(cvsSplitBy);
                if(!mediaDia.containsKey(dados[0])){
                    mediaDia.put(dados[0],getDouble(dados[1])+getDouble(dados[2]));
                    cont.put(dados[0],1);
                }else{
                    mediaDia.put(dados[0],mediaDia.get(dados[0])+getDouble(dados[1])+getDouble(dados[2]));
                    cont.put(dados[0],cont.get(dados[0])+1);
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s : mediaDia.keySet()) {
            mediaDia.put(s,mediaDia.get(s)/cont.get(s));
        }

    }*/

    public void CalcDesvioDia(){
        String query = "select year,month,day,AVG((ch1_kw_avg - sub.a) * (ch1_kw_avg  - sub.a)) as var from energy_history , " +
                " (SELECT AVG(ch1_kw_avg) AS a FROM energy_history) AS sub group by month,year,day;";
        try{
            ResultSet rs = db.getData(query);
            while (rs.next()) {
                String date = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getString(3);

                if(!desvioDia.containsKey(date)){
                    desvioDia.put(date, Math.sqrt(Double.parseDouble(rs.getString(4))));
                }else{
                    desvioDia.put(date,Math.sqrt(Double.parseDouble(rs.getString(4))));
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*public void CalcDesvioDia(){
        Map<String, Integer> cont  =new HashMap<String, Integer>();
        try{
            br= new BufferedReader(new FileReader(csvFile));
            String line=br.readLine();
            while ((line = br.readLine()) != null) {
                String[] dados = line.split(cvsSplitBy);
                if(!desvioDia.containsKey(dados[0])){
                    desvioDia.put(dados[0],Math.pow(getDouble(dados[1])+getDouble(dados[2])-mediaDia.get(dados[0]),2));
                    cont.put(dados[0],1);
                }else{
                    desvioDia.put(dados[0],desvioDia.get(dados[0])+Math.pow(getDouble(dados[1])+getDouble(dados[2])-mediaDia.get(dados[0]),2));
                    cont.put(dados[0],cont.get(dados[0])+1);
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s : desvioDia.keySet()) {
            desvioDia.put(s,Math.sqrt(desvioDia.get(s)/cont.get(s)));
        }

    }*/




    public void CalcMediaMes(){
        Map<String, Integer> cont  =new HashMap<String, Integer>();
        String mes;
        String[] data;
        for(String s : mediaDia.keySet()){
            data = s.split("/");
            mes= data[1]+"/"+data[2];
            if(!mediaMes.containsKey(mes)){
                mediaMes.put(mes,mediaDia.get(s));
                cont.put(mes,1);
            }else{
                mediaMes.put(mes,mediaMes.get(mes)+mediaDia.get(s));
                cont.put(mes,cont.get(mes)+1);
            }
        }

        for (String s : mediaMes.keySet()) {
            mediaMes.put(s,mediaMes.get(s)/cont.get(s));
        }

    }

    public void CalcDesvioMes(){
        Map<String, Integer> cont  =new HashMap<String, Integer>();
        String mes;
        String[] data;
        for(String s : mediaDia.keySet()){
            data = s.split("/");
            mes=data[1]+"/"+data[2];
            if(!desvioMes.containsKey(mes)){
                desvioMes.put(mes,Math.pow(mediaDia.get(s)-mediaMes.get(mes),2));
                cont.put(mes,1);
            }else{
                desvioMes.put(mes,desvioMes.get(mes)+Math.pow(mediaDia.get(s)-mediaMes.get(mes),2));
                cont.put(mes,cont.get(mes)+1);
            }
        }

        for (String s : mediaMes.keySet()) {
            mediaMes.put(s,Math.sqrt(mediaMes.get(s)/cont.get(s)));
        }
    }




    private double getDouble(String s){
        double d;
        try{
            d=Double.parseDouble(s);
            return d;
        }catch (NumberFormatException n){
            return 0;
        }

    }


}


