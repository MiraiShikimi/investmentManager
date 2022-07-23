package com.csgoinvestmentmanager.investmentManager.randomFuncitions;

import com.csgoinvestmentmanager.investmentManager.Exeptions.Http429Expection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;


public class ValveApi {
    /**
     * gets the Lowest selling price form the Steam Community market for an item defined by its hash name
     * @param itemName the item hash name
     * @return the lowest selling price of the iteam
     */
    public BigDecimal getItemPriceFromValveApi(String itemName, BigDecimal currentPrice){

        BigDecimal itemPrice = currentPrice;

        try {


            URL url = new URL("https://steamcommunity.com/market/priceoverview/?currency=3&country=DE&appid=730&market_hash_name=" +
                    itemName);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            System.out.println(url);
            int responseCode = 0;
            try {
                //Check if connect is made
                responseCode = conn.getResponseCode();
            }catch (IOException e){
                System.out.println("it errored here");
                e.printStackTrace();
            }

            if (responseCode == 429) {
                System.out.println("now it come to here");
                Thread.sleep(60000);
                throw new Http429Expection("Too many Requests");

            }

            // 200 OK
           else if (responseCode != 200) {
                conn.disconnect();
              //
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                Scanner scanner = new Scanner(url.openStream());

                String apiReqquest = null;
                while (scanner.hasNext()) {
                    apiReqquest = scanner.nextLine();

                }
                //Close the scanner
                scanner.close();


                //JSON simple library Setup with Maven is used to convert strings to JSON
                JSONParser parser = new JSONParser();
                JSONObject recivedData = (JSONObject) parser.parse(apiReqquest);

                System.out.println(recivedData);

                itemPrice = new BigDecimal(((recivedData).get("lowest_price").toString().substring(0,4).replace(',','.').replaceAll("-","0")));
                System.out.println(((recivedData).get("lowest_price").toString().substring(0,4).replace(',','.')));
                conn.disconnect();


                System.out.println("done :D");
            }

        } catch (ProtocolException e) {
            System.out.println("protocol expection");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("protocol expection");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOExeption expection");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Parese expection");
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted expection");
            e.printStackTrace();
        }
        return itemPrice;

    }


}
