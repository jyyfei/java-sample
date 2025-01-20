package com.java.sample.interview.ae.email;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmailServiceTest {
    public static void main(String[] args) {
        List<Email> all = new ArrayList();
        all.add(new Email(11, "你好World", LocalDateTime.now()));
        all.add(new Email(22, "你好World", LocalDateTime.now()));
        EmailServiceImpl emailService = new EmailServiceImpl();
        List<Email> search = emailService.search(all, "", null, null, null);
        System.out.println(search);
    }
}
