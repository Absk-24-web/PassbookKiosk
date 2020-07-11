package Main;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static Main.GlobalMembers.*;

public class Timer implements Runnable {

    private int iMsgType;
    private int RotateCount;
    private InputStream input;
    private OutputStream output;
    private boolean bTransactionDataReceived;
    private boolean bIsCheckingBCOrientation = false;
    private static final String dirPath;
    private String[] strTxnLine = new String[0];
    private String[] strTxnPostedDateACK = new String[0];
    private String[] strEndBalanceACK = new String[0];
    private String[] strTransIdACK = new String[0];
    private String[] strDateACK = new String[0];
    private String[] strTransSerialNoACK = new String[0];

        static {
            dirPath = System.getProperty("user.dir");
        }
    
    @Override
    public void run() {
        while (true) {
            if (!bThreadProtection) {
                try {
                    if(client != null){
                        output = new DataOutputStream(client.getOutputStream());
                        input = new DataInputStream(client.getInputStream());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                switch (objTaskState) {
                    case TransactionStart: {
                        try {
                            bThreadProtection = true;
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

                            objClientConfigIni.put("Kiosk_Details", "TxnFlow", "0");
                            txtLabel.setVisible(false);
                            time.setVisible(false);
                            headingLabel.setVisible(false);
                            imgLabel.setIcon(DisplayImage("K002"));
                            RotateCount = 0;
                            bIsNewTransaction = true;
                            strTxnRRN = "";
                            bIsCheckingBCOrientation = false;
                            bMoreData = false;
                            bNextRequest = false;
                            bTransactionDataReceived = false;

                            //if Passbook is Demo, used for debugging code without printer
                            if (bIsPassbookDemo) {
                                Log.Write("***************TRANSACTION SESSION STARTED IN DEMO*******************");
                                Log.Write("Transaction started in demo");

                                //try read dummy accno for demo purpose
                                AccountNumber = objLksdbIni.get("DEMO", "Dummy_Acc_No");
                                bThreadProtection = false;
                                objTaskState = Task.AuthenticationRequest;
                            } else {
                                //todo printer
//                            if (GlobalMembers.bIsPassbookEpson == false)
//                                Ollivtti_Obj.StartPassbookCheck();
//                            else
//                                EPSON_Obj.StartPassbookCheck();
                            }
                        } catch (Exception ex) {
                            Log.Write("Exception in WndProc->TransactionStart: " + ex.getMessage());
                            bThreadProtection = false;
                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionEnd;
                        }
                    }
                    break;
                    case AuthenticationRequest: {
                        try {
                            bThreadProtection = true;

                            DisplayImageOnForm("K004","",true);

                            Log.Write("Authentication request");
                            ResetISOFields();

                            UpdateSTAN();

                            Arrays.copyOf(strTxnLine,0);
                            Arrays.copyOf(strTxnPostedDateACK,0);
                            Arrays.copyOf(strEndBalanceACK,0);
                            Arrays.copyOf(strTransIdACK,0);
                            Arrays.copyOf(strDateACK,0);
                            Arrays.copyOf(strTransSerialNoACK,0);

                            strTotalLinesPrinted = "0";
                            strTotalLinesToBePrinted = "0";
                            strLinesToPrint = "00";
                            objPBAckDetails.strLastLinePrinted = "00";

                            if (AccountNumber.length() != objACCOUNT_DETAILS.iAccount_Length) {
                                Log.Write("AUTHENTICATION REQUEST BARCODE DATA LEN IS NOT OK ");
                                bThreadProtection = false;
                                objTaskState = Task.TransactionError; errorCode = 1002;
                            } else {
                                // ISOMessage Compose
                                iMsgType = 1;   //For authentication
                                strDefMTI = "1200";
                                objISO[2].strValue = AccountNumber;
                                objISO[3].strValue = "110000";
                                objISO[4].strValue = "000000000000";
                                objISO[11].strValue = STAN;
                                Last_STAN = STAN;
                                objISO[12].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));  //DateTime.Now.ToString("yyyyMMddHHmm");
                                objISO[17].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));   //DateTime.Now.ToString("MMdd");
                                objISO[24].strValue = "200";
                                objISO[32].strValue = "888888";
                                objISO[35].strValue = "9619531053";

                                //16 digit Reference No (12 + 4)
                                objISO[37].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmmss" + STAN.substring(2))); //DateTime.Now.ToString("ddMMyyHHmmss") + GlobalMembers.STAN.substring(2);

                                objISO[41].strValue = "10000000";
                                objISO[43].strValue = "CEDGETHANE-WEST0000000000000000000MHIN00";
                                objISO[49].strValue = "356";
                                objISO[102].strValue = AccountNumber;
                                objISO[123].strValue = "SWT";
                                objISO[124].strValue = "KSK";
                                objISO[126].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy yyyy")); //DateTime.Now.ToString("ddMMyy yyyy");

                                Log.Write("Authentication Request", "MSGComm");
                                for (int iIterator = 2; iIterator < 128; iIterator++) {
                                    if (!objISO[iIterator].strValue.equals(""))
                                        Log.Write("Field " + iIterator + " - " + objISO[iIterator].strValue, "MSGComm");
                                }

                                //Compose ISO message, return error message if it fails
                                if (ISOMsgParser.ComposeISO8583Message(bRequest)) {

                                    //if TCP connection
                                    if (objCBS.bIs_TCP) {
                                        try {
                                            Log.Write("Connection establish with CBS");

                                            Log.Write("AUTHENTICATION REQUEST SENT TO CBS AND STAN IS " + STAN);

                                            //send request by tcp
                                            output.write(bRequest);
                                            output.flush();
                                            bThreadProtection = false;
                                            Log.Write("AuthenticationRequest message sent", "MSGComm");
                                            Log.Write(bRequest, "MSGComm");

                                            //Receive response by tcp
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            byte[] buffer = new byte[293];
                                            baos.write(buffer, 0, input.read(buffer));

                                            bResponse = baos.toByteArray();//ref bResponse
//                                            String res = Arrays.toString(GlobalMembers.bResponse);
//                                            System.out.println(res);
                                            objTaskState = Task.AuthenticationResponse;

                                        } catch (Exception ex) {
                                            Log.Write("AuthenticationRequest not sent(TCP): " + ex.getMessage(), "MSGComm");
                                            Log.Write("AUTHENTICATION REQUEST NOT SENT TO CBS VIA TCP AND STAN IS " + STAN + "  " + ex.getMessage());
                                            bThreadProtection = false;
                                            objTaskState = Task.TransactionEnd; errorCode  = 9;
                                        }
                                    }
                                } else {
                                    Log.Write("AuthenticationRequest message format error- " + strErrorMsg , "MSGComm");
                                    bThreadProtection = false;
                                    objTaskState = Task.TransactionError; errorCode = 1006;
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.Write("Exception in WndProc->AuthenticationRequest: " + ex.getMessage(), "MSGComm");
                            bThreadProtection = false;
                            objTaskState = Task.TransactionEnd;
                        }
                    }
                    break;
                    case AuthenticationResponse: {
                        try {
                            bThreadProtection = true;
//                        timer2.Enabled = false;
                            Log.Write("Authentication response");
                            Log.Write("AuthenticationResponse message received", "MSGComm");
                            Log.Write(bResponse, "MSGComm");

                            //load for DEMO purpose to match the STAN or not, read here so that setting immediately applied from next transaction.
                            if (objLksdbIni != null)
                                bIsSTANMatch = (!objLksdbIni.get("DEMO", "Check_STAN").toLowerCase().equals("no"));

                            for (int i = 0; i <= 128; i++) {
                                objISO[i].strValue = "";
                            }

                            //if reply msg filter properly
                            if (ISOMsgParser.FilterISO8583ResponseMessage()) {
                                Log.Write("AuthenticationResponse message", "MSGComm");
                                for (int iIterator = 2; iIterator < 128; iIterator++) {
                                    if (!objISO[iIterator].strValue.equals("")) {
                                        Log.Write("Field " + iIterator + " - " + objISO[iIterator].strValue, "MSGComm");
                                    }
                                }

                                Log.Write("AUTHENTICATION RESPONSE COMES FROM CBS AND STAN IS " + objISO[11].strValue);

                                //if status is 000
                                if (objISO[39].strValue != null && objISO[39].strValue.equals("000")) {
                                    //if field 125 contains error
                                    if (objISO[125].strValue.contains("Error")) {
                                        bThreadProtection = false;
                                        //send error no by adding 2000 to it
                                        objTaskState = Task.TransactionError;
                                        //todo substring
                                        errorCode = 2000 + Integer.parseInt(objISO[125].strValue.substring(14, 3));
                                    }
                                    //is STAN to be matched then is it matched
                                    else if (!bIsSTANMatch || Last_STAN.equals(objISO[11].strValue)) {
                                        //update the STAN
                                        UpdateSTAN();
                                        bThreadProtection = false;
                                        //send data request
                                        objTaskState = Task.PassbookDataRequest;
                                    } else {
                                        bThreadProtection = false;
                                        //STAN mismatch error
                                        objTaskState = Task.TransactionError; errorCode = 1005;
                                    }
                                } else {
                                    //update the STAN
                                    UpdateSTAN();
                                    Log.Write("AUTHENTICATION RESPONSE COMES WITH ERROR FROM CBS, ERROR IS " + objISO[39].strValue);
                                    bThreadProtection = false;
                                    //show error came
                                    objTaskState = Task.TransactionError;
                                    errorCode = Integer.parseInt(objISO[39].strValue);
                                }
                            } else {
                                objTxnIni.put("PBTxn_Details", "Result", objISO[39].strValue);
                                objTxnIni.put("PBTxn_Details", "Changes_DonePB", "1");

                                //show msg format error
                                Log.Write("AuthenticationResponse message format error-" + strErrorMsg , "MSGComm");
                                bThreadProtection = false;
                                objTaskState = Task.TransactionError; errorCode = 1006;
                            }
                        } catch (Exception ex) {
                            Log.Write("Exception in WndProc->AuthenticationResponse: " + ex.getMessage(), "MSGComm");
                            bThreadProtection = false;
                            objTaskState = Task.TransactionError; errorCode = 1006;
                        }
                    }
                    break;
                    case PassbookDataRequest: {
                        try {
                            bThreadProtection = true;

                            bMoreData = false;
                            bNextRequest = false;
                            isCoverPage = false;


                            Log.Write("Passbook Data request");

                            ResetISOFields();

                            // ISOMessage Compose
                            iMsgType = 3;   //For data rqst
                            strDefMTI = "1200";
                            objISO[2].strValue = AccountNumber;
                            objISO[3].strValue = "920000";
                            objISO[4].strValue = "000000000000";

                            objISO[11].strValue = STAN;
                            Last_STAN = STAN;
                            objISO[12].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")); //DateTime.Now.ToString("yyyyMMddHHmm");
                            objISO[17].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd")); //DateTime.Now.ToString("MMdd");
                            objISO[24].strValue = "200";
                            objISO[32].strValue = "888888";
                            objISO[35].strValue = "9619531053";

                            //16 digit Reference No (12 + 4)
                            if (strTxnRRN.equals("")) {
                                objISO[37].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmmss" + STAN.substring(2)));  //DateTime.Now.ToString("ddMMyyHHmmss") + GlobalMembers.STAN.subSString(2);
                                strTxnRRN = objISO[37].strValue;
                            } else {
                                objISO[37].strValue = strTxnRRN;
                            }

                            objISO[41].strValue = "10000000";
                            objISO[43].strValue = "CEDGETHANE-WEST0000000000000000000MHIN00";
                            objISO[49].strValue = "356";
                            //GlobalMembers.objISO[60].strValue = "0153TES1+000";
                            objISO[102].strValue = AccountNumber;
                            objISO[123].strValue = "SWT";
                            objISO[124].strValue = "KSK";
                            objISO[126].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy yyyy")); //DateTime.Now.ToString("ddMMyy yyyy");

                            Log.Write("PassbookDataRequest message", "MSGComm");
                            for (int iIterator = 2; iIterator < 128; iIterator++) {
                                if (!objISO[iIterator].strValue.equals("")) {
                                    Log.Write("Field " + iIterator + " - " + objISO[iIterator].strValue, "MSGComm");
                                }
                            }

                            if (ISOMsgParser.ComposeISO8583Message(bRequest)) {
                                if (objCBS.bIs_TCP) {
                                    try {
                                        Log.Write("Connection establish with CBS");

                                        Log.Write("PASSBOOK DATA REQUEST SENT TO CBS AND STAN IS " + STAN);

                                        Log.Write("PassbookDataRequest message sent", "MSGComm");
                                        Log.Write(bRequest, "MSGComm");

                                        //Send request
                                        output.write(bRequest);
                                        output.flush();
                                        bThreadProtection = false;

                                        //Receive response
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        byte[] buffer = new byte[2920];
                                        baos.write(buffer, 0, input.read(buffer));

                                        bResponse = baos.toByteArray();//ref bResponse
//                                            String res = Arrays.toString(GlobalMembers.bResposne);
//                                            System.out.println(res);
                                        objTaskState = Task.PassbookDataResponse;

                                    } catch (Exception ex) {
                                        Log.Write("PassbookDataRequest not sent: " + ex.getMessage(), "MSGComm");
                                        Log.Write("PASSBOOK DATA REQUEST NOT SENT TO CBS AND STAN IS " + STAN + "  " + ex.getMessage());
                                        bThreadProtection = false;
                                        objTaskState = Task.TransactionEnd; errorCode = 9;
                                    }
                                }
                            } else {
                                Log.Write("PassbookDataRequest message format error- "+strErrorMsg, "MSGComm");
                                bThreadProtection = false;
                                objTaskState = Task.TransactionError; errorCode = 1006;
                            }
                        } catch (Exception ex) {
                            Log.Write("Exception in WndProc->PassbookDataRequest: " + ex.getMessage());
                            bThreadProtection = false;
                            objTaskState = Task.TransactionEnd;
                        }
                    }
                    break;
                    case PassbookDataResponse: {
                        try {
                            bThreadProtection = true;
                            isCoverPage = false;

                            Log.Write("Passbook Data response");
                            Log.Write("PassbookDataResponse message received", "MSGComm");
                            Log.Write(bResponse, "MSGComm");

                            for (int i = 0; i <= 128; i++) {
                                objISO[i].strValue = "";
                            }

                            if (ISOMsgParser.FilterISO8583ResponseMessage()) {
                                Log.Write("PassbookDataResponse message", "MSGComm");
                                for (int iIterator = 2; iIterator < 128; iIterator++) {
                                    if (!objISO[iIterator].strValue.equals("")) {
                                        Log.Write("Field " + iIterator + " - " + objISO[iIterator].strValue, "MSGComm");
                                    }
                                }

                                Log.Write("PASSBOOK DATA RESPONSE COMES FROM CBS AND STAN IS " + objISO[11].strValue);

                                if (objISO[39].strValue != null && objISO[39].strValue.equals("000")) {
                                    if (!bIsSTANMatch || Last_STAN.equals(objISO[11].strValue)) {
                                        UpdateSTAN();

                                        if (objISO[125].strValue.equals("N00")) {
                                            Log.Write("No more pending txns in N00 case");

                                            bThreadProtection = false;
                                            GlobalMembers.objTaskState = GlobalMembers.Task.TransactionEnd;
                                        } else {
                                            DisplayImage("K005");
                                            Log.Write("Before Compose Print Data");
                                            bTransactionDataReceived = true;

                                            String strBuffer = "";
                                            // TODO: 11-07-2020  composeAndPrint
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
                                                objTaskState = Task.TransactionError; errorCode = 1012;
//                                            }
                                        }
                                    } else {
                                        bThreadProtection = false;
                                        objTaskState = Task.TransactionError; errorCode =1005;
                                    }
                                } else {
                                    UpdateSTAN();

                                    objTxnIni.put("PBTxn_Details", "Result", objISO[39].strValue);
                                    objTxnIni.put("PBTxn_Details", "Changes_DonePB", "1");

                                    Log.Write("PASSBOOK DATA RESPONSE COMES WITH ERROR FROM CBS, ERROR IS " + objISO[39].strValue);
                                    bThreadProtection = false;
                                    objTaskState = Task.TransactionError; errorCode = Integer.parseInt(objISO[39].strValue);
                                }
                            } else {
                                Log.Write("PassbookDataResponse message format error- "+strErrorMsg , "MSGComm");
                                bThreadProtection = false;
                                objTaskState = Task.TransactionError; errorCode = 1006;
                            }
                        } catch (Exception ex) {
                            Log.Write("Exception in WndProc->PassbookDataResponse: " + ex.getMessage());
                            bThreadProtection = false;
                            objTaskState = Task.TransactionError; errorCode = 1006;
                        }
                    }
                    break;
                    case TransactionEnd: {
                        try
                        {
                            bThreadProtection = true;
//                            HeartBeat.Enabled = true;

                            if (errorCode == 1)
                            {
                                DisplayImageOnForm("K021", "", true);
                            }
                            else if (errorCode == 2)
                            {
                                DisplayImageOnForm("K008", "", true);
                                Log.Write("Transaction end CoverPage");
                            }
                            else if (errorCode == 9) //No connectivity at the moment
                            {
                                DisplayImageOnForm("K017","",true);
                                Log.Write("Transaction end, because Server connection loss");
                            }
                            else if (errorCode == 4 || errorCode == 5 || errorCode == 6) //Passbook Error
                            {
                                Log.Write("Transaction end");
                            }
                            else if (errorCode == 7)   //Transaction Error
                            {
                                Log.Write("Transaction end");
                            }
                            else
                            {
                                DisplayImageOnForm("K006", "", true);
                                Log.Write("Transaction end");
                            }

                            //For DEMO PB, eject not need to be called, Eject called when PB is live only
                            //todo printer
//                            if (!bIsPassbookDemo)
//                            {
//                                if (bIsPassbookEpson == false)
//                                    Ollivtti_Obj.EjectPassbook();
//                                else
//                                    EPSON_Obj.EjectPassbook();
//                            }

                            Log.Write("PASSBOOK EJECTED" );
                            Log.Write("***************TRANSACTION SESSION ENDS*******************");
                            Log.Write("***************TRANSACTION SESSION ENDS*******************", "MSGComm");

//                            bThreadProtection = false;
                            objTaskState = Task.TransactionStart;
                        }
                        catch (Exception ex)
                        {
                            Log.Write("Exception in WndProc->TransactionEnd: " + ex.getMessage(), "MSGComm");
                        }
                    }
                    break;
                    case RequestTimeout: {
                        try {
                            bThreadProtection = true;
//                        timer2.Enabled = false;
//                        timer2.Interval = 30000;
                            Log.Write("TRANSACTION REQUEST TIMEOUT ");

                            bThreadProtection = false;
                            objTaskState = Task.TransactionEnd; errorCode=9;
                        } catch (Exception ex) {
                            Log.Write("Exception in WndProc->RequestTimeout: " + ex.getMessage());
                        }
                    }
                    break;
                    case TransactionError: {
                        try
                        {
                            bThreadProtection = true;

                            Log.Write("Txn error found");
                            switch (errorCode)
                            {
                                case 1002: // Barcode Lenght is Not OK
                                {
                                    DisplayImageOnForm("K009", "", true);
                                    Log.Write("Barcode read fails", "MSGComm");
                                    Log.Write("BARCODE READ FAILS");
                                }
                                break;
                                case 1003: // Barcode Re-Read and Verify Fails (Account Number Mismatch)
                                {
                                    DisplayImageOnForm("K011", "", true);
                                    Log.Write("Barcode verification fails", "MSGComm");
                                    Log.Write("BARCODE VERIFICATION FAILS");
                                }
                                break;
                                case 1004: // Passbook Jammed in Printer
                                {
                                    DisplayImageOnForm("K011","",true);
                                    Log.Write("Passbook jammed", "MSGComm");
                                    Log.Write("PASSBOOK JAMMED");
                                }
                                break;
                                case 1005: //STAN Mismatch
                                {
                                    DisplayImageOnForm("K007", "Error-MESSAGE CO-ORDINATION STAN MISMATCH",true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "16");
                                    Log.Write("Message co-ordination stan mismatch", "MSGComm");
                                    Log.Write("MESSAGE CO-ORDINATION STAN MISMATCH");
                                }
                                break;
                                case 1006: //Msg Format Error
                                {
                                    DisplayImageOnForm("K007", "Error-MESSAGE FORMAT",true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "08");
                                    Log.Write("Message format error", "MSGComm");
                                    Log.Write("MESSAGE FORMAT ERROR");
                                }
                                break;

                                case 169: //114(ACCOUNT HELD)
                                {
                                    DisplayImageOnForm("K007", "Err - 169(Invalid Account Number)", true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Err - 169(Invalid Account Number)", "MSGComm");
                                    Log.Write("Err - 169(Invalid Account Number)");
                                }
                                break;

                                case 114: //114(ACCOUNT HELD)
                                {
                                    DisplayImageOnForm("K007", "Err - 114(ACCOUNT HELD)", true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Err - 114(ACCOUNT HELD)", "MSGComm");
                                    Log.Write("Err - 114(ACCOUNT HELD)");
                                }
                                break;

                                case 911: //911(Timed out)
                                {
                                    DisplayImageOnForm("K007", "Err - 911(Time out)", true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Err - 911(Time out)", "MSGComm");
                                    Log.Write("Err - 911(Time out)");
                                }
                                break;

                                case 188://188(No record found)
                                {
                                    DisplayImageOnForm("K007", "Err - 188(No record found)", true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Err - 188(No record found)", "MSGComm");
                                    Log.Write("Err - 188(No record found)");
                                }
                                break;

                                case 909: //909(sys malfunction)
                                {
                                    DisplayImageOnForm("K007", "Err - 909(System malfunction)", true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Err - 909(System malfunction)", "MSGComm");
                                    Log.Write("Err - 909(System malfunction)");
                                }
                                break;

                                case 913: //913(duplicate transmission)
                                {
                                    DisplayImageOnForm("K007", "Err - 913(Duplicate transmission)", true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Err - 913(duplicate transmission)", "MSGComm");
                                    Log.Write("Err - 913(duplicate transmission)");
                                }
                                break;

                                case 121: //121(Exceeds WDL amt limit))
                                {
                                    DisplayImageOnForm("K007", "Err - 121(exceeds WDL amt limit)", true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Err - 121(exceeds WDL amt limit)", "MSGComm");
                                    Log.Write("Err - 121(exceeds WDL amt limit)");
                                }
                                break;

                                case 902: //902(Account Closed)
                                {
                                    DisplayImageOnForm("K007", "Err - 902(Invalid correction)", true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Err - 902(Invalid correction)", "MSGComm");
                                    Log.Write("Err - 902(Invalid correction)");
                                }
                                break;

                                case 100: //100(Account Closed)
                                {
                                    DisplayImageOnForm("K007", "Err - 100(Account Closed)", true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Err - 100(Account Closed)", "MSGComm");
                                    Log.Write("Err - 100(Account Closed)");
                                }
                                break;

                                case 119: //119(this transaction not permitted to Account holder)
                                {
                                    DisplayImageOnForm("K007", "Err - 119(This transaction not permitted to Account holder)", true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Err - 119(This transaction not permitted to Account holder)", "MSGComm");
                                    Log.Write("Err - 119(This transaction not permitted to Account holder)");
                                }
                                break;

                                case 116: //116(Insufficent funds)
                                {
                                    DisplayImageOnForm("K007", "Err - 116(Insufficent funds)", true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Err - 116(Insufficient funds)");
                                    Log.Write("Err - 116(Insufficient funds)", "MSGComm");
                                }
                                break;
                                case 855: //N00 Condition
                                {
                                    DisplayImageOnForm("K012","",true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Customer have no TXN to print(N00)", "MSGComm");
                                    Log.Write("CUSTOMER HAVE NO TXN TO PRINT(N00)");
                                }
                                break;
                                case 1012:
                                {
                                    DisplayImageOnForm("K013","",true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Error in ComposeandPrint", "MSGComm");
                                    Log.Write("Error in ComposeandPrint");
                                }
                                break;
                                case 2001: // Last print detail not found
                                {
                                    DisplayImage("K011");
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "11");
                                    Log.Write("Error001, Last print detail not found", "MSGComm");
                                    Log.Write("LAST PRINT DETAIL NOT FOUND");
                                }
                                break;
                                case 2002: //error due to first time printing
                                {
                                    DisplayImage("K022");
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Error002, Error due to first time printing", "MSGComm");
                                    Log.Write("ERROR DUE TO FIRST TIME PRINTING");
                                }
                                break;
                                case 2003: //Error capturing last print details
                                {
                                    DisplayImage("K023");
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "12");
                                    Log.Write("Error003, Error capturing last print details", "MSGComm");
                                    Log.Write("ERROR CAPTURING LAST PRINT DETAILS");
                                }
                                break;
                                case 2004: //get line number 0 in last print details
                                {
                                    DisplayImage("K048");
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "00");
                                    Log.Write("Error048, CBS send last printed Line no. 0, so terminate txn.", "MSGComm");
                                    Log.Write("ERROR 048 LAST PRINTED LINE IS 0 IN LAST PRINT DETAILS");
                                }
                                break;
                                case 2005: //Print Failed
                                {
                                    DisplayImageOnForm("K007", "Err - Print Failed", true);
                                    Log.Write("Err - Print Failed", "MSGComm");
                                    Log.Write("Err - Print Failed");
                                }
                                break;
                                default: //CBS Error Code
                                {
                                    DisplayImageOnForm("K007", "Error Code - " + errorCode, true);
                                    Log.WriteMISData(Last_STAN, strBarcode, strLinesToPrint, "99");
                                    Log.Write("Er:" + errorCode + ", Generic error", "MSGComm");
                                    Log.Write("ER:" + errorCode + ", GENERIC ERROR");
                                }
                                break;
                            }

                            if (bIsNewTransaction)
                            {
//                                bThreadProtection = false;
                                objTaskState = Task.TransactionEnd; errorCode = 7;
                            }
                            else if (bTransactionDataReceived)
                            {
//                                bThreadProtection = false;
                                objTaskState = Task.PassbookNegAckRequest;
                            }
                            else
                            {
//                                bThreadProtection = false;
                                objTaskState = Task.TransactionEnd;
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.Write("Exception in WndProc->TransactionError: " + ex.getMessage(), "MSGComm");

//                            bThreadProtection = false;
                            objTaskState = Task.TransactionEnd;
                        }
                    }
                    break;
                    case PassbookScanned:{
                        try
                        {
                            objClientConfigIni.put("Kiosk_Details", "TxnFlow", "1");
                            bThreadProtection = true;
//                            HeartBeat.Enabled = false;

                            if (bIsNewTransaction)
                            {
                                Log.Write("***************TRANSACTION SESSION STARTED*******************");
                                Log.Write("***************TRANSACTION SESSION STARTED*******************", "MSGComm");
                            }

                            strBarcode = "";
                           DisplayImageOnForm("K003", "", true);

                            if (bIsPassbookEpson)
                            {
                                if (AccountNumber != null && AccountNumber.length() > 0)
                                {
                                    Log.Write("Barcode Read Successfully with Epson" );

                                    if (bMoreData)  //if page turn found
                                    {
                                        //If acc no found and matched with previous after page turn
                                        if (AccountNumber.equals(PrvAccountNumber))
                                        {
                                            //Account matched after page turn
                                            Log.Write("Account number match with previous");
                                            Log.Write("Barcode match");


                                            DisplayImageOnForm("K005", "", true);

                                            //todo composeAndPrintData
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

                                            bThreadProtection = false;
                                            objTaskState = Task.TransactionError; errorCode = 1003;
                                        }
                                    }
                                    else
                                    {
                                        Log.Write("Barcode read- " + AccountNumber);
                                        PrvAccountNumber = AccountNumber;
                                        bThreadProtection = false;
                                        objTaskState = Task.AuthenticationRequest;
                                    }
                                }
                            }
                            else
                            {
                                File file = new File(objPATH_DETAIL.strPassbook_Image_Path + "\\R.bmp");
                                if (file.exists())
                                {
                                    bIsFullScanBarcode = (!objLksdbIni.get("DEMO", "Full_Scan_Barcode").toLowerCase().equals("no"));
                                    if (bIsFullScanBarcode)
                                    {
                                        //reset flag for not checking orientation in first try
                                        bIsCheckingBCOrientation = false;

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
                                    bThreadProtection = false;
                                    objTaskState = Task.ReadBarcodeFromSD;
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.Write("Exception in WndProc->PassbookScanned: " + ex.getMessage());
                        }
                    }
                    break;
                    case ReadBarcodeFromSD:{
                        bThreadProtection = true;
                        Log.Write("thrdBarcodeNotifier Set to read barcode " );
                        //StartReadBarcode = true;
                        //todo barcode
//                        thrdBarcodeNotifier.Set();
                    }
                    break;
                    case PassbookNegAckRequest:{
                        try
                        {
                            bThreadProtection = true;
                            isCoverPage = false;

                            bTransactionDataReceived = false;

                            iMsgType = 4;   //For NegACK

                            DisplayImageOnForm("K004", "", true);

                            Log.Write("Passbook NegAck Request");
                            ResetISOFields();

                            strDefMTI = "1200";
                            objISO[2].strValue = AccountNumber;
                            objISO[3].strValue = "930000";
                            objISO[4].strValue = "000000000000";

                            objISO[11].strValue = STAN;
                            Last_STAN = STAN;
                            objISO[12].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")); //DateTime.Now.ToString("yyyyMMddHHmm");
                            objISO[17].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));//DateTime.Now.ToString("MMdd");
                            objISO[24].strValue = "200";
                            objISO[32].strValue = "888888";
                            objISO[35].strValue = "9999999999";

                            //16 digit Reference No (12 + 4)
                            objISO[37].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmmss"))+ STAN.substring(2); //DateTime.Now.ToString("ddMMyyHHmmss") + GlobalMembers.STAN.Substring(2);

                            objISO[41].strValue = "10000000";
                            objISO[43].strValue = "CEDGETHANE-WEST0000000000000000000MHIN00";
                            objISO[49].strValue = "356";
                            //GlobalMembers.objISO[60].strValue = "0153TES1+000";
                            objISO[102].strValue = AccountNumber;
                            objISO[123].strValue = "SWT";
                            objISO[124].strValue = "KSK";
                            objISO[126].strValue = String.format("%9s", objPBAckDetails.strLastTransId).replace(' ','0') +
                                    String.format("%2s", objPBAckDetails.strLastLinePrinted).replace(' ','0');

                            Log.Write("PassbookNegAckRequest message", "MSGComm");
                            for (int iIterator = 2; iIterator < 128; iIterator++)
                            {
                                if (!objISO[iIterator].strValue.equals(""))
                                {
                                    Log.Write("Field " + iIterator + " - " + objISO[iIterator].strValue, "MSGComm");
                                }
                            }

                            if (ISOMsgParser.ComposeISO8583Message(bRequest))
                            {
                                Log.Write("PASSBOOK NEGACK REQUEST SENT TO CBS AND STAN IS " + STAN );

                                Log.Write("PassbookNegAckRequest message sent", "MSGComm");
                                Log.Write(bRequest, "MSGComm");

                                if (objCBS.bIs_TCP)
                                {
                                    try {
//                                        if (GlobalMembers.client != null)
//                                        {
//                                            client.Connect();
//                                        }

                                        Log.Write("Connection establish with CBS");

                                        //send to server
                                        output.write(bRequest);

                                        //receive from server
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        byte[] buffer = new byte[293];
                                        baos.write(buffer, 0, input.read(buffer));

                                        bResponse = baos.toByteArray();//ref bResponse
//                                            String res = Arrays.toString(GlobalMembers.bResposne);
//                                            System.out.println(res);

                                    }
                                    catch (Exception ex)
                                    {
                                        Log.Write("PassbookNegAckRequest not sent: " + ex.getMessage(), "MSGComm");
                                        Log.Write("PASSBOOK NEGACK REQUEST NOT SENT TO CBS AND STAN IS " + STAN + "  " + ex.getMessage() );
                                        Log.Write("NETWORK DISCONNECTED" );

                                        //todo
                                        //if network disconnected, try checking for connectivity
//                                        if (client.CommunicationState == Lipi.Communication.Scs.Communication.CommunicationStates.Disconnected)
//                                            thrdNetworkNotifier.Set();
                                    }
                                }
                            }
                            else
                            {
                                Log.Write("PassbookNegAckRequest message format error- "+strErrorMsg , "MSGComm");
                                bThreadProtection = false;
                                GlobalMembers.objTaskState = GlobalMembers.Task.TransactionEnd;
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.Write("Exception in WndProc->PassbookNegAckRequest: " + ex.getMessage(), "MSGComm");
                        }
                        finally
                        {
                            //start timer for checking request time out
                            //todo
//                            timer2.Interval = GlobalMembers.objKioskTimers.iTmrTransaction * 1000;
//                            timer2.Enabled = true;
                        }
                    }
                    break;
                    case PassbookNegAckResponse:{
                        try
                        {
                            bThreadProtection = true;

//                            timer2.Enabled = false;

                            Log.Write("Passbook NegACK response");
                            Log.Write("PassbookNegAckResponse message received", "MSGComm");
                            Log.Write(bResponse, "MSGComm");

                            for (int i = 0; i < 128; i++)
                            {
                                objISO[i].strValue = "";
                            }

                            if (ISOMsgParser.FilterISO8583ResponseMessage())
                            {
                                Log.Write("PassbookNegAckResponse message", "MSGComm");
                                for (int iIterator = 2; iIterator < 128; iIterator++)
                                {
                                    if (objISO[iIterator].strValue != "")
                                    {
                                        Log.Write("Field " + iIterator + " - " + objISO[iIterator].strValue, "MSGComm");
                                    }
                                }

                                Log.Write("PASSBOOK NEGACK RESPONSE COMES FROM CBS AND STAN IS " + objISO[11].strValue );
                            }

                            bThreadProtection = false;
                            objTaskState = Task.TransactionEnd;
                        }
                        catch (Exception ex)
                        {
                            Log.Write("Exception in WndProc->PassbookNegAckResponse: " + ex.getMessage(), "MSGComm");

                            bThreadProtection = false;
                            objTaskState = Task.TransactionEnd;
                        }
                    }
                    break;
                    case GetLastPrintDetailRequest:{
                        try
                        {
                            iMsgType = 2;
                            bThreadProtection = true;

                            Log.Write("Get last print details request");
                            ResetISOFields();
                            strLinesToPrint = "00";

                            // ISOMessage Compose
                            //For get last print details

                            //Fill data in ISO message fields
                            strDefMTI = "1200";
                            //GlobalMembers.objISO[2].strValue = GlobalMembers.strAccountNumber;
                            objISO[3].strValue = "310000";
                            objISO[4].strValue = "0000000000000000";
                            //GlobalMembers.objISO[7].strValue = DateTime.Now.ToString("yyyyMMddHHmm");
                            objISO[11].strValue = STAN;
                            Last_STAN = STAN;
                            objISO[12].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")); //DateTime.Now.ToString("yyyyMMddHHmmss");
                            objISO[17].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); //DateTime.Now.ToString("yyyyMMdd");
                            objISO[24].strValue = "200";
                            objISO[32].strValue = "0404";
                            objISO[34].strValue = "4575140050010116";
                            //GlobalMembers.objISO[37].strValue = "            "; //GlobalMembers.objISO8583.DE37;
                            objISO[41].strValue = String.format("%16s", objISO8583.DE41); //GlobalMembers.objISO8583.DE41.PadRight(16, ' ');
                            objISO[43].strValue = "BANKAWAY";
                            objISO[49].strValue = "INR";
                            objISO[102].strValue = "112        " + AccountNumber.substring(0, 4) + "     " + AccountNumber + "   ";// GlobalMembers.strAccountNumber;
                            objISO[123].strValue = "KSK";
                            objISO[125].strValue = "KIOSKPB1";

                            Log.Write("GetLastPrintDetailRequest msg", "MSGComm");
                            for (int iIterator = 2; iIterator < 128; iIterator++)
                            {
                                if (!objISO[iIterator].strValue.equals(""))
                                {
                                    Log.Write("Field " + iIterator + " - " + objISO[iIterator].strValue, "MSGComm");
                                }
                            }

                            //Compose ISO message, return error message if it fails
                            if (ISOMsgParser.ComposeISO8583Message(bRequest))
                            {
                                Log.Write("GETLASTPRINTDETAIL REQUEST SENT TO CBS AND STAN IS " + STAN);

                                Log.Write("GetLastPrintDetailRequest message sent", "MSGComm");
                                Log.Write(bRequest, "MSGComm");

                                //if TCP connection
                                if (objCBS.bIs_TCP)
                                {
                                    try
                                    {
                                        //send request by tcp
                                        output.write(bRequest);

                                        //receive response by tcp
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        byte[] buffer = new byte[293];
                                        baos.write(buffer, 0, input.read(buffer));

                                        bResponse = baos.toByteArray();//ref bResponse
//                                            String res = Arrays.toString(GlobalMembers.bResposne);
//                                            System.out.println(res);


                                    }
                                    catch (Exception ex)
                                    {
                                        Log.Write("GetLastPrintDetailRequest not sent(TCP): " + ex.getMessage(), "MSGComm");
                                        Log.Write("GETLASTPRINTDETAIL REQUEST NOT SENT TO CBSS VIA TCP AND STAN IS " + STAN + "  " + ex.getMessage());

                                        Log.Write("NETWORK DISCONNECTED");

                                        //todo
                                        //if network disconnected, try checking for connectivity
//                                        if (client.CommunicationState == Lipi.Communication.Scs.Communication.CommunicationStates.Disconnected)
//                                            thrdNetworkNotifier.Set();
                                    }
                                }
                            }
                            else
                            {
                                Log.Write("GetLastPrintDetailRequest message format error- "+strErrorMsg, "MSGComm");
                                bThreadProtection = false;  //  added in V260.1.1.13  9 dec 2015
                                objTaskState = Task.TransactionEnd;
//                                PostMessage(GlobalMembers.MainHandle, TransactionEnd, IntPtr.Zero, IntPtr.Zero);
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.Write("Exception in WndProc->GetLastPrintDetailRequest: " + ex.getMessage(), "MSGComm");
                        }
                        finally
                        {
                            //start timer for checking request time out
                            //todo
//                            timer2.Interval = GlobalMembers.objKioskTimers.iTmrTransaction * 1000;
//                            timer2.Enabled = true;
                        }
                    }
                    break;
                    case GetLastPrintDetailResponse:{
                        try
                        {
                            bThreadProtection = true;

//                            timer2.Enabled = false;

                            Log.Write("Get last print detail response");
                            Log.Write("GetLastPrintDetailResponse message received", "MSGComm");
                            Log.Write(bResponse, "MSGComm");

                            //load for DEMO purpose to match the STAN or not, read here so that setting immediately applied from next transaction.
                            if (objLksdbIni != null)
                                bIsSTANMatch = (!objLksdbIni.get("DEMO", "Check_STAN").toLowerCase().equals("no"));

                            //if reply msg filter properly
                            if (ISOMsgParser.FilterISO8583ResponseMessage())
                            {
                                Log.Write("GetLastPrintDetailResponse message", "MSGComm");
                                for (int iIterator = 2; iIterator < 128; iIterator++)
                                {
                                    if (!objISO[iIterator].strValue.equals(""))
                                    {
                                        Log.Write("Field " + iIterator + " - " + objISO[iIterator].strValue, "MSGComm");
                                    }
                                }

                                Log.Write("GETLASTPRINTDETAIL RESPONSE COMES FROM CBS AND STAN IS " + objISO[11].strValue);

                                //if status is 000
                                if (objISO[39].strValue != null && objISO[39].strValue.equals("000"))
                                {
                                    //if field 125 contains error
                                    if (objISO[125].strValue.contains("Error"))
                                    {
                                        bThreadProtection = false;
                                        //send error no by adding 2000 to it
                                        objTaskState = Task.TransactionError; errorCode = 2000 + Integer.parseInt(objISO[125].strValue.substring(14,3));
                                    }
                                    //is STAN to be matched then is it matched
                                    else if (!bIsSTANMatch || Last_STAN.equals(objISO[11].strValue))
                                    {
                                        //update the STAN
                                        UpdateSTAN();

                                        bThreadProtection = false;
                                        //send data request
                                        objTaskState = Task.PassbookDataRequest;
                                    }
                                    else
                                    {
                                        lblErrorCode.setText("");//29
                                        lblErrorCode.setVisible(true);

                                        bThreadProtection = false;
                                        //STAN mismatch error
                                        objTaskState = Task.TransactionError; errorCode = 1005;
                                    }
                                }
                                else
                                {
                                    //update the STAN
                                    UpdateSTAN();
                                    Log.Write("GETLASTPRINTDETAIL RESPONSE COMES WITH ERROR FROM CBS, ERROR IS " + objISO[39].strValue);

                                    bThreadProtection = false;
                                    //show error came
                                    objTaskState = Task.TransactionError; errorCode = Integer.parseInt(objISO[39].strValue);
                                }
                            }
                            else
                            {
                                //V26.1.2.9
                                objTxnIni.put("PBTxn_Details", "Result", objISO[39].strValue);
                                objTxnIni.put("PBTxn_Details", "Changes_DonePB", "1");

                                //show msg format error
                                Log.Write("GetLastPrintDetailResponse message format error- "+strErrorMsg , "MSGComm");
                                bThreadProtection = false;
                                objTaskState = Task.TransactionError; errorCode = 1006;
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.Write("Exception in WndProc->GetLastPrintDetailResponse: " + ex.getMessage(), "MSGComm");

                            bThreadProtection = false;
                            objTaskState = Task.TransactionEnd;
                        }
                    }
                    break;
                    case PassbookPrintNextPage:{
                        Log.Write("PassbookPrintNextPage case");
                        bThreadProtection = true;

                        //if more data found, when all txns not printed on same page and page turn required, also true when txn printed upto last line of page
                        if (bMoreData || bNextRequest)
                        {
                            //GlobalMembers.bMoreData = false;

                            if (bMoreData && Integer.parseInt(strTotalLinesPrinted) >= Integer.parseInt(strTotalLinesToBePrinted))
                            {
                                bMoreData = false;
                                //GlobalMembers.bThreadProtection = false;

                                //if pending txns are avlbl on CBS then send re-request else end the txn
                                if (bNextRequest)
                                {
                                    //send authentication request
                                    bThreadProtection = false;
                                    objTaskState = Task.AuthenticationRequest;
                                }
                                else
                                {

                                    bThreadProtection = false;
                                    objTaskState = Task.TransactionEnd;
                                }
                            }
                            else
                            {
                                Log.Write("MoreData, Re-request after page turn");

                                if (!bIsPassbookEpson)
                                {
                                    //todo printer
//                                    //eject the passbook
//                                    if (Ollivtti_Obj.EjectPassbook())
//                                    {
//                                        Log.Write("PB ejected and waiting for re-insert");
//
//                                        //Reset line number to 1 from where to start printing on next page
//                                        GlobalMembers.objLastTxnDetails.iLastLineNo = 1;
//
//                                        //passbook page turn state
//                                        GlobalMembers.bThreadProtection = false;
//                                        objTaskState = Task.PassbookRecheckState;
////                                        PostMessage(GlobalMembers.MainHandle, PassbookRecheckState, IntPtr.Zero, IntPtr.Zero);
//                                    }
//                                    else
//                                    {
//                                        Log.Write("PB not ejected");
//                                        //Error message passbook jam
//                                        GlobalMembers.bThreadProtection = false;
                                        objTaskState = Task.TransactionError; errorCode =1004;
//                                    }
                                }
                                else
                                {
                                    //todo printer
                                    //eject the passbook
//                                    if (EPSON_Obj.EjectPassbook())
//                                    {
//                                       Log.Write("PB ejected and waiting for re-insert");
//
//                                        //Reset line number to 1 from where to start printing on next page
//                                        GlobalMembers.objLastTxnDetails.iLastLineNo = 1;
//
//                                        //passbook page turn state
//                                        GlobalMembers.bThreadProtection = false;
//                                        objTaskState = Task.PassbookRecheckState;
////                                        PostMessage(GlobalMembers.MainHandle, PassbookRecheckState, IntPtr.Zero, IntPtr.Zero);
//                                    }
//                                    else
//                                    {
//                                        Log.Write("PB not ejected");
//                                        //Error message passbook jam
//                                        GlobalMembers.bThreadProtection = false;
                                        objTaskState = Task.TransactionError; errorCode = 1004;
//                                    }
                                }
                            }
                        }
                        else
                        {
                            Log.Write("No more pending txns");
                            bThreadProtection = false;
                            objTaskState = Task.TransactionEnd;
                        }
                    }
                    break;
                    case PassbookRecheckState:{
                        bThreadProtection = true;
                        DisplayImageOnForm("K018", "", true);
                        Log.Write("Paasbook check again");
                        bIsNewTransaction = false;

                        //if Passbook is Demo, used for debugging code without printer
                        if (bIsPassbookDemo)
                        {
                            //try read dummy accno for demo purpose
                            strBarcode = objLksdbIni.get("DEMO", "Dummy_Acc_No");
                            bThreadProtection = false;
                            objTaskState = Task.AuthenticationRequest;
                        }
                        else
                        {
                            //todo printer
//                            if (GlobalMembers.bIsPassbookEpson == false)
//                                Ollivtti_Obj.StartPassbookCheck();
//                            else
//                                EPSON_Obj.StartPassbookCheck();
                        }
                    }
                    break;
                    case PassbookCoverPageAckRequest:{
                        try
                        {
                            bThreadProtection = true;
                            ResetISOFields();
                            iMsgType = 5;   //ForCoverPage ACK Request
//                            timer2.Enabled = false;
                            isCoverPage = false;
                            DisplayImageOnForm("K004", "", true);

                            Log.Write("Cover pageACK request");

                            strDefMTI = "1200";

                            objISO[3].strValue = "310000";

                            objISO[11].strValue = STAN;
                            Last_STAN = STAN;
                            objISO[12].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")); //DateTime.Now.ToString("yyyyMMddHHmmss");
                            objISO[17].strValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); //DateTime.Now.ToString("yyyyMMdd");
                            objISO[24].strValue = "200";
                            objISO[32].strValue = "0404";
                            objISO[34].strValue = "4575140050010116";
                            objISO[41].strValue = String.format("%16s", objISO8583.DE41); //GlobalMembers.objISO8583.DE41.PadRight(16, ' ');
                            objISO[43].strValue = "BANKAWAY";
                            objISO[49].strValue = "INR";
                            objISO[102].strValue = "112        " + AccountNumber.substring(0, 4) + "     " + AccountNumber + "   ";// GlobalMembers.strAccountNumber;
                            objISO[123].strValue = "KSK";
                            objISO[125].strValue = "KIOSKPB3";
                            Log.Write("PassbookCoverPageAckRequest message", "MSGComm");
                            for (int iIterator = 2; iIterator < 128; iIterator++)
                            {
                                if (!objISO[iIterator].strValue.equals(""))
                                {
                                    Log.Write("Field " + iIterator + " - " + objISO[iIterator].strValue, "MSGComm");
                                }
                            }

                            if (ISOMsgParser.ComposeISO8583Message(bRequest))
                            {
                                Log.Write("PASSBOOK COVERPAGE ACK REQUEST SENT TO CBS AND STAN IS " + STAN );

                                Log.Write("PassbookCoverPageAckRequest message sent", "MSGComm");
                                Log.Write(bRequest, "MSGComm");

                                if (objCBS.bIs_TCP)
                                {
                                    try
                                    {
                                        //send request
                                        output.write(bRequest);

                                        //receive response
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        byte[] buffer = new byte[293];
                                        baos.write(buffer, 0, input.read(buffer));

                                        bResponse = baos.toByteArray();//ref bResponse
//                                            String res = Arrays.toString(GlobalMembers.bResposne);
//                                            System.out.println(res);
                                  }
                                    catch (Exception ex)
                                    {
                                        Log.Write("PassbookCoverPageAckRequest not sent: " + ex.getMessage(), "MSGComm");
                                        Log.Write("PASSBOOK COVER PAGE ACK REQUEST NOT SENT TO CBS AND STAN IS " + STAN + "  " + ex.getMessage() );

                                        Log.Write("NETWORK DISCONNECTED" );

                                        //todo network checking
                                        //if network disconnected, try checking for connectivity
//                                        if (client.CommunicationState == Lipi.Communication.Scs.Communication.CommunicationStates.Disconnected)
//                                            thrdNetworkNotifier.Set();
                                    }
                                }
                            }
                            else
                            {
                                Log.Write("PassbookCoverPageAckRequest message format error- " +strErrorMsg, "MSGComm");
                                bThreadProtection = false;
                                objTaskState = Task.TransactionEnd;
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.Write("Exception in WndProc->PassbookCoverPageAckRequest: " + ex.getMessage(), "MSGComm");
                        }
                        finally
                        {
                            //todo timer
//                            timer2.Interval = GlobalMembers.objKioskTimers.iTmrTransaction * 1000;
//                            timer2.Enabled = true;
                        }
                    }
                    break;
                    case PassbookCoverPageAckResponse:{
                        try
                        {
                            bThreadProtection = true;

//                            timer2.Enabled = false;
                            bIsPositiveACK = true;

                            Log.Write("CoverPageACK response");
                            Log.Write("PassbookCoverACKResponse message received", "MSGComm");
                            Log.Write(bResponse, "MSGComm");

                            if (ISOMsgParser.FilterISO8583ResponseMessage())
                            {
                                Log.Write("PassbookCoverACKResponse message", "MSGComm");
                                for (int iIterator = 2; iIterator < 128; iIterator++)
                                {
                                    if (!objISO[iIterator].strValue.equals(""))
                                    {
                                        Log.Write("Field " + iIterator + " - " + objISO[iIterator].strValue, "MSGComm");
                                    }
                                }

                                if (objISO[125].strValue.toLowerCase().equals("y"))
                                {
                                    bThreadProtection = false;
                                    objTaskState = Task.TransactionEnd; errorCode = 2;
                                }
                                else
                                {
                                    bThreadProtection = false;
                                    objTaskState = Task.TransactionError;
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.Write("Exception in WndProc->Passbook CoverPageACKResponse: " + ex.getMessage(), "MSGComm");
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



