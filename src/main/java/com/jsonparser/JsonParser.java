package com.jsonparser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;

public class JsonParser {
    private boolean fileParsed;

    public boolean parseJson(String filePath){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try{
            if(null == filePath || filePath.isEmpty())
                throw  new IllegalArgumentException("invalid File/Filepath");
            URL resource = getClass().getClassLoader().getResource(filePath);
            if(resource == null)
                throw  new IllegalArgumentException("Invalid File/Filepath");
            List<IpScore> ipScores = mapper.readValue(new File(getClass().getClassLoader().getResource(filePath).getFile()), new TypeReference<ArrayList<IpScore>>(){});
            Map<String, List<IpScore>> scoreMap = new LinkedHashMap<>();
            Map<String,Integer> idScores = new LinkedHashMap<>();
            List<IpScore> list;

            for (IpScore ipScore1 : ipScores) {
                IpScore ipScore = ipScore1;
                if (!scoreMap.containsKey(ipScore.getId())) {
                    list = new ArrayList<>();
                    list.add(ipScore);
                    scoreMap.put(ipScore.getId(), list);
                } else {
                    list = scoreMap.get(ipScore.getId());
                    list.add(ipScore);
                    scoreMap.put(ipScore.getId(), list);
                }

            }

            for(Map.Entry<String, List<IpScore>> entry : scoreMap.entrySet()) {
                String key = entry.getKey();
                System.out.println(key + ":");
                List<IpScore> ipScoresList = entry.getValue();
                Map<String,Integer> ipCount = new LinkedHashMap<>();
                int score = 0;
                for (IpScore ipScore1 : ipScoresList) {
                    if(idScores.keySet().contains(ipScore1.getId())){

                    }

                    if (ipCount.keySet().contains(ipScore1.getIp())) {
                        ipCount.put(ipScore1.getIp(), ipCount.get(ipScore1.getIp()) + 1);
                    } else {
                        ipCount.put(ipScore1.getIp(), 1);
                    }
                    score = score+ ipScore1.getScore();
                }
                ipCount = ipCount.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue,
                                LinkedHashMap::new
                        ));
                ipCount.put("score", score);

                for(Map.Entry<String, Integer> temp : ipCount.entrySet()){
                    System.out.println("\t" + temp.getKey() + " : " + temp.getValue());
                }
            }
            fileParsed = true;

        }  catch (JsonMappingException e){
            throw  new IllegalArgumentException("No content to map due to end-of-input");
        } catch (IOException e){
            e.printStackTrace();
        }
        return fileParsed;
    }

}
