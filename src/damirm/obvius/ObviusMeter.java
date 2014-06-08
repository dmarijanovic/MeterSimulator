package damirm.obvius;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import damirm.Meter;

public class ObviusMeter extends Meter {

    private List<ObviusPuls> pulsList = new ArrayList<ObviusPuls>();

    private ObviusPuls mLastPuls;

    public ObviusMeter() {
        mLastPuls = new ObviusPuls();
        mLastPuls.setChannel(new BigDecimal(100), 0);
        mLastPuls.setChannel(new BigDecimal(200), 1);
        mLastPuls.setChannel(new BigDecimal(300), 2);
        mLastPuls.setChannel(new BigDecimal(400), 3);
    }

    @Override
    public void makePulse() {
        ObviusPuls newPuls = new ObviusPuls();
        newPuls.setChannel(mLastPuls.chennels[0].input.add(new BigDecimal(randInt(10, 15))), 0);
        newPuls.setChannel(mLastPuls.chennels[1].input.add(new BigDecimal(randInt(10, 15))), 1);
        newPuls.setChannel(mLastPuls.chennels[2].input.add(new BigDecimal(randInt(10, 15))), 2);
        newPuls.setChannel(mLastPuls.chennels[3].input.add(new BigDecimal(randInt(10, 15))), 3);
        pulsList.add(newPuls);
        mLastPuls = newPuls;
    }

    @Override
    public void sendToServer() {
        if (queueSize() < 5) {
            return;
        }
        
        System.out.println("Sending csv. Buff size " + queueSize());

        gzipList();
        post();
    }
    
    @Override
    public int queueSize() {
        return pulsList.size();
    };
    
    private void post() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost("http://localhost:9000/obvius/");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //builder.addTextBody("LOGFILE", body, ContentType.TEXT_PLAIN);
        builder.addTextBody("MODE", "LOGFILEUPLOAD", ContentType.TEXT_PLAIN);
        builder.addTextBody("SERIALNUMBER", "1338", ContentType.TEXT_PLAIN);
        builder.addTextBody("MODBUSDEVICE", "13380", ContentType.TEXT_PLAIN);
        builder.addBinaryBody("LOGFILE", new File("data/out2.gz"), ContentType.APPLICATION_OCTET_STREAM, "file.ext");
        HttpEntity multipart = builder.build();

        uploadFile.setEntity(multipart);

        try {
            CloseableHttpResponse response=  httpClient.execute(uploadFile);
            if (response.getStatusLine().getStatusCode() == 200) {
                pulsList.clear();
            } else {
                System.out.println("Error sending with status code " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Error sending " + e.getMessage());
        }
        //responseEntity = response.getEntity();
    }
    
    public void gzipFile() {
        GZIPOutputStream gos = null;

        try {
            File myGzipFile = new File("data/out2.gz");

            //InputStream is = new ByteArrayInputStream(str.getBytes());
            InputStream is = new FileInputStream(new File("data/out.csv"));
            gos = new GZIPOutputStream(new FileOutputStream(myGzipFile));

            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                gos.write(buffer, 0, len);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { gos.close(); } catch (IOException e) { }
        }
    }
    
    public void gzipList() {
        GZIPOutputStream gos = null;
        
        try {
            StringBuilder sb = new StringBuilder();
            for (ObviusPuls puls : pulsList) {
                sb.append(puls.getCSV());
                sb.append("\n");
            }
            
            File myGzipFile = new File("data/out2.gz");
            
            InputStream is = new ByteArrayInputStream(sb.toString().getBytes());

            gos = new GZIPOutputStream(new FileOutputStream(myGzipFile));
            
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                gos.write(buffer, 0, len);
            }
            is.close();
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { gos.close(); } catch (IOException e) { }
        }
    }
}
