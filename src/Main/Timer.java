package Main;

import javafx.application.Application;
import org.ini4j.Wini;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static Main.GlobalMembers.DisplayImageOnForm;
import static Main.GlobalMembers.objLksdbIni;

public class Timer implements Runnable {

    private int iMsgType;
    private int RotateCount;
    private InputStream input;
    private OutputStream output;
    private boolean bTransactionDataReceived;
    private boolean bIsCheckingBCOrientation = false;
    private static final String dirPath;

        static {
            dirPath = System.getProperty("user.dir");
        }
    
    @Override
    public void run() {
        while (true) {
            if (!GlobalMembers.bThreadProtection) {
                try {
                    if(Main.client != null){
                        output = new DataOutputStream(Main.client.getOutputStream());
                        input = new DataInputStream(Main.client.getInputStream());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                switch (GlobalMembers.objTaskState) {
                    case TransactionStart: {
                        try {
                            GlobalMembers.bThreadProtection = true;
                            iMsgType = 0;

//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\F.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\F.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R_Bottom.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R_Bottom.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R_Top.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R_Top.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\RBright_70.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\RBright_70.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\RBright_50.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\RBright_50.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\RBright_30.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\RBright_30.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R_Top_50.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R_Top_50.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\RBright.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\RBright.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\Reardown.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\Reardown.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\Rearup.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\Rearup.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R_Left.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R_Left.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R_Right.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R_Right.bmp");
//
//                        if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\Front.bmp"))
//                            File.Delete(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\Front.bmp");


//                        lblText.Visible = false;
//                        lblTime.Visible = false;
//                        lblAppVersion.Visible = false;

                            GlobalMembers.objClientConfigIni.put("Kiosk_Details", "TxnFlow", "0");
                            GlobalMembers.txtLabel.setVisible(false);
                            GlobalMembers.time.setVisible(false);
                            GlobalMembers.headingLabel.setVisible(false);
                            GlobalMembers.imgLabel.setIcon(GlobalMembers.DisplayImage("K002"));
                            RotateCount = 0;
                            GlobalMembers.bIsNewTransaction = true;
                            GlobalMembers.strTxnRRN = "";
                            bIsCheckingBCOrientation = false;
                            GlobalMembers.bMoreData = false;
                            GlobalMembers.bNextRequest = false;
                            bTransactionDataReceived = false;

                            //if Passbook is Demo, used for debugging code without printer
                            if (GlobalMembers.bIsPassbookDemo) {
                                Log.Write("***************TRANSACTION SESSION STARTED IN DEMO*******************");
                                Log.Write("Transaction started in demo");

                                //try read dummy accno for demo purpose
                                GlobalMembers.AccountNumber = objLksdbIni.get("DEMO", "Dummy_Acc_No");
                                GlobalMembers.bThreadProtection = false;
                                GlobalMembers.objTaskState = GlobalMembers.Task.AuthenticationRequest;
                            } else {
//                            if (GlobalMembers.bIsPassbookEpson == false)
//                                Ollivtti_Obj.StartPassbookCheck();
//                            else
//                                EPSON_Obj.StartPassbookCheck();
                            }
                        } catch (Exception ex) {
                            Log.Write("Exception in WndProc->TransactionStart: " + ex.getMessage());
                            GlobalMembers.bThreadProtection = false;
//                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionEnd;
                        }
                    }
                    break;
                    case AuthenticationRequest: {
                        try {
                            GlobalMembers.bThreadProtection = true;

                            GlobalMembers.DisplayImage("K004");

                            Log.Write("Authentication request");
                            GlobalMembers.ResetISOFields();

                            UpdateSTAN();

//                            Array.Resize(ref strTxnLine, 0);
//                            Array.Resize(ref strTxnPostedDateACK, 0);
//                            Array.Resize(ref strEndBalanceACK, 0);
//                            Array.Resize(ref strTransIdACK, 0);
//                            Array.Resize(ref strDateACK, 0);
//                            Array.Resize(ref strTransSerialNoACK, 0);

                            GlobalMembers.strTotalLinesPrinted = "0";
                            GlobalMembers.strTotalLinesToBePrinted = "0";
                            GlobalMembers.strLinesToPrint = "00";
                            GlobalMembers.objPBAckDetails.strLastLinePrinted = "00";

                            if (GlobalMembers.AccountNumber.length() != GlobalMembers.objACCOUNT_DETAILS.iAccount_Length) {
                                Log.Write("AUTHENTICATION REQUEST BARCODE DATA LEN IS NOT OK ");
                                GlobalMembers.bThreadProtection = false;
                                GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                            } else {
                                // ISOMessage Compose
                                iMsgType = 1;   //For authentication
                                GlobalMembers.strDefMTI = "1200";
                                GlobalMembers.objISO[2].strValue = GlobalMembers.AccountNumber;
                                GlobalMembers.objISO[3].strValue = "110000";
                                GlobalMembers.objISO[4].strValue = "000000000000";
                                GlobalMembers.objISO[11].strValue = GlobalMembers.STAN;
                                GlobalMembers.Last_STAN = GlobalMembers.STAN;
                                GlobalMembers.objISO[12].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));  //DateTime.Now.ToString("yyyyMMddHHmm");
                                GlobalMembers.objISO[17].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));   //DateTime.Now.ToString("MMdd");
                                GlobalMembers.objISO[24].strValue = "200";
                                GlobalMembers.objISO[32].strValue = "888888";
                                GlobalMembers.objISO[35].strValue = "9619531053";

                                //16 digit Reference No (12 + 4)
                                GlobalMembers.objISO[37].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmmss" + GlobalMembers.STAN.substring(2))); //DateTime.Now.ToString("ddMMyyHHmmss") + GlobalMembers.STAN.substring(2);

                                GlobalMembers.objISO[41].strValue = "10000000";
                                GlobalMembers.objISO[43].strValue = "CEDGETHANE-WEST0000000000000000000MHIN00";
                                GlobalMembers.objISO[49].strValue = "356";
                                GlobalMembers.objISO[102].strValue = GlobalMembers.AccountNumber;
                                GlobalMembers.objISO[123].strValue = "SWT";
                                GlobalMembers.objISO[124].strValue = "KSK";
                                GlobalMembers.objISO[126].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy yyyy")); //DateTime.Now.ToString("ddMMyy yyyy");

                                Log.Write("Authentication Request", "MSGComm");
                                for (int iIterator = 2; iIterator < 128; iIterator++) {
                                    if (!GlobalMembers.objISO[iIterator].strValue.equals(""))
                                        Log.Write("Field " + iIterator + " - " + GlobalMembers.objISO[iIterator].strValue, "MSGComm");
                                }

                                //Compose ISO message, return error message if it fails
                                if (ISOMsgParser.ComposeISO8583Message(GlobalMembers.bRequest)) {

                                    //if TCP connection
                                    if (GlobalMembers.objCBS.bIs_TCP) {
                                        try {
                                            Log.Write("Connection establish with CBS");

                                            Log.Write("AUTHENTICATION REQUEST SENT TO CBS AND STAN IS " + GlobalMembers.STAN);

                                            //send request by tcp
                                            output.write(GlobalMembers.bRequest);
                                            output.flush();
                                            GlobalMembers.bThreadProtection = false;
                                            Log.Write("AuthenticationRequest message sent", "MSGComm");
                                            Log.Write(GlobalMembers.bRequest, "MSGComm");

                                            //Receive response by tcp
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            byte[] buffer = new byte[293];
                                            baos.write(buffer, 0, input.read(buffer));

                                            GlobalMembers.bResponse = baos.toByteArray();//ref bResponse
//                                            String res = Arrays.toString(GlobalMembers.bResposne);
//                                            System.out.println(res);
                                            GlobalMembers.objTaskState = GlobalMembers.Task.AuthenticationResponse;

                                        } catch (Exception ex) {
                                            Log.Write("AuthenticationRequest not sent(TCP): " + ex.getMessage(), "MSGComm");
                                            Log.Write("AUTHENTICATION REQUEST NOT SENT TO CBS VIA TCP AND STAN IS " + GlobalMembers.STAN + "  " + ex.getMessage());
                                            GlobalMembers.bThreadProtection = false;
//                                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionEnd;
                                        }
                                    }
                                } else {
                                    Log.Write("AuthenticationRequest message format error " , "MSGComm");
                                    GlobalMembers.bThreadProtection = false;
//                                    GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.Write("Exception in WndProc->AuthenticationRequest: " + ex.getMessage(), "MSGComm");
                            GlobalMembers.bThreadProtection = false;
//                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionEnd;
                        }
                    }
                    break;
                    case AuthenticationResponse: {
                        try {
                            GlobalMembers.bThreadProtection = true;
//                        timer2.Enabled = false;
                            Log.Write("Authentication response");
                            Log.Write("AuthenticationResponse message received", "MSGComm");
                            Log.Write(GlobalMembers.bResponse, "MSGComm");

                            //load for DEMO purpose to match the STAN or not, read here so that setting immediately applied from next transaction.
                            if (objLksdbIni != null)
                                GlobalMembers.bIsSTANMatch = (!objLksdbIni.get("DEMO", "Check_STAN").toLowerCase().equals("no"));

                            for (int i = 0; i <= 128; i++) {
                                GlobalMembers.objISO[i].strValue = "";
                            }

                            //if reply msg filter properly
                            if (ISOMsgParser.FilterISO8583ResponseMessage()) {
                                Log.Write("AuthenticationResponse message", "MSGComm");
                                for (int iIterator = 2; iIterator < 128; iIterator++) {
                                    if (!GlobalMembers.objISO[iIterator].strValue.equals("")) {
                                        Log.Write("Field " + iIterator + " - " + GlobalMembers.objISO[iIterator].strValue, "MSGComm");
                                    }
                                }

                                Log.Write("AUTHENTICATION RESPONSE COMES FROM CBS AND STAN IS " + GlobalMembers.objISO[11].strValue);

                                //if status is 000
                                if (GlobalMembers.objISO[39].strValue != null && GlobalMembers.objISO[39].strValue.equals("000")) {
                                    //if field 125 contains error
                                    if (GlobalMembers.objISO[125].strValue.contains("Error")) {
                                        GlobalMembers.bThreadProtection = false;
                                        //send error no by ading 2000 to it
//                                        GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                                    }
                                    //is STAN to be matched then is it matched
                                    else if (!GlobalMembers.bIsSTANMatch || GlobalMembers.Last_STAN.equals(GlobalMembers.objISO[11].strValue)) {
                                        //update the STAN
                                        UpdateSTAN();
                                        GlobalMembers.bThreadProtection = false;
                                        //send data request
                                        GlobalMembers.objTaskState = GlobalMembers.Task.PassbookDataRequest;
                                    } else {
                                        GlobalMembers.bThreadProtection = false;
                                        //STAN mismatch error
//                                        GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                                    }
                                } else {
                                    //update the STAN
                                    UpdateSTAN();
                                    Log.Write("AUTHENTICATION RESPONSE COMES WITH ERROR FROM CBS, ERROR IS " + GlobalMembers.objISO[39].strValue);
                                    GlobalMembers.bThreadProtection = false;
                                    //show error came
//                                    GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                                }
                            } else {
                                GlobalMembers.objTxnIni.put("PBTxn_Details", "Result", GlobalMembers.objISO[39].strValue);
                                GlobalMembers.objTxnIni.put("PBTxn_Details", "Changes_DonePB", "1");

                                //show msg format error
                                Log.Write("AuthenticationResponse message format error" , "MSGComm");
                                GlobalMembers.bThreadProtection = false;
//                                GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                            }
                        } catch (Exception ex) {
                            Log.Write("Exception in WndProc->AuthenticationResponse: " + ex.getMessage(), "MSGComm");
                            GlobalMembers.bThreadProtection = false;
//                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                        }
                    }
                    break;
                    case PassbookDataRequest: {
                        try {
                            GlobalMembers.bThreadProtection = true;

                            GlobalMembers.bMoreData = false;
                            GlobalMembers.bNextRequest = false;
                            GlobalMembers.isCoverPage = false;


                            Log.Write("Passbook Data request");

                            GlobalMembers.ResetISOFields();

                            // ISOMessage Compose
                            iMsgType = 3;   //For data rqst
                            GlobalMembers.strDefMTI = "1200";
                            GlobalMembers.objISO[2].strValue = GlobalMembers.AccountNumber;
                            GlobalMembers.objISO[3].strValue = "920000";
                            GlobalMembers.objISO[4].strValue = "000000000000";

                            GlobalMembers.objISO[11].strValue = GlobalMembers.STAN;
                            GlobalMembers.Last_STAN = GlobalMembers.STAN;
                            GlobalMembers.objISO[12].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")); //DateTime.Now.ToString("yyyyMMddHHmm");
                            GlobalMembers.objISO[17].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd")); //DateTime.Now.ToString("MMdd");
                            GlobalMembers.objISO[24].strValue = "200";
                            GlobalMembers.objISO[32].strValue = "888888";
                            GlobalMembers.objISO[35].strValue = "9619531053";

                            //16 digit Reference No (12 + 4)
                            if (GlobalMembers.strTxnRRN.equals("")) {
                                GlobalMembers.objISO[37].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmmss" + GlobalMembers.STAN.substring(2)));  //DateTime.Now.ToString("ddMMyyHHmmss") + GlobalMembers.STAN.subSString(2);
                                GlobalMembers.strTxnRRN = GlobalMembers.objISO[37].strValue;
                            } else {
                                GlobalMembers.objISO[37].strValue = GlobalMembers.strTxnRRN;
                            }

                            GlobalMembers.objISO[41].strValue = "10000000";
                            GlobalMembers.objISO[43].strValue = "CEDGETHANE-WEST0000000000000000000MHIN00";
                            GlobalMembers.objISO[49].strValue = "356";
                            //GlobalMembers.objISO[60].strValue = "0153TES1+000";
                            GlobalMembers.objISO[102].strValue = GlobalMembers.AccountNumber;
                            GlobalMembers.objISO[123].strValue = "SWT";
                            GlobalMembers.objISO[124].strValue = "KSK";
                            GlobalMembers.objISO[126].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy yyyy")); //DateTime.Now.ToString("ddMMyy yyyy");

                            Log.Write("PassbookDataRequest message", "MSGComm");
                            for (int iIterator = 2; iIterator < 128; iIterator++) {
                                if (!GlobalMembers.objISO[iIterator].strValue.equals("")) {
                                    Log.Write("Field " + iIterator + " - " + GlobalMembers.objISO[iIterator].strValue, "MSGComm");
                                }
                            }

                            if (ISOMsgParser.ComposeISO8583Message(GlobalMembers.bRequest)) {
                                if (GlobalMembers.objCBS.bIs_TCP) {
                                    try {
                                        Log.Write("Connection establish with CBS");

                                        Log.Write("PASSBOOK DATA REQUEST SENT TO CBS AND STAN IS " + GlobalMembers.STAN);

                                        Log.Write("PassbookDataRequest message sent", "MSGComm");
                                        Log.Write(GlobalMembers.bRequest, "MSGComm");

                                        //Send request
                                        output.write(GlobalMembers.bRequest);
                                        output.flush();

                                        //Receive response
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        byte[] buffer = new byte[293];
                                        baos.write(buffer, 0, input.read(buffer));

                                        GlobalMembers.bResponse = baos.toByteArray();//ref bResponse
//                                            String res = Arrays.toString(GlobalMembers.bResposne);
//                                            System.out.println(res);
                                        GlobalMembers.objTaskState = GlobalMembers.Task.PassbookDataResponse;

                                    } catch (Exception ex) {
                                        Log.Write("PassbookDataRequest not sent: " + ex.getMessage(), "MSGComm");
                                        Log.Write("PASSBOOK DATA REQUEST NOT SENT TO CBS AND STAN IS " + GlobalMembers.STAN + "  " + ex.getMessage());
                                        GlobalMembers.bThreadProtection = false;
                                        GlobalMembers.objTaskState = GlobalMembers.Task.TransactionEnd;
                                    }
                                }
                            } else {
                                Log.Write("PassbookDataRequest message format error- ", "MSGComm");
                                GlobalMembers.bThreadProtection = false;
//                                GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                            }
                        } catch (Exception ex) {
                            Log.Write("Exception in WndProc->PassbookDataRequest: " + ex.getMessage());
                            GlobalMembers.bThreadProtection = false;
//                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionEnd;
                        }
                    }
                    break;
                    case PassbookDataResponse: {
                        try {
                            GlobalMembers.bThreadProtection = true;
                            GlobalMembers.isCoverPage = false;

                            Log.Write("Passbook Data response");
                            Log.Write("PassbookDataResponse message received", "MSGComm");
                            Log.Write(GlobalMembers.bResponse, "MSGComm");

                            for (int i = 0; i <= 128; i++) {
                                GlobalMembers.objISO[i].strValue = "";
                            }

                            if (ISOMsgParser.FilterISO8583ResponseMessage()) {
                                Log.Write("PassbookDataResponse message", "MSGComm");
                                for (int iIterator = 2; iIterator < 128; iIterator++) {
                                    if (!GlobalMembers.objISO[iIterator].strValue.equals("")) {
                                        Log.Write("Field " + iIterator + " - " + GlobalMembers.objISO[iIterator].strValue, "MSGComm");
                                    }
                                }

                                Log.Write("PASSBOOK DATA RESPONSE COMES FROM CBS AND STAN IS " + GlobalMembers.objISO[11].strValue);

                                if (GlobalMembers.objISO[39].strValue != null && GlobalMembers.objISO[39].strValue.equals("000")) {
                                    if (!GlobalMembers.bIsSTANMatch || GlobalMembers.Last_STAN.equals(GlobalMembers.objISO[11].strValue)) {
                                        UpdateSTAN();

                                        if (GlobalMembers.objISO[125].strValue.equals("N00")) {
                                            Log.Write("No more pending txns in N00 case");

                                            GlobalMembers.bThreadProtection = false;
//                                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionEnd;
                                        } else {
                                            GlobalMembers.DisplayImage("K005");
                                            Log.Write("Before Compose Print Data");
                                            bTransactionDataReceived = true;

                                            String strBuffer = "";
//                                            if (ComposeAndPrintData(true,  strBuffer)) {
//                                                if (GlobalMembers.bIsPassbookEpson == false)
//                                                    Ollivtti_Obj.PrintData(strBuffer);
//                                                else
//                                                    EPSON_Obj.PrintData(strBuffer);
//
//                                                Log.Write("After Compose Print Data, set bIsPositiveACK = true");
//                                                GlobalMembers.bIsPositiveACK = true;
//                                            } else {
//                                                Log.Write("After Compose Print Data, set bIsPositiveACK = false");
//                                                GlobalMembers.bIsPositiveACK = false;
//                                                GlobalMembers.bThreadProtection = false;
////                                                GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
//                                            }
                                        }
                                    } else {
                                        GlobalMembers.bThreadProtection = false;
//                                        GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                                    }
                                } else {
                                    UpdateSTAN();

                                    GlobalMembers.objTxnIni.put("PBTxn_Details", "Result", GlobalMembers.objISO[39].strValue);
                                    GlobalMembers.objTxnIni.put("PBTxn_Details", "Changes_DonePB", "1");

                                    Log.Write("PASSBOOK DATA RESPONSE COMES WITH ERROR FROM CBS, ERROR IS " + GlobalMembers.objISO[39].strValue);
                                    GlobalMembers.bThreadProtection = false;
//                                    GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                                }
                            } else {
                                Log.Write("PassbookDataResponse message format error " , "MSGComm");
                                GlobalMembers.bThreadProtection = false;
//                                GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                            }
                        } catch (Exception ex) {
                            Log.Write("Exception in WndProc->PassbookDataResponse: " + ex.getMessage());
                            GlobalMembers.bThreadProtection = false;
//                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
                        }
                    }
                    break;
                    case TransactionEnd: {
                        try {
                            GlobalMembers.bThreadProtection = true;
//                        HeartBeat.Enabled = true;

//                        if (m.WParam.ToInt32() == 1) {
//                            DisplayImageonForm("K021", "", true);
//                        } else if (m.WParam.ToInt32() == 2) {
//                            DisplayImageonForm("K008", "", true);
//                            Log.Write("Transaction end CoverPage");
//                        } else if (m.WParam.ToInt32() == 9) //No connectivity at the moment
//                        {
//                            DisplayImageonForm("K017", "", true);
//                            Log.Write("Transaction end, because Server connection loss");
//                        } else if (m.WParam.ToInt32() == 4 || m.WParam.ToInt32() == 5 || m.WParam.ToInt32() == 6) //Passbook Error
//                        {
//                            Log.Write("Transaction end");
//                        } else if (m.WParam.ToInt32() == 7)   //Transaction Error
//                        {
//                            Log.Write("Transaction end");
//                        } else {
//                            DisplayImageonForm("K006", "", true);
//                            Log.Write("Transaction end");
//                        }

                            //For DEMO PB, eject not need to be called, Eject called when PB is live only
//                        if (!GlobalMembers.bIsPassbookDemo) {
//                            if (GlobalMembers.bIsPassbookEpson == false)
//                                Ollivtti_Obj.EjectPassbook();
//                            else
//                                EPSON_Obj.EjectPassbook();
//                        }

                            Log.Write("PASSBOOK EJECTED");
//                        Application.DoEvents();
                            Log.Write("***************TRANSACTION SESSION ENDS*******************");
//                        Log.Write("***************TRANSACTION SESSION ENDS*******************", "MSGComm");

                            GlobalMembers.bThreadProtection = false;
                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionStart;

                        } catch (Exception ex) {
                            Log.Write("Exception in WndProc->TransactionEnd: " + ex.getMessage());
                        }
                    }
                    break;
                    case RequestTimeout: {
                        try {
                            GlobalMembers.bThreadProtection = true;
//                        timer2.Enabled = false;
//                        timer2.Interval = 30000;
                            Log.Write("TRANSACTION REQUEST TIMEOUT ");

                            GlobalMembers.bThreadProtection = false;
                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionEnd;
//                        PostMessage(GlobalMembers.MainHandle, TransactionEnd, (IntPtr) 9, IntPtr.Zero);
                        } catch (Exception ex) {
                            Log.Write("Exception in WndProc->RequestTimeout: " + ex.getMessage());
                        }
                    }
                    break;
                    case TransactionError: {
//                        try
//                        {
//                            GlobalMembers.bThreadProtection = true;   //  added in V260.1.1.13  9 dec 2015
//
//                            GlobalMembers.WriteLog("Txn error found");
//                            switch (Convert.ToInt32(m.LParam.ToString()))
//                            {
//                                case 1002: // Barcode Lenght is Not OK
//                                {
//                                    DisplayImageonForm("K009", "", true);
//                                    GlobalMembers.WriteLog("Barcode read fails", "MSGComm");
//                                    GlobalMembers.WriteLog("BARCODE READ FAILS");
//                                }
//                                break;
//                                case 1003: // Barcode Re-Read and Verify Fails (Account Number Mismatch)
//                                {
//                                    DisplayImageonForm("K011", "", true);
//                                    GlobalMembers.WriteLog("Barcode verification fails", "MSGComm");
//                                    GlobalMembers.WriteLog("BARCODE VERIFICATION FAILS");
//                                }
//                                break;
//                                case 1004: // Passbook Jammed in Printer
//                                {
//                                    DisplayImageonForm("K011","",true);
//                                    GlobalMembers.WriteLog("Passbook jammed", "MSGComm");
//                                    GlobalMembers.WriteLog("PASSBOOK JAMMED");
//                                }
//                                break;
//                                case 1005: //STAN Mismatch
//                                {
//                                    DisplayImageonForm("K007", "Error-MESSAGE CO-ORDINATION STAN MISMATCH",true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "16");
//                                    GlobalMembers.WriteLog("Message co-ordination stan mismatch", "MSGComm");
//                                    GlobalMembers.WriteLog("MESSAGE CO-ORDINATION STAN MISMATCH");
//                                }
//                                break;
//                                case 1006: //Msg Format Error
//                                {
//                                    DisplayImageonForm("K007", "Error-MESSAGE FORMAT",true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "08");
//                                    GlobalMembers.WriteLog("Message format error", "MSGComm");
//                                    GlobalMembers.WriteLog("MESSAGE FORMAT ERROR");
//                                }
//                                break;
//
//                                case 169: //114(ACCOUNT HELD)
//                                {
//                                    DisplayImageonForm("K007", "Err - 169(Invalid Account Number)", true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Err - 169(Invalid Account Number)", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - 169(Invalid Account Number)");
//                                }
//                                break;
//
//                                case 114: //114(ACCOUNT HELD)
//                                {
//                                    DisplayImageonForm("K007", "Err - 114(ACCOUNT HELD)", true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Err - 114(ACCOUNT HELD)", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - 114(ACCOUNT HELD)");
//                                }
//                                break;
//
//                                case 911: //911(Timed out)
//                                {
//                                    DisplayImageonForm("K007", "Err - 911(Time out)", true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Err - 911(Time out)", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - 911(Time out)");
//                                }
//                                break;
//
//                                case 188://188(No record found)
//                                {
//                                    DisplayImageonForm("K007", "Err - 188(No record found)", true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Err - 188(No record found)", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - 188(No record found)");
//                                }
//                                break;
//
//                                case 909: //909(sys malfunction)
//                                {
//                                    DisplayImageonForm("K007", "Err - 909(System malfunction)", true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Err - 909(System malfunction)", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - 909(System malfunction)");
//                                }
//                                break;
//
//                                case 913: //913(duplicate transmission)
//                                {
//                                    DisplayImageonForm("K007", "Err - 913(Duplicate transmission)", true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Err - 913(duplicate transmission)", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - 913(duplicate transmission)");
//                                }
//                                break;
//
//                                case 121: //121(Exceeds WDL amt limit))
//                                {
//                                    DisplayImageonForm("K007", "Err - 121(exceeds WDL amt limit)", true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Err - 121(exceeds WDL amt limit)", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - 121(exceeds WDL amt limit)");
//                                }
//                                break;
//
//                                case 902: //902(Account Closed)
//                                {
//                                    DisplayImageonForm("K007", "Err - 902(Invalid correction)", true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Err - 902(Invalid correction)", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - 902(Invalid correction)");
//                                }
//                                break;
//
//                                case 100: //100(Account Closed)
//                                {
//                                    DisplayImageonForm("K007", "Err - 100(Account Closed)", true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Err - 100(Account Closed)", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - 100(Account Closed)");
//                                }
//                                break;
//
//                                case 119: //119(this transaction not permitted to Account holder)
//                                {
//                                    DisplayImageonForm("K007", "Err - 119(This transaction not permitted to Account holder)", true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Err - 119(This transaction not permitted to Account holder)", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - 119(This transaction not permitted to Account holder)");
//                                }
//                                break;
//
//                                case 116: //116(Insufficent funds)
//                                {
//                                    DisplayImageonForm("K007", "Err - 116(Insufficent funds)", true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Err - 116(Insufficent funds)", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - 116(Insufficent funds)");
//                                }
//                                break;
//                                case 855: //N00 Condition
//                                {
//                                    DisplayImageonForm("K012","",true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Customer have no TXN to print(N00)", "MSGComm");
//                                    GlobalMembers.WriteLog("CUSTOMER HAVE NO TXN TO PRINT(N00)");
//                                }
//                                break;
//                                case 1012:
//                                {
//                                    DisplayImageonForm("K013","",true);
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Error in ComposeandPrint", "MSGComm");
//                                    GlobalMembers.WriteLog("Error in ComposeandPrint");
//                                }
//                                break;
//                                case 2001: // Last print detail not found
//                                {
//                                    DisplayImageonForm("K011");
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "11");
//                                    GlobalMembers.WriteLog("Error001, Last print detail not found", "MSGComm");
//                                    GlobalMembers.WriteLog("LAST PRINT DETAIL NOT FOUND");
//                                }
//                                break;
//                                case 2002: //error due to first time printing
//                                {
//                                    DisplayImageonForm("K022");
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Error002, Error due to first time printing", "MSGComm");
//                                    GlobalMembers.WriteLog("ERROR DUE TO FIRST TIME PRINTING");
//                                }
//                                break;
//                                case 2003: //Error capturing last print details
//                                {
//                                    DisplayImageonForm("K023");
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "12");
//                                    GlobalMembers.WriteLog("Error003, Error capturing last print details", "MSGComm");
//                                    GlobalMembers.WriteLog("ERROR CAPTURING LAST PRINT DETAILS");
//                                }
//                                break;
//                                case 2004: //get line number 0 in last print details
//                                {
//                                    DisplayImageonForm("K048");
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "00");
//                                    GlobalMembers.WriteLog("Error048, CBS send last printed Line no. 0, so terminate txn.", "MSGComm");
//                                    GlobalMembers.WriteLog("ERROR 048 LAST PRINTED LINE IS 0 IN LAST PRINT DETAILS");
//                                }
//                                break;
//                                case 2005: //Print Failed
//                                {
//                                    DisplayImageonForm("K007", "Err - Print Failed", true);
//                                    GlobalMembers.WriteLog("Err - Print Failed", "MSGComm");
//                                    GlobalMembers.WriteLog("Err - Print Failed");
//                                }
//                                break;
//                                default: //CBS Error Code
//                                {
//                                    DisplayImageonForm("K007", "Error Code - " + m.LParam.ToString(), true);
//
//                                    GlobalMembers.WriteMISData(GlobalMembers.Last_STAN, GlobalMembers.strBarcode, GlobalMembers.strLinesToPrint, "99");
//                                    GlobalMembers.WriteLog("Er:" + Convert.ToInt32(m.LParam.ToString()) + ", Generic error", "MSGComm");
//                                    GlobalMembers.WriteLog("ER:" + Convert.ToInt32(m.LParam.ToString()) + ", GENERIC ERROR");
//                                }
//                                break;
//                            }
//
//                            if (GlobalMembers.bIsNewTransaction)
//                            {
//                                GlobalMembers.bThreadProtection = false;
//                                PostMessage(GlobalMembers.MainHandle, TransactionEnd, (IntPtr)7, IntPtr.Zero);
//                            }
//                            else if (bTransactionDataReceived)
//                            {
//                                GlobalMembers.bThreadProtection = false;
//                                PostMessage(GlobalMembers.MainHandle, PassbookNegAckRequest, IntPtr.Zero, IntPtr.Zero);
//                            }
//                            else
//                            {
//                                GlobalMembers.bThreadProtection = false;
//                                PostMessage(GlobalMembers.MainHandle, TransactionEnd, IntPtr.Zero, IntPtr.Zero);
//                            }
//                        }
//                        catch (Exception ex)
//                        {
//                            GlobalMembers.WriteLog("Exception in WndProc->TransactionError: " + ex.Message, "MSGComm");
//
//                            GlobalMembers.bThreadProtection = false;
//                            PostMessage(GlobalMembers.MainHandle, TransactionEnd, IntPtr.Zero, IntPtr.Zero);
//                        }
                    }
                    break;
                    case PassbookScanned:{
                        try
                        {
                            GlobalMembers.objClientConfigIni.put("Kiosk_Details", "TxnFlow", "1");
                            GlobalMembers.bThreadProtection = true;
//                            HeartBeat.Enabled = false;

                            if (GlobalMembers.bIsNewTransaction)
                            {
                                Log.Write("***************TRANSACTION SESSION STARTED*******************");
                                Log.Write("***************TRANSACTION SESSION STARTED*******************", "MSGComm");
                            }

                            GlobalMembers.strBarcode = "";
                           GlobalMembers.DisplayImageOnForm("K003", "", true);

                            if (GlobalMembers.bIsPassbookEpson)
                            {
                                if (GlobalMembers.AccountNumber != null && GlobalMembers.AccountNumber.length() > 0)
                                {
                                    Log.Write("Barcode Read Successfully with Epson" );

                                    if (GlobalMembers.bMoreData)  //if page turn found
                                    {
                                        //If acc no found and matched with previous after page turn
                                        if (GlobalMembers.AccountNumber.equals(GlobalMembers.PrvAccountNumber))
                                        {
                                            //Account matched after page turn
                                            Log.Write("Account number match with previous");
                                            Log.Write("Barcode match");


                                            DisplayImageOnForm("K005", "", true);

                                            //compose the data and print on passbook, if function failed then send ACK
                                            String strBuffer = "";
//                                            if (ComposeAndPrintData(false,  strBuffer))
//                                            {
//                                                if (GlobalMembers.bIsPassbookEpson == false)
//                                                    Ollivtti_Obj.PrintData(strBuffer);
//                                                else
//                                                    EPSON_Obj.PrintData(strBuffer);
//
//                                                Log.Write("After Compose Print Data, set bIsPositiveACK = true" );
//                                                GlobalMembers.bIsPositiveACK = true;
//                                            }
//                                            else
//                                            {
//                                                //Error
//                                                GlobalMembers.bThreadProtection = false;
//                                                GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
////                                                PostMessage(GlobalMembers.MainHandle, TransactionError, IntPtr.Zero, IntPtr.Zero);
//                                            }
                                        }
                                        else
                                        {
                                            //Account mismatch
                                            Log.Write("ACCOUNT NUMBER MISMATCH WITH PREVIOUS" );
                                            Log.Write("Barcode mismatch");

                                            GlobalMembers.bThreadProtection = false;
                                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionError;
//                                            PostMessage(GlobalMembers.MainHandle, TransactionError, IntPtr.Zero, (IntPtr)1003);
                                        }
                                    }
                                    else
                                    {
                                        Log.Write("Barcode read- " + GlobalMembers.AccountNumber);
                                        GlobalMembers.PrvAccountNumber = GlobalMembers.AccountNumber;
                                        GlobalMembers.bThreadProtection = false;
                                        GlobalMembers.objTaskState = GlobalMembers.Task.AuthenticationRequest;
//                                        PostMessage(GlobalMembers.MainHandle, AuthenticationTransactionRequest, IntPtr.Zero, IntPtr.Zero);
                                    }
                                }
                            }
                            else
                            {
                                File file = new File(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R.bmp");
                                if (file.exists())
                                {
                                    GlobalMembers.bIsFullScanBarcode = (!objLksdbIni.get("DEMO", "Full_Scan_Barcode").toLowerCase().equals("no"));
                                    if (GlobalMembers.bIsFullScanBarcode)
                                    {
                                        //reset flag for not checking orientation in first try
                                        bIsCheckingBCOrientation = false; //v26.1.1. demo  //V26.1.1.10

                                        String Command = " --raw -q ";
                                        //todo
//                                        Runtime.getRuntime().exec("C:\\KIOSK\\zbarimg", Command + GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R.bmp");
//                                        commandPrompt = new CommandPrompt("C:\\KIOSK\\zbarimg", Command + GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + @"\R.bmp");

                                        // Run command asynchronously
//                                        commandPrompt.OutputDataReceived += Barcode_DataReceived;
//                                        commandPrompt.BeginRun();
                                    }
                                    else
                                    {
                                            //todo
//                                        CropImage(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R.bmp", ImageSplitType.Horizontal);  // V25.1.1.4

                                        //  CropImage(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + @"\R.bmp", GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + @"\Reardown.bmp", DOWN_BMP);

                                        //if image exist
                                        // if (File.Exists(GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + @"\Reardown.bmp"))
                                        File file1 = new File("GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + \"\\\\R_Top.bmp\"");
                                        if (file1.exists()) // R_Left  // V25.1.1.4  // wrng R_Bottom
                                        {
                                            String Command = " --raw -q ";

                                            //  commandPrompt = new CommandPrompt("C:\\KIOSK\\zbarimg", Command + GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + @"\Reardown.bmp");

//                                            commandPrompt = new CommandPrompt("C:\\KIOSK\\zbarimg", Command + GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + @"\R_Top.bmp"); //R_Top //R_Bottom  // V25.1.1.4
//                                            Runtime.getRuntime().exec("C:\\KIOSK\\zbarimg", Command + GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path + "\\R_Top.bmp");
                                            // Run command asynchronously
                                            //todo
//                                            commandPrompt.OutputDataReceived += Barcode_DataReceived;
//                                            commandPrompt.BeginRun();
                                        }
                                    }
                                }
                                else
                                {
                                    GlobalMembers.bThreadProtection = false;  //  added in V260.1.1.13  9 dec 2015
                                    GlobalMembers.objTaskState = GlobalMembers.Task.ReadBarcodeFromSD;
//                                    PostMessage(GlobalMembers.MainHandle, ReadBarcodeFromSD, IntPtr.Zero, IntPtr.Zero);
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.Write("Exception in WndProc->PassbookScanned: " + ex.getMessage());
                        }
                    }
                    break;
                    default:
                        break;
                }
                System.gc();
            }
        }
        ///////////////////////// End ///////////////////////////////////////////
    }


    private void UpdateSTAN() {
        if (objLksdbIni != null) {
            GlobalMembers.STAN = String.valueOf((Integer.parseInt(GlobalMembers.STAN) + 1));  //+ "D" + GlobalMembers.objISO[11].iLength);
            GlobalMembers.STAN = "000" + GlobalMembers.STAN;
            objLksdbIni.put("TXN", "STAN", GlobalMembers.STAN);
        }
    }
}



