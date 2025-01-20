package com.java.sample.interview.ae.email;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EmailServiceImpl implements EmailService {

    /**
     * 从all中搜索
     * 返回符合查找条件的邮件列表
     **/
    public List<Email> search(List<Email> all,
                              String titleLike, Integer sender,
                              LocalDateTime start, LocalDateTime end) {
        return all.parallelStream()
                .filter(email -> email.choice(titleLike, sender, start, end))
                .collect(Collectors.toList());
    }
}
