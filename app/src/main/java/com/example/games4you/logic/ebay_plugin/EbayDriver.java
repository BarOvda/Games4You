package com.example.games4you.logic.ebay_plugin;

import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.games4you.GamePageFragment;
import com.example.games4you.logic.EbayTitle;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class EbayDriver extends AsyncTask<Void, Void, Void> {


    public final static String EBAY_APP_ID = "BarOvda-Games4Yo-PRD-9c8ec878c-ae291bb6";

    public final static String EBAY_FINDING_SERVICE_URI = "https://svcs.ebay.com/services/search/FindingService/v1?SECURITY-APPNAME="
            + "{applicationId}&OPERATION-NAME={operation}&SERVICE-VERSION={version}"
            +"&RESPONSE-DATA-FORMAT=XML"
            + "&REST-PAYLOAD=true&keywords={keywords}&paginationInput.entriesPerPage={maxresults}"
            +"&categoryId=139973"
            +"&sortOrder=PricePlusShippingLowest"
            + "&GLOBAL-ID={globalId}&siteid=0";
    public static final String SERVICE_VERSION = "1.0.0";
    public static final String OPERATION_NAME = /*"findItemsByKeywords"*/"findItemsAdvanced";
    public static final String GLOBAL_ID = "EBAY-US";
    public final static int REQUEST_DELAY = 0;
    public final static int MAX_RESULTS = 6;
    private int maxResults;
    private String console;
    private GamePageFragment.IProcess mProcess;

    private String tag;
    private List<EbayTitle> titles;

    public EbayDriver() {
        this.titles = new ArrayList<>();
        this.maxResults = MAX_RESULTS;
    }
    public EbayDriver(String tag, GamePageFragment.IProcess mProcess,String console) {
        this.titles = new ArrayList<>();
        this.maxResults = MAX_RESULTS;
        this.tag = tag;
        this.mProcess = mProcess;
        this.console = console;
    }
    @Override
    protected Void doInBackground(Void... voids) {
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
        }        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        //Do All UI Changes HERE
        super.onPostExecute(aVoid);
        mProcess.updateAdapter();

    }
    public EbayDriver(int maxResults,String console) {
        this.titles = new ArrayList<>();

        this.maxResults = maxResults;
        this.console = console;
    }


    public void runDriver(String tag) {
        this.tag = tag;
        this.execute();
    }

    private String createAddress(String tag) {
        tag = tag+" "+this.console;
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
        XPathExpression ackExpression = xpath.compile("//findItemsAdvancedResponse/ack");
        XPathExpression itemExpression = xpath.compile("//findItemsAdvancedResponse/searchResult/item");

        String ackToken = (String) ackExpression.evaluate(doc, XPathConstants.STRING);
        Log.d("ACK from ebay API :: ", ackToken);
        if (!ackToken.equals("Success")) {
            throw new Exception(" service returned an error");
        }

        NodeList nodes = (NodeList) itemExpression.evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            EbayTitle tmp = new EbayTitle();
            Node node = nodes.item(i);

            String itemId = (String) xpath.evaluate("itemId", node, XPathConstants.STRING);
            String title = (String) xpath.evaluate("title", node, XPathConstants.STRING);
            String itemUrl = (String) xpath.evaluate("viewItemURL", node, XPathConstants.STRING);
            String galleryUrl = (String) xpath.evaluate("galleryURL", node, XPathConstants.STRING);
            String currentPrice = (String) xpath.evaluate("sellingStatus/currentPrice", node, XPathConstants.STRING);

            tmp.setCurrentPrice(currentPrice);
            tmp.setGalleryUrl(galleryUrl);
            tmp.setTitle(title);
            tmp.setItemId(itemId);
            tmp.setItemUrl(itemUrl);
            titles.add(tmp);

            Log.d("currentPrice", currentPrice);
            Log.d("itemId", itemId);
            Log.d("title", title);
            Log.d("galleryUrl", galleryUrl);

        }

        is.close();

    }
    public List<EbayTitle> getTitles(){return this.titles;}

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

}
