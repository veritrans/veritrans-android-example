package id.co.veritrans.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.UUID;

import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.LocalDataHandler;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.TransactionRequest;
import id.co.veritrans.sdk.core.VeritransBuilder;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.BillInfoModel;
import id.co.veritrans.sdk.models.ItemDetails;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.models.UserDetail;

public class MainActivity extends AppCompatActivity {

    public static final String VT_CLIENT_KEY = "VT-client-Lre_JFh5klhfGefF";
    public static final String BASE_URL_MERCHANT_FOR_DEBUG = "https://hangout.betas.in/veritrans/api/";

    private Button coreCreditButton, uiCreditButton,
            corePermataButton, uiPermataButton,
            coreMandiriBillButton, uiMandiriBillButton;
    private ArrayList<PaymentMethodsModel> selectedPaymentMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSDK();
        initView();
    }

    /**
     * Initialize Veritrans SDK using VeritransBuilder.
     */
    private void initSDK() {
        VeritransBuilder veritransBuilder = new
                VeritransBuilder(getApplicationContext(), VT_CLIENT_KEY, BASE_URL_MERCHANT_FOR_DEBUG);
        veritransBuilder.enableLog(true);
        veritransBuilder.buildSDK();
        UserDetail userDetail = new UserDetail();
        userDetail.setMerchantToken("1241wmfkalsfafsa");
        LocalDataHandler.saveObject(getString(R.string.user_details), userDetail);
    }

    /**
     * Initialize transaction data.
     *
     * @return the transaction request.
     */
    private TransactionRequest initializePurchaseRequest() {
        TransactionRequest transactionRequestNew = new
                TransactionRequest(UUID.randomUUID().toString(), 360000);

        Logger.i(" created new transaction object ");
        ItemDetails itemDetails = new ItemDetails("1", 360000, 1, "shoes");
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequestNew.setItemDetails(itemDetailsArrayList);
        // bill info
        BillInfoModel billInfoModel = new BillInfoModel("demo_label", "demo_value");
        transactionRequestNew.setBillInfoModel(billInfoModel);
        return transactionRequestNew;
    }

    /**
     * Initialize the view.
     */
    private void initView() {
        // Handle Credit Card Payment using Core Flow
        coreCreditButton = (Button) findViewById(R.id.btn_credit_core);
        coreCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start credit card payment activity
                Intent intent = new Intent(MainActivity.this, CreditCardPaymentActivity.class);
                startActivity(intent);
            }
        });

        // Handle Credit Card Payment using UI Flow
        uiCreditButton = (Button) findViewById(R.id.btn_credit_ui);
        uiCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Payment Model using credit card
                ArrayList<PaymentMethodsModel> models = new ArrayList<>();
                PaymentMethodsModel model = new PaymentMethodsModel(getString(R.string.credit_card), id.co.veritrans.sdk.R.drawable.ic_credit, Constants.PAYMENT_METHOD_NOT_SELECTED);
                model.setIsSelected(true);
                models.add(model);
                selectedPaymentMethods = models;
                VeritransSDK.getVeritransSDK().setSelectedPaymentMethods(selectedPaymentMethods);

                // Create transaction request
                TransactionRequest transactionRequestNew = initializePurchaseRequest();
                transactionRequestNew.setCardPaymentInfo(getString(R.string.card_click_type_none),
                        false);
                // Set transaction request
                VeritransSDK.getVeritransSDK().setTransactionRequest(transactionRequestNew);

                // Start ui flow
                VeritransSDK.getVeritransSDK().startPaymentUiFlow(MainActivity.this);
            }
        });

        // Handle Permata VA Payment using Core Flow
        corePermataButton = (Button) findViewById(R.id.btn_permata_core);
        corePermataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PermataVAPaymentActivity.class);
                startActivity(intent);
            }
        });

        // Handle Permata VA Payment using UI Flow
        uiPermataButton = (Button) findViewById(R.id.btn_permata_ui);
        uiPermataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Payment Model using credit card
                ArrayList<PaymentMethodsModel> models = new ArrayList<>();
                PaymentMethodsModel model = new PaymentMethodsModel(getString(R.string.bank_transfer), id.co.veritrans.sdk.R.drawable.ic_atm, Constants.PAYMENT_METHOD_NOT_SELECTED);
                model.setIsSelected(true);
                models.add(model);
                selectedPaymentMethods = models;
                VeritransSDK.getVeritransSDK().setSelectedPaymentMethods(selectedPaymentMethods);

                // Create transaction request
                TransactionRequest transactionRequestNew = initializePurchaseRequest();
                // Set transaction request
                VeritransSDK.getVeritransSDK().setTransactionRequest(transactionRequestNew);

                // Start ui flow
                VeritransSDK.getVeritransSDK().startPaymentUiFlow(MainActivity.this);
            }
        });

        // Handle Mandiri Bill Payment using Core Flow
        coreMandiriBillButton = (Button) findViewById(R.id.btn_mandiri_bill_core);
        coreMandiriBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MandiriBillPaymentActivity.class);
                startActivity(intent);
            }
        });
    }
}
