package com.java.sample.interview.ae.email;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailService {
    List<Email> search(List<Email> all, String titleLike, Integer sender,
                       LocalDateTime start, LocalDateTime end);
}
