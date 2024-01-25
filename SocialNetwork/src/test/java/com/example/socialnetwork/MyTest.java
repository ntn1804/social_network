package com.example.socialnetwork;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
public class MyTest {

    @Test
    public void testOtp() {
        String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
        System.out.println(otp);
    }
    
    @Test
    public void date(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        System.out.println("Date = "+ cal.getTime());
    }

}
