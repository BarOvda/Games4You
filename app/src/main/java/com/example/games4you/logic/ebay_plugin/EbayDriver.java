package com.example.games4you.logic.ebay_plugin;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class EbayDriver {


    public final static String EBAY_APP_ID = "BarOvda-Games4Yo-PRD-9c8ec878c-ae291bb6";

    public final static String EBAY_FINDING_SERVICE_URI = "https://svcs.ebay.com/services/search/FindingService/v1?SECURITY-APPNAME="
            + "{applicationId}&OPERATION-NAME={operation}&SERVICE-VERSION={version}"
            + "&REST-PAYLOAD&keywords={keywords}&paginationInput.entriesPerPage={maxresults}"
            + "&GLOBAL-ID={globalId}&siteid=0";
    public static final String SERVICE_VERSION = "1.0.0";
    public static final String OPERATION_NAME = "findItemsByKeywords";
    public static final String GLOBAL_ID = "EBAY-US";
    public final static int REQUEST_DELAY = 3000;
    public final static int MAX_RESULTS = 3;
    private int maxResults;
    private Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            try  {

                String address = createAddress(tag);
                Log.d("sending request to :: ", address);
                String response = URLReader.read(address);
                Log.d("response :: ", response);
                //process xml dump returned from EBAY
                processResponse(response);
                //Honor rate limits - wait between results
                Thread.sleep(REQUEST_DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    private String tag;


    public EbayDriver() {
        this.maxResults = MAX_RESULTS;
    }

    public EbayDriver(int maxResults) {
        this.maxResults = maxResults;
    }


    public void runDriver(String tag) {
        this.tag = tag;
        thread.start();

    }

    private String createAddress(String tag) {

        //substitute token
        String address = EbayDriver.EBAY_FINDING_SERVICE_URI;
        address = address.replace("{version}", EbayDriver.SERVICE_VERSION);
        address = address.replace("{operation}", EbayDriver.OPERATION_NAME);
        address = address.replace("{globalId}", EbayDriver.GLOBAL_ID);
        address = address.replace("{applicationId}", EbayDriver.EBAY_APP_ID);
        address = address.replace("{keywords}", tag);
        address = address.replace("{maxresults}", "" + this.maxResults);

        return address;

    }

    private void processResponse(String response) throws Exception {


        XPath xpath = XPathFactory.newInstance().newXPath();
        InputStream is = new ByteArrayInputStream(response.getBytes("UTF-8"));
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = domFactory.newDocumentBuilder();


        Document doc = builder.parse(is);
        XPathExpression ackExpression = xpath.compile("//findItemsByKeywordsResponse/ack");
        XPathExpression itemExpression = xpath.compile("//findItemsByKeywordsResponse/searchResult/item");

        String ackToken = (String) ackExpression.evaluate(doc, XPathConstants.STRING);
        Log.d("ACK from ebay API :: ", ackToken);
        if (!ackToken.equals("Success")) {
            throw new Exception(" service returned an error");
        }

        NodeList nodes = (NodeList) itemExpression.evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {

            Node node = nodes.item(i);

            String itemId = (String) xpath.evaluate("itemId", node, XPathConstants.STRING);
            String title = (String) xpath.evaluate("title", node, XPathConstants.STRING);
            String itemUrl = (String) xpath.evaluate("viewItemURL", node, XPathConstants.STRING);
            String galleryUrl = (String) xpath.evaluate("galleryURL", node, XPathConstants.STRING);

            String currentPrice = (String) xpath.evaluate("sellingStatus/currentPrice", node, XPathConstants.STRING);

            Log.d("currentPrice", currentPrice);
            Log.d("itemId", itemId);
            Log.d("title", title);
            Log.d("galleryUrl", galleryUrl);

        }

        is.close();

    }


}
