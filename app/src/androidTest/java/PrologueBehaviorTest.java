import android.os.SystemClock;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import id.co.veritrans.sdk.sample.MainActivity;
import id.co.veritrans.sdk.sample.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author rakawm
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PrologueBehaviorTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private String fullName, email, phone;
    private String address, city, zipcode, country;

    @Before
    public void initString() {
        fullName = "Raka Westu Mogandhi";
        email = "westumogandhi@gmail.com";
        phone = "082140518011";
        address = "Jalan Wirajaya 312";
        city = "Yogyakarta";
        zipcode = "55198";
        country = "Indonesia";
    }

    @Test
    public void runPrologueBehaviorTest() {
        // Click get authentication token first
        onView(withId(R.id.btn_get_token)).perform(click());

        // Wait until get token finished
        SystemClock.sleep(2000);

        // Go to credit card
        onView(withId(R.id.btn_credit_ui)).perform(scrollTo(), click());

        // Fill consumer name
        onView(withId(R.id.et_full_name)).perform(clearText(), typeText(fullName), closeSoftKeyboard());
        onView(withId(R.id.et_email)).perform(clearText(), typeText(email), closeSoftKeyboard());
        onView(withId(R.id.et_phone)).perform(clearText(), typeText(phone), closeSoftKeyboard());

        // Click next button
        onView(withId(R.id.btn_next)).perform(click());

        // Fill consumer details
        onView(withId(R.id.et_address)).perform(clearText(), typeText(address), closeSoftKeyboard());
        onView(withId(R.id.et_city)).perform(clearText(), typeText(city), closeSoftKeyboard());
        onView(withId(R.id.et_zipcode)).perform(clearText(), typeText(zipcode), closeSoftKeyboard());
        onView(withId(R.id.et_country)).perform(clearText(), typeText(country), closeSoftKeyboard());

        // Click next button
        onView(withId(R.id.btn_next)).perform(click());
    }
}
