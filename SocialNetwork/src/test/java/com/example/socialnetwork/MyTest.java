package com.example.socialnetwork;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class MyTest {

    @Test
    public void testOtp() {
        String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
        System.out.println(otp);
    }

    @Test
    public void date(){
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime sevenDaysBefore = LocalDateTime.now().minusDays(7);
        System.out.println(time);
        System.out.println(sevenDaysBefore);
    }

    @Test
    public void testStream() {
        List<Integer> list = Arrays.asList(4,3,2,1,0,3,4,5);
        list.stream().distinct().sorted().forEach(System.out::println);
//        Stream<Integer> sortedList = list.stream().sorted();
//        System.out.println(sortedList);
    }

}
