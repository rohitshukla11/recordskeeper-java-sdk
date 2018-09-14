package RecordsKeeperJavaLib.Test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;
import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import RecordsKeeperJavaLib.Config;
import RecordsKeeperJavaLib.transactions;


public class TransactionsTest {

    public Properties prop;
    public String validaddress;
    public String dumptxhex;
    public String miningaddress;
    public String testdata;
    public String privatekey;
    public double amount;
    public String dumpsignedtxhex;
    public String dumptxid;
	
	
	public boolean getPropert() throws IOException {

        prop = new Properties();

        String path = "config.properties";
        File file = new File(path);
        if (file.exists()) {
            FileInputStream fs = new FileInputStream(path);
            prop.load(fs);
            fs.close();
            return true;
        } else {
            return false;
        }
    }
	
	    

	    public TransactionsTest() throws IOException {
	
         if (getPropert() == true) {
            validaddress = prop.getProperty("validaddress");
            miningaddress = prop.getProperty("miningaddress");
            amount = Double.parseDouble(Config.getProperty("amount"));
            testdata = prop.getProperty("testdata");
            dumptxhex = prop.getProperty("dumptxhex");
            privatekey = prop.getProperty("privatekey");
            dumpsignedtxhex = prop.getProperty("dumpsignedtxhex");
            dumptxid = prop.getProperty("dumptxid");
        } else {
            validaddress = System.getenv("validaddress");
            miningaddress = System.getenv("miningaddress");
            amount = Double.parseDouble(System.getenv("amount"));
            testdata = System.getenv("testdata");
            dumptxhex = System.getenv("dumptxhex");
            privatekey = System.getenv("privatekey");
            dumpsignedtxhex = System.getenv("dumpsignedtxhex");
            dumptxid = System.getenv("dumptxid");
        }
		    
		    
		    
		    
		    
		    
	    }


	    @Test
	    public void sendTransaction() throws Exception {

	        byte[] bytes = "hello".getBytes("UTF-8");
	        BigInteger bigInt = new BigInteger(bytes);
	        String hexString = bigInt.toString(16);

	        String txid = transactions.sendTransaction(miningaddress, validaddress, hexString, 0.2);
	        int tx_size = txid.length();
	        assertEquals(tx_size, 64);
	    }

	    @Test
	    public void createRawTransaction() throws IOException, JSONException {

	        byte[] bytes = "hello".getBytes("UTF-8");
	        BigInteger bigInt = new BigInteger(bytes);
	        String hexString = bigInt.toString(16);

	        String txhex = transactions.createRawTransaction(miningaddress, validaddress, amount, hexString);
	        int tx_size = txhex.length();
	        //System.out.println(tx_size);
	        assertEquals(tx_size, 280);

	    }

	    @Test
	    public void signrawtransaction() throws IOException, JSONException {

	        String txhex = transactions.signRawTransaction(dumptxhex, privatekey);
	        int tx_size = txhex.length();
	        //System.out.println(tx_size);
	        assertEquals(tx_size, 268);
	    }

	    @Test
	    public void sendrawtransaction() throws IOException, JSONException {

	        String txid = transactions.sendRawTransaction("0100000001fd52d9622262c0d491a93bd14b0190362b3d1c362a429750411a255bfd83e91a000000006b483045022100d05595c9a60b3ee0d9ae6479a6f7be64a6fccf993bbfba1135fb1d68d873e41a02202fb4754f8447e0da862e0637183853640a4a0758f4dc0b42315d0d6332d2c2f90121038c1d7be91850add595b238c541d17cfa6e6d780a410bb7db4930a982cad933d4ffffffff03005a6202000000001976a914b2bc8a974aa0b9c4ad82ac0a7017160df0751f5888ac0000000000000000066a0474657374a0e61239000000001976a9145f2976565b53d4ed013b6131e98201e89787518688ac00000000");
	       
	        assertEquals(txid, "transaction already in block chain");
	    }

	       @Test
	    public void sendsignedtransaction() throws IOException, JSONException {

	        byte[] bytes = "hello".getBytes("UTF-8");
	        BigInteger bigInt = new BigInteger(bytes);
	        String hexString = bigInt.toString(16);

	        String txid = transactions.sendSignedTransaction(miningaddress, validaddress, amount, privatekey, hexString);
	        int tx_size = txid.length();
	        assertEquals(tx_size, 64);
	    }

               @Test
	        public void retrievetransaction() throws IOException, JSONException {
	        JSONObject res= transactions.retrieveTransaction(dumptxid);
	        System.out.println(res);
	        String sentdata = res.getString("sent_data");
	        assertEquals(sentdata, "hellodata");

	        double sentamount = res.getDouble("sent_amount");
	        assertNotEquals(sentamount, 0);
	    }
	            
	           
	    @Test
	    public void getfee() throws IOException, JSONException {
	        double fees = transactions.getFee(miningaddress, "4b1fbf9fb1e5c93cfee2d37ddc5fef444da0a05cc9354a834dc7155ff861a5e0");
	       System.out.println(fees);
	        assertEquals(fees, 0.0269, 0.001);
	    }
	
}