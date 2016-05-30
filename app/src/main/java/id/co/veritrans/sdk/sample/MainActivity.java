package id.co.veritrans.sdk.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.UUID;

import id.co.veritrans.sdk.activities.SaveCreditCardActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.LocalDataHandler;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.TransactionRequest;
import id.co.veritrans.sdk.core.VeritransBuilder;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.eventbus.callback.GetAuthenticationBusCallback;
import id.co.veritrans.sdk.eventbus.events.AuthenticationEvent;
import id.co.veritrans.sdk.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.models.BillInfoModel;
import id.co.veritrans.sdk.models.ItemDetails;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.scancard.ScanCard;

public class MainActivity extends AppCompatActivity implements GetAuthenticationBusCallback{

    ProgressDialog dialog;
    private TextView authToken;
    private Button coreCreditButton, uiCreditButton,
            corePermataButton, uiPermataButton,
            coreMandiriBillButton, uiMandiriBillButton,
            coreBCAVAButton, uiBCAVAButton,
            coreMandiriClickButton, uiMandiriClickButton,
            coreCIMBClickButton, uiCIMBClickButton,
            coreIndomaretButton, uiIndomaretButton,
            coreMandiriCashButton, uiMandiriCashButton,
            coreBCAKlikButton, uiBCAKlikButton,
            coreCardRegistration, uiCardRegistration,
            getAuthenticationToken;
    private ArrayList<PaymentMethodsModel> selectedPaymentMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VeritransBusProvider.getInstance().register(this);
        setContentView(R.layout.activity_main);
        initSDK();
        initView();
    }

    @Override
    protected void onDestroy() {
        VeritransBusProvider.getInstance().unregister(this);
        super.onDestroy();
    }
    /**
     * Initialize Veritrans SDK using VeritransBuilder.
     */
    private void initSDK() {
        VeritransBuilder veritransBuilder = new
                VeritransBuilder(getApplicationContext(), BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL);
        veritransBuilder.enableLog(true);
        veritransBuilder.setExternalScanner(new ScanCard());
        veritransBuilder.buildSDK();

        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        veritransSDK.setDefaultText("open_sans_regular.ttf");
        veritransSDK.setSemiBoldText("open_sans_semibold.ttf");
        veritransSDK.setBoldText("open_sans_bold.ttf");
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

        //Initialize progress dialog
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading");

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
                        true);
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
                // Set Payment Model using Permata VA/Bank Transfer
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

        // Handle Mandiri Bill Payment using UI Flow
        uiMandiriBillButton = (Button) findViewById(R.id.btn_mandiri_bill_ui);
        uiMandiriBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Payment Model using Mandiri Bill Payment
                ArrayList<PaymentMethodsModel> models = new ArrayList<>();
                PaymentMethodsModel model = new PaymentMethodsModel(getString(R.string.mandiri_bill_payment), id.co.veritrans.sdk.R.drawable.ic_mandiri_bill_payment2, Constants.PAYMENT_METHOD_NOT_SELECTED);
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

        // Handle BCA VA Payment using core flow
        coreBCAVAButton = (Button) findViewById(R.id.btn_bca_va_core);
        coreBCAVAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BCAPaymentActivity.class);
                startActivity(intent);
            }
        });

        // Handle BCA VA Payment using UI Flow
        uiBCAVAButton = (Button) findViewById(R.id.btn_bca_va_ui);
        uiBCAVAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Payment Model using Permata VA/Bank Transfer
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

        // Handle Mandiri ClickPay Payment using Core Flow
        coreMandiriClickButton = (Button) findViewById(R.id.btn_mandiri_click_core);
        coreMandiriClickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MandiriClickPaymentActivity.class);
                startActivity(intent);
            }
        });

        // Handle Mandiri ClickPay Payment using UI Flow
        uiMandiriClickButton = (Button) findViewById(R.id.btn_mandiri_click_ui);
        uiMandiriClickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Payment Model using Mandiri Click Pay
                ArrayList<PaymentMethodsModel> models = new ArrayList<>();
                PaymentMethodsModel model = new PaymentMethodsModel(getString(R.string.mandiri_click_pay), id.co.veritrans.sdk.R.drawable.ic_mandiri2, Constants.PAYMENT_METHOD_NOT_SELECTED);
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

        // Handle CIMB Click Payment using core flow
        coreCIMBClickButton = (Button) findViewById(R.id.btn_cimb_click_core);
        coreCIMBClickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CIMCBClickPaymentActivity.class);
                startActivity(intent);
            }
        });

        // Handle CIMB Click Payment using UI Flow
        uiCIMBClickButton = (Button) findViewById(R.id.btn_cimb_click_ui);
        uiCIMBClickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Payment Model using CIMB Click
                ArrayList<PaymentMethodsModel> models = new ArrayList<>();
                PaymentMethodsModel model = new PaymentMethodsModel(getString(R.string.cimb_clicks), id.co.veritrans.sdk.R.drawable.ic_cimb, Constants.PAYMENT_METHOD_NOT_SELECTED);
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

        // Handle Indomaret Payment using Core Flow
        coreIndomaretButton = (Button) findViewById(R.id.btn_indomaret_core);
        coreIndomaretButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IndomaretPaymentActivity.class);
                startActivity(intent);
            }
        });

        // Handle Indomaret Payment using Core Flow
        uiIndomaretButton = (Button) findViewById(R.id.btn_indomaret_ui);
        uiIndomaretButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Payment Model using Indomaret Payment
                ArrayList<PaymentMethodsModel> models = new ArrayList<>();
                PaymentMethodsModel model = new PaymentMethodsModel(getString(R.string.indomaret), id.co.veritrans.sdk.R.drawable.ic_indomaret, Constants.PAYMENT_METHOD_NOT_SELECTED);
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

        // Handle Mandiri E-Cash using Core Flow
        coreMandiriCashButton = (Button) findViewById(R.id.btn_mandiri_cash_core);
        coreMandiriCashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MandiriECashActivity.class);
                startActivity(intent);
            }
        });

        // Handle Mandiri E-Cash using UI Flow
        uiMandiriCashButton = (Button) findViewById(R.id.btn_mandiri_cash_ui);
        uiMandiriCashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Payment Model using Mandiri E cash
                ArrayList<PaymentMethodsModel> models = new ArrayList<>();
                PaymentMethodsModel model = new PaymentMethodsModel(getString(R.string.mandiri_e_cash), id.co.veritrans.sdk.R.drawable.ic_mandiri_e_cash, Constants.PAYMENT_METHOD_NOT_SELECTED);
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

        // Handle BCA KlikPay using Core Flow
        coreBCAKlikButton = (Button) findViewById(R.id.btn_bca_klik_core);
        coreBCAKlikButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BCAKlikPayActivity.class);
                startActivity(intent);
            }
        });

        // Handle BCA KlikPay using UI Flow
        uiBCAKlikButton = (Button) findViewById(R.id.btn_bca_klik_ui);
        uiBCAKlikButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Payment Model using BCA KlikPay
                ArrayList<PaymentMethodsModel> models = new ArrayList<>();
                PaymentMethodsModel model = new PaymentMethodsModel(getString(R.string.bca_klik), id.co.veritrans.sdk.R.drawable.ic_klikpay, Constants.PAYMENT_METHOD_NOT_SELECTED);
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

        // Handle Card registration using core flow
        coreCardRegistration = (Button)findViewById(R.id.btn_card_registration_core);
        coreCardRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SaveCreditCardActivity.class);
                startActivity(intent);
            }
        });

        // Handle Card registration using UI flow
        uiCardRegistration = (Button)findViewById(R.id.btn_card_registration_ui);
        uiCardRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeritransSDK.getVeritransSDK().startRegisterCardUIFlow(MainActivity.this);
            }
        });

        authToken = (TextView) findViewById(R.id.txt_auth_token);
        getAuthenticationToken = (Button) findViewById(R.id.btn_get_token);
        getAuthenticationToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeritransSDK.getVeritransSDK().getAuthenticationToken();
            }
        });
        refreshAuthenticationContainer();
    }

    private void refreshAuthenticationContainer() {
        if(VeritransSDK.getVeritransSDK().readAuthenticationToken()!=null
                && !VeritransSDK.getVeritransSDK().readAuthenticationToken().equals("")) {
            getAuthenticationToken.setVisibility(View.GONE);
            authToken.setText(getString(R.string.authentication_token_format, VeritransSDK.getVeritransSDK().readAuthenticationToken()));
        } else {
            getAuthenticationToken.setVisibility(View.VISIBLE);
            authToken.setText(getString(R.string.authentication_token_format, "Not Available"));
        }
    }

    @Subscribe
    @Override
    public void onEvent(AuthenticationEvent authenticationEvent) {
        String auth = authenticationEvent.getResponse().getxAuth();
        LocalDataHandler.saveString(Constants.AUTH_TOKEN, auth);
        refreshAuthenticationContainer();
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent networkUnavailableEvent) {
        // Handle network not available condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Network is unavailable")
                .create();
        dialog.show();
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent generalErrorEvent) {
        // Handle generic error condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Unknown error: " + generalErrorEvent.getMessage() )
                .create();
        dialog.show();
    }
}
